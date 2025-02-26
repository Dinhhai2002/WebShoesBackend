package com.shoes.webshoes.controller;

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

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.common.utils.StringErrorValue;
import com.shoes.webshoes.entity.CartDetail;
import com.shoes.webshoes.entity.ProductDetail;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDCartDetailRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.CartDetailResponse;
import com.shoes.webshoes.service.CartDetailService;
import com.shoes.webshoes.service.ProductDetailService;


@RestController
@RequestMapping("/api/v1/cart-detail")
public class CartDetailController  {
    @Autowired
    public CartDetailService cartDetailService;
    
    @Autowired
    public ProductDetailService productDetailService;

    @GetMapping("")
//	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<CartDetailResponse>>> getAll(
			@RequestParam(name = "cart_id", required = false, defaultValue = "-1") int cartId,
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<CartDetailResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<CartDetail> listCartDetail = cartDetailService.spGListCartDetail(cartId, keySearch,
				status, pagination);

		BaseListDataResponse<CartDetailResponse> listData = new BaseListDataResponse<>();

		listData.setList(new CartDetailResponse().mapToList(listCartDetail.getResult()));
		listData.setTotalRecord(listCartDetail.getTotalRecord());

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @GetMapping("/{id}")
	public ResponseEntity<BaseResponse<CartDetailResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<CartDetailResponse> response = new BaseResponse<>();
		CartDetail cartDetail = cartDetailService.findOne(id);

		if (cartDetail == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CART_DETAIL_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
        response.setData(new CartDetailResponse(cartDetail));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// @PostMapping("/{id}/change-status")
	// @PreAuthorize("hasAnyAuthority('ADMIN')")
	// public ResponseEntity<BaseResponse<CartDetailResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
	// 	BaseResponse<CartDetailResponse> response = new BaseResponse<>();
	// 	CartDetail cartDetail = cartDetailService.findOne(id);

	// 	if (cartDetail == null) {
	// 		response.setStatus(HttpStatus.BAD_REQUEST);
	// 		response.setMessageError(StringErrorValue.CART_DETAIL_NOT_FOUND);
	// 		return new ResponseEntity<>(response, HttpStatus.OK);
	// 	}

	// 	cartDetail.setStatus(cartDetail.getStatus() == 1 ? 0 : 1);

	// 	cartDetailService.update(cartDetail);
    //     response.setData(new CartDetailResponse(cartDetail));

	// 	return new ResponseEntity<>(response, HttpStatus.OK);
	// }

	@PostMapping("/create")
	public ResponseEntity<BaseResponse<CartDetailResponse>> create(
			@Valid @RequestBody CRUDCartDetailRequest wrapper) throws Exception {

		BaseResponse<CartDetailResponse> response = new BaseResponse<>();
		// CartDetail cartDetailCheck = cartDetailService.spGListCartDetail(wrapper.getCartId(), "", 0, new Pagination(0, 20))
		// 												.getResult().stream().findFirst().orElse(null);
		// if (cartDetailCheck != null) {
		// 	response.setStatus(HttpStatus.BAD_REQUEST);
		// 	response.setMessageError(StringErrorValue.CART_DETAIL_IS_EXIST);
		// 	return new ResponseEntity<>(response, HttpStatus.OK);
		// }

		CartDetail cartDetail = new CartDetail();
		cartDetail.setCartId(wrapper.getCartId());
		cartDetail.setProductDetailId(wrapper.getProductDetailId());
		cartDetail.setQuantity(wrapper.getQuantity());

		cartDetailService.create(cartDetail);
		response.setData(new CartDetailResponse(cartDetail));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update")
	public ResponseEntity<BaseResponse<CartDetailResponse>> update(@PathVariable("id") int id,
			@Valid @RequestBody CRUDCartDetailRequest wrapper) throws Exception {

		BaseResponse<CartDetailResponse> response = new BaseResponse<>();
		CartDetail cartDetail = cartDetailService.findOne(id);

		if (cartDetail == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CART_DETAIL_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		ProductDetail productDetail = productDetailService.findOne(cartDetail.getProductDetailId());
		if(productDetail == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_DETAIL_NOT_IN_CART);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		if(wrapper.getQuantity() > productDetail.getStock()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_DETAIL_NOT_INSUFFICIENT_QUANTITY);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		cartDetail.setQuantity(wrapper.getQuantity());
		cartDetailService.update(cartDetail);

		response.setData(new CartDetailResponse(cartDetail));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
