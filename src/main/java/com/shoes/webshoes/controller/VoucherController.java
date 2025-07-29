package com.shoes.webshoes.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoes.webshoes.common.enums.DiscountTypeEnum;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.common.utils.StringErrorValue;
import com.shoes.webshoes.common.utils.Utils;
import com.shoes.webshoes.entity.Cart;
import com.shoes.webshoes.entity.CartDetail;
import com.shoes.webshoes.entity.Product;
import com.shoes.webshoes.entity.ProductDetail;
import com.shoes.webshoes.entity.Users;
import com.shoes.webshoes.entity.Voucher;
import com.shoes.webshoes.entity.VoucherApplication;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.AppyVoucherRequest;
import com.shoes.webshoes.request.CRUDVoucherRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.VoucherApplyResponse;
import com.shoes.webshoes.response.VoucherResponse;
import com.shoes.webshoes.service.CartDetailService;
import com.shoes.webshoes.service.CartService;
import com.shoes.webshoes.service.ProductDetailService;
import com.shoes.webshoes.service.ProductService;
import com.shoes.webshoes.service.VoucherApplicationService;
import com.shoes.webshoes.service.VoucherService;

@RestController
@RequestMapping("/api/v1/voucher")
public class VoucherController extends BaseController {
	@Autowired
	public VoucherService voucherService;
	
	@Autowired
	public VoucherApplicationService voucherApplicationService;

	@Autowired
	public CartService cartService;

	@Autowired
	public CartDetailService cartDetailService;

	@Autowired
	public ProductDetailService productDetailService;

	@Autowired
	public ProductService productService;

	@GetMapping("/best-voucher")
	public ResponseEntity<BaseResponse<VoucherApplyResponse>> getBestVoucher() throws Exception {
		BaseResponse<VoucherApplyResponse> response = new BaseResponse<>();
		Users users = this.getUser();

		Cart cart = cartService.spGListCart(users.getId(), "", -1, new Pagination(0, 20)).getResult().stream()
				.findFirst().orElse(null);
		if (cart == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Cart not found");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		List<CartDetail> listCartDetail = cartDetailService
				.spGListCartDetail(cart.getId(), -1, "", 1, new Pagination(0, 20)).getResult();
		if (listCartDetail.isEmpty()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Cart is empty");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Map<Integer, CartDetail> cartDetailMap = new HashMap<>();
		for (CartDetail cartDetail : listCartDetail) {
			cartDetailMap.put(cartDetail.getProductDetailId(), cartDetail);
		}

		Set<Integer> listProductDetailIds = listCartDetail.stream().map(item -> item.getProductDetailId())
				.collect(Collectors.toSet());

		List<ProductDetail> productDetails = productDetailService.findByIds(new ArrayList<>(listProductDetailIds));
		Map<Integer, ProductDetail> productDetailMap = new HashMap<>();
		for (ProductDetail productDetail : productDetails) {
			productDetailMap.put(productDetail.getId(), productDetail);
		}

		BigDecimal totalAmount = listCartDetail.stream().map(cartDetail -> {
			ProductDetail productDetail = productDetailMap.get(cartDetail.getProductDetailId());
			if (productDetail != null) {
				return productDetail.getPrice().multiply(BigDecimal.valueOf(cartDetail.getQuantity()));
			}
			return BigDecimal.ZERO;
		}).reduce(BigDecimal.ZERO, BigDecimal::add);

		List<Voucher> availableVouchers = voucherService.getAll().stream().filter(Voucher::isCurrentDateInRange)
				.filter(voucher -> !voucher.isNumberLimit())
				.filter(voucher -> totalAmount.compareTo(voucher.getMinOrderValue()) >= 0).collect(Collectors.toList());

		BigDecimal bestDiscountAmount = BigDecimal.ZERO;
		Voucher bestVoucher = null;
		VoucherResponse bestVoucherResponse = null;
		List<Product> products = productService.getAll();
		Map<Integer, Product> productMap = new HashMap<>();
		for (Product product : products) {
			productMap.put(product.getId(), product);
		}
		for (Voucher voucher : availableVouchers) {
			BigDecimal amountVoucher = BigDecimal.ZERO;
			VoucherApplication voucherApplication = voucherApplicationService
					.spGListVoucherApplication(voucher.getId(), -1, -1, -1, "", 1, new Pagination(0, 20)).getResult()
					.stream().findFirst().orElse(null);
			Set<Integer> listProductIdsApplyVoucher = new HashSet<>();
			if (voucherApplication == null || (Utils.isEmpty(voucherApplication.getProductId())
					&& Utils.isEmpty(voucherApplication.getBrandId())
					&& Utils.isEmpty(voucherApplication.getCategoryId()))) {
				amountVoucher = calculateTotalAmountApplyVoucher(totalAmount, voucher);
			} else {
				amountVoucher = calculateAmountWithVoucherApplication(listCartDetail, productDetails, productDetailMap,
						voucher, voucherApplication, listProductIdsApplyVoucher, productMap);
			}

			if (amountVoucher.compareTo(bestDiscountAmount) > 0) {
				if(amountVoucher.compareTo(totalAmount) >= 0) {
					bestDiscountAmount = totalAmount;
				} else {
					bestDiscountAmount = amountVoucher;
				}
				bestVoucher = voucher;
				if (bestVoucher != null) {
					bestVoucherResponse = new VoucherResponse(bestVoucher);
				}
			}
		}

		if (bestVoucher != null) {
			if (bestDiscountAmount.compareTo(bestVoucher.getMaxDiscount()) > 0) {
				bestDiscountAmount = bestVoucher.getMaxDiscount();
			}
		}

		BigDecimal discountedTotal = totalAmount.subtract(bestDiscountAmount);
		VoucherApplyResponse voucherApplyResponse = new VoucherApplyResponse(discountedTotal, bestDiscountAmount);
		voucherApplyResponse.setVoucher(bestVoucherResponse);
		response.setData(voucherApplyResponse);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private BigDecimal calculateAmountWithVoucherApplication(List<CartDetail> listCartDetail,
			List<ProductDetail> productDetails, Map<Integer, ProductDetail> productDetailMap, Voucher voucher,
			VoucherApplication voucherApplication, Set<Integer> listProductIdsApplyVoucher,
			Map<Integer, Product> productMap) {
		BigDecimal amountVoucher = BigDecimal.ZERO;
		if (voucherApplication != null && !Utils.isEmpty(voucherApplication.getCategoryId())) {
			for (CartDetail cartDetail : listCartDetail) {
				ProductDetail productDetail = productDetailMap.get(cartDetail.getProductDetailId());
				if (listProductIdsApplyVoucher.contains(productDetail.getId())) {
					continue;
				}
				if (productDetail != null) {
					Product product = productMap.get(productDetail.getProductId());
					if (voucherApplication.getCategoryId().equals(product.getCategoryId())) {
						amountVoucher = amountVoucher
								.add(calculateTotalAmountApplyVoucher(productDetail.getPrice(), voucher));
						listProductIdsApplyVoucher.add(productDetail.getId());
					}
				}
			}
		}
		if (voucherApplication != null && !Utils.isEmpty(voucherApplication.getBrandId())) {
			for (CartDetail cartDetail : listCartDetail) {
				ProductDetail productDetail = productDetailMap.get(cartDetail.getProductDetailId());
				if (listProductIdsApplyVoucher.contains(productDetail.getId())) {
					continue;
				}
				if (productDetail != null) {
					Product product = productMap.get(productDetail.getProductId());
					if (voucherApplication.getBrandId().equals(product.getBrandId())) {
						amountVoucher = amountVoucher
								.add(calculateTotalAmountApplyVoucher(productDetail.getPrice(), voucher));
						listProductIdsApplyVoucher.add(productDetail.getId());
					}
				}
			}
		}
		if (voucherApplication != null && !Utils.isEmpty(voucherApplication.getProductId())) {
			for (CartDetail cartDetail : listCartDetail) {
				ProductDetail productDetail = productDetailMap.get(cartDetail.getProductDetailId());
				if (listProductIdsApplyVoucher.contains(productDetail.getId())) {
					continue;
				}
				if (productDetail != null && voucherApplication.getProductId().equals(productDetail.getProductId())) {
					amountVoucher = amountVoucher
							.add(calculateTotalAmountApplyVoucher(productDetail.getPrice(), voucher));
					listProductIdsApplyVoucher.add(productDetail.getId());
				}
			}
		}
		return amountVoucher;
	}

	

	@GetMapping("")
	// @PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<VoucherResponse>>> getAll(
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<VoucherResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<Voucher> listVoucher = voucherService.spGListVoucher(keySearch, status, pagination);

		BaseListDataResponse<VoucherResponse> listData = new BaseListDataResponse<>();

		listData.setList(new VoucherResponse().mapToList(listVoucher.getResult()));
		listData.setTotalRecord(listVoucher.getTotalRecord());

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BaseResponse<VoucherResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<VoucherResponse> response = new BaseResponse<>();
		Voucher voucher = voucherService.findOne(id);

		if (voucher == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		response.setData(new VoucherResponse(voucher));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<VoucherResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
		BaseResponse<VoucherResponse> response = new BaseResponse<>();
		Voucher voucher = voucherService.findOne(id);

		if (voucher == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		voucher.setStatus(voucher.getStatus() == 1 ? 0 : 1);

		voucherService.update(voucher);
		response.setData(new VoucherResponse(voucher));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<BaseResponse<VoucherResponse>> create(@Valid @RequestBody CRUDVoucherRequest wrapper)
			throws Exception {

		BaseResponse<VoucherResponse> response = new BaseResponse<>();
		Voucher voucherCheck = voucherService.findByName(wrapper.getCode());

		if (voucherCheck != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if(wrapper.getDiscountType() == DiscountTypeEnum.PERCENT.getValue() && wrapper.getDiscountValue().compareTo(new BigDecimal(100)) > 0) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_DISCOUNT_VALUE_INVALID);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Voucher voucher = new Voucher();
		voucher.setCode(wrapper.getCode());
		voucher.setDiscountType(wrapper.getDiscountType());
		voucher.setDiscountValue(wrapper.getDiscountValue());
		voucher.setMinOrderValue(wrapper.getMinOrderValue());
		voucher.setMaxDiscount(wrapper.getMaxDiscount());
		voucher.setStartDate(Utils.convertStringToDate(wrapper.getStartDate()));
		voucher.setEndDate(Utils.convertStringToDate(wrapper.getEndDate()));
		voucher.setUsageLimit(wrapper.getUsageLimit());
		voucher.setUsedCount(wrapper.getUsedCount());
		voucher.setStatus(1);

		voucherService.create(voucher);
		response.setData(new VoucherResponse(voucher));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update")
	public ResponseEntity<BaseResponse<VoucherResponse>> update(@PathVariable("id") int id,
			@Valid @RequestBody CRUDVoucherRequest wrapper) throws Exception {

		BaseResponse<VoucherResponse> response = new BaseResponse<>();
		Voucher voucher = voucherService.findOne(id);

		if (voucher == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (!voucher.getCode().equals(wrapper.getCode()) && voucherService.findByName(wrapper.getCode()) != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}
		if(wrapper.getDiscountType() == DiscountTypeEnum.PERCENT.getValue() && wrapper.getDiscountValue().compareTo(new BigDecimal(100)) > 0) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_DISCOUNT_VALUE_INVALID);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		voucher.setCode(wrapper.getCode());
		voucher.setDiscountType(wrapper.getDiscountType());
		voucher.setDiscountValue(wrapper.getDiscountValue());
		voucher.setMinOrderValue(wrapper.getMinOrderValue());
		voucher.setMaxDiscount(wrapper.getMaxDiscount());
		voucher.setStartDate(Utils.convertStringToDate(wrapper.getStartDate()));
		voucher.setEndDate(Utils.convertStringToDate(wrapper.getEndDate()));
		voucher.setUsageLimit(wrapper.getUsageLimit());
		voucher.setUsedCount(wrapper.getUsedCount());
		voucher.setStatus(1);
		voucherService.update(voucher);

		response.setData(new VoucherResponse(voucher));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/apply")
	public ResponseEntity<BaseResponse<VoucherApplyResponse>> applyVoucher(@PathVariable("id") int id,
			@Valid @RequestBody AppyVoucherRequest wrapper) throws Exception {

		BaseResponse<VoucherApplyResponse> response = new BaseResponse<>();
		Users users = this.getUser();
		Voucher voucher = voucherService.findOne(id);
		if (voucher == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Cart cart = cartService.spGListCart(users.getId(), "", -1, new Pagination(0, 20)).getResult().stream()
				.findFirst().orElse(null);
		List<CartDetail> listCartDetail = cartDetailService
				.spGListCartDetail(cart.getId(), -1, "", 1, new Pagination(0, 20)).getResult();
		Map<Integer, CartDetail> cartDetailMap = new HashMap<>();
		for (CartDetail cartDetail : listCartDetail) {
			cartDetailMap.put(cartDetail.getProductDetailId(), cartDetail);
		}
		Set<Integer> listProductIdsApplyVoucher = new HashSet<>();

		Set<Integer> listProductDetailIds = listCartDetail.stream().map(item -> item.getProductDetailId())
				.collect(Collectors.toSet());

		List<ProductDetail> productDetails = productDetailService.findByIds(new ArrayList<>(listProductDetailIds));
		Set<Integer> listProductIds = productDetails.stream().map(item -> item.getProductId())
				.collect(Collectors.toSet());
		List<Product> products = productService.findByIds(new ArrayList<>(listProductIds));
		Map<Integer, Product> productMap = new HashMap<>();
		for (Product product : products) {
			productMap.put(product.getId(), product);
		}
		if (!voucher.isCurrentDateInRange() || voucher.isNumberLimit()
				|| wrapper.getTotalAmount().compareTo(voucher.getMinOrderValue()) < 0) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_IS_NOT_APPLY);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		BigDecimal totalAmount = wrapper.getTotalAmount();
		BigDecimal amountVoucher = BigDecimal.ZERO;

		VoucherApplication voucherApplication = voucherApplicationService
				.spGListVoucherApplication(voucher.getId(), -1, -1, -1, "", 1, new Pagination(0, 20)).getResult()
				.stream().findFirst().orElse(null);
		if (voucherApplication == null
				|| (Utils.isEmpty(voucherApplication.getProductId()) && Utils.isEmpty(voucherApplication.getBrandId())
						&& Utils.isEmpty(voucherApplication.getCategoryId()))) {
			amountVoucher = calculateTotalAmountApplyVoucher(wrapper.getTotalAmount(), voucher);
		}
		if (voucherApplication != null && !Utils.isEmpty(voucherApplication.getCategoryId())) {
			for (ProductDetail productDetail : productDetails) {
				if (listProductIdsApplyVoucher.contains(productDetail.getId())) {
					continue;
				}
				Integer productId = productDetail.getProductId();
				BigDecimal price = productDetail.getPrice();
				Product product = productMap.get(Integer.valueOf(productDetail.getProductId()));

				if (voucherApplication.getCategoryId().equals(product.getCategoryId())) {
					CartDetail cartDetailFromMap = cartDetailMap.get(Integer.valueOf(productDetail.getId()));
					amountVoucher = amountVoucher.add(calculateTotalAmountApplyVoucher(price, voucher));
					listProductIdsApplyVoucher.add(Integer.valueOf(productDetail.getId()));
				}
			}
		}
		if (voucherApplication != null && !Utils.isEmpty(voucherApplication.getBrandId())) {
			for (ProductDetail productDetail : productDetails) {
				if (listProductIdsApplyVoucher.contains(productDetail.getId())) {
					continue;
				}
				BigDecimal price = productDetail.getPrice();
				Product product = productMap.get(Integer.valueOf(productDetail.getProductId()));

				if (voucherApplication.getBrandId().equals(product.getBrandId())) {
					CartDetail cartDetailFromMap = cartDetailMap.get(Integer.valueOf(productDetail.getId()));
					amountVoucher = amountVoucher.add(calculateTotalAmountApplyVoucher(price, voucher));
					listProductIdsApplyVoucher.add(Integer.valueOf(productDetail.getId()));
				}
			}
		}
		if (voucherApplication != null && !Utils.isEmpty(voucherApplication.getProductId())) {
			for (ProductDetail productDetail : productDetails) {
				if (listProductIdsApplyVoucher.contains(productDetail.getId())) {
					continue;
				}
				Integer productId = productDetail.getProductId();
				BigDecimal price = productDetail.getPrice();
				// Product product =
				// productMap.get(Integer.valueOf(productDetail.getProductId()));

				if (voucherApplication.getProductId().equals(productId)) {
					CartDetail cartDetailFromMap = cartDetailMap.get(Integer.valueOf(productDetail.getId()));
					amountVoucher = amountVoucher.add(calculateTotalAmountApplyVoucher(price, voucher));
					listProductIdsApplyVoucher.add(Integer.valueOf(productDetail.getId()));
				}
			}
		}
		if (wrapper.getTotalAmount().compareTo(amountVoucher) < 0
				|| amountVoucher.compareTo(voucher.getMaxDiscount()) >= 0) {
					if(voucher.getDiscountType() == 2 && wrapper.getTotalAmount().compareTo(amountVoucher) < 0) {
						amountVoucher = wrapper.getTotalAmount();
					}else{
						amountVoucher = voucher.getMaxDiscount();
					}
				}
		totalAmount = wrapper.getTotalAmount().subtract(amountVoucher);

		if (amountVoucher.equals(BigDecimal.ZERO)) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_IS_NOT_APPLY);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		response.setData(new VoucherApplyResponse(totalAmount, amountVoucher, new VoucherResponse(voucher)));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
