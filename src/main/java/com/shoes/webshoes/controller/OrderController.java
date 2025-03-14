package com.shoes.webshoes.controller;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
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

import com.shoes.webshoes.common.enums.PaymentStatusEnum;
import com.shoes.webshoes.common.enums.StatusOrderEnum;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.common.utils.StringErrorValue;
import com.shoes.webshoes.entity.Cart;
import com.shoes.webshoes.entity.CartDetail;
import com.shoes.webshoes.entity.Order;
import com.shoes.webshoes.entity.OrderDetail;
import com.shoes.webshoes.entity.ProductDetail;
import com.shoes.webshoes.entity.Users;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDOrderRequest;
import com.shoes.webshoes.request.ChangeStatusOrderRequest;
import com.shoes.webshoes.request.ChangePaymentStatusRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.OrderDetailResponse;
import com.shoes.webshoes.response.OrderResponse;
import com.shoes.webshoes.security.ConfigVnpay;
import com.shoes.webshoes.service.CartService;
import com.shoes.webshoes.service.CartDetailService;
import com.shoes.webshoes.service.OrderDetailService;
import com.shoes.webshoes.service.OrderService;
import com.shoes.webshoes.service.ProductDetailService;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController extends BaseController {
	@Autowired
	public OrderService orderService;

	@Autowired
	public OrderDetailService orderDetailService;

	@Autowired
	public CartService cartService;

	@Autowired
	public CartDetailService cartDetailService;

	@Autowired
	public ProductDetailService productDetailService;

	@GetMapping("")
	// @PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<OrderResponse>>> getAll(
			@RequestParam(name = "user_id", required = false, defaultValue = "-1") int userId,
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<OrderResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<Order> listOrder = orderService.spGListOrder(userId, keySearch,
				status, pagination);

		BaseListDataResponse<OrderResponse> listData = new BaseListDataResponse<>();

		listData.setList(new OrderResponse().mapToList(listOrder.getResult()));
		listData.setTotalRecord(listOrder.getTotalRecord());

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BaseResponse<OrderResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<OrderResponse> response = new BaseResponse<>();
		Order order = orderService.findOne(id);

		if (order == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.ORDER_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		List<OrderDetail> orderDetails = orderDetailService
				.spGListOrderDetail(order.getId(), "", 1, new Pagination(0, 20)).getResult();
		List<OrderDetailResponse> orderDetailsResponse = new OrderDetailResponse().mapToList(orderDetails);

		response.setData(new OrderResponse(order, orderDetailsResponse));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<OrderResponse>> changeStatus(@PathVariable("id") int id,
			@Valid @RequestBody ChangeStatusOrderRequest wrapper) throws Exception {
		BaseResponse<OrderResponse> response = new BaseResponse<>();
		Order order = orderService.findOne(id);

		if (order == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.ORDER_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		order.setStatus(wrapper.getStatus());

		orderService.update(order);
		response.setData(new OrderResponse(order));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<BaseResponse> create(@Valid @RequestBody CRUDOrderRequest wrapper) throws Exception {
		BaseResponse response = new BaseResponse<>();
		Users users = this.getUser();

		// Lấy cart và cart details của user
		StoreProcedureListResult<Cart> userCart = cartService.spGListCart(users.getId(), "", 1, new Pagination(0, 1));
		if (userCart.getResult().isEmpty()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CART_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		Cart cart = userCart.getResult().get(0);
		StoreProcedureListResult<CartDetail> cartDetails = cartDetailService.spGListCartDetail(
			cart.getId(), -1, "", 1, new Pagination(0, 100)
		);

		if (cartDetails.getResult().isEmpty()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CART_DETAIL_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// Tạo order
		Order order = new Order();
		order.setUserId(users.getId());
		order.setPrice(wrapper.getPrice());
		order.setDiscountAmount(wrapper.getDiscountAmount());
		order.setTotalPrice(wrapper.getTotalPrice());
		order.setPaymentMethod(wrapper.getPaymentMethod());
		order.setPaymentStatus(PaymentStatusEnum.PENDING.getValue());
		order.setStatus(StatusOrderEnum.PENDING.getValue());
		
		orderService.create(order);

		// Tạo order details từ cart details
		// Lấy danh sách productDetailIds từ cartDetails
		List<Integer> productDetailIds = cartDetails.getResult().stream()
			.map(CartDetail::getProductDetailId)
			.collect(Collectors.toList());

		// Lấy thông tin các ProductDetail
		List<ProductDetail> productDetails = productDetailService.findByIds(productDetailIds);
		// Tạo map để dễ dàng truy xuất ProductDetail theo ID
		Map<Integer, ProductDetail> productDetailMap = productDetails.stream()
			.collect(Collectors.toMap(ProductDetail::getId, pd -> pd));

		for (CartDetail cartDetail : cartDetails.getResult()) {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setOrderId(order.getId());
			orderDetail.setProductDetailId(cartDetail.getProductDetailId());
			orderDetail.setQuantity(cartDetail.getQuantity());
			
			// Lấy giá từ ProductDetail
			ProductDetail productDetail = productDetailMap.get(cartDetail.getProductDetailId());
			if (productDetail != null) {
				orderDetail.setPrice(productDetail.getPrice());
				// Tính tổng tiền cho từng orderDetail
				BigDecimal totalPrice = productDetail.getPrice().multiply(new BigDecimal(cartDetail.getQuantity()));
				orderDetail.setTotalPrice(totalPrice);
			}
			
			orderDetail.setStatus(1); // Assuming 1 is active status
			
			orderDetailService.create(orderDetail);
			
			// Xóa cart detail sau khi đã chuyển sang order detail
			cartDetailService.delete(cartDetail.getId());
		}

		String paymentUrl = generateVnPayUrl(wrapper.getTotalPrice(), String.valueOf(order.getId()));
		response.setData(paymentUrl);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update")
	public ResponseEntity<BaseResponse<OrderResponse>> update(@PathVariable("id") int id,
			@Valid @RequestBody CRUDOrderRequest wrapper) throws Exception {

		BaseResponse<OrderResponse> response = new BaseResponse<>();
		Order order = orderService.findOne(id);

		if (order == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.ORDER_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// if (!order.getName().equals(wrapper.getName())
		// && orderService.findByName(wrapper.getName()) != null) {
		// response.setStatus(HttpStatus.BAD_REQUEST);
		// response.setMessageError(StringErrorValue.ORDER_IS_EXIST);
		// return new ResponseEntity<>(response, HttpStatus.OK);

		// }
		// order.setName(wrapper.getName());
		orderService.update(order);

		response.setData(new OrderResponse(order));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/payment-confirm/{id}")
	public ResponseEntity<BaseResponse> getPaymentUrl(@PathVariable("id") int id) throws Exception {
		BaseResponse response = new BaseResponse<>();
		Users users = this.getUser();

		// Kiểm tra đơn hàng tồn tại và thuộc về user hiện tại
		Order order = orderService.findOne(id);
		if (order == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.ORDER_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// Kiểm tra đơn hàng có phải của user hiện tại không
		if (order.getUserId() != users.getId()) {
			response.setStatus(HttpStatus.FORBIDDEN);
			response.setMessageError("You don't have permission to access this order");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// Kiểm tra trạng thái thanh toán của đơn hàng
		if (order.getPaymentStatus() != PaymentStatusEnum.PENDING.getValue()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Order has been paid or cancelled");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		String paymentUrl = generateVnPayUrl(order.getTotalPrice(), String.valueOf(order.getId()));
		response.setData(paymentUrl);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-payment-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<OrderResponse>> changePaymentStatus(
			@PathVariable("id") int id,
			@Valid @RequestBody ChangePaymentStatusRequest wrapper) throws Exception {
		BaseResponse<OrderResponse> response = new BaseResponse<>();
		Order order = orderService.findOne(id);

		if (order == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.ORDER_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// Kiểm tra trạng thái thanh toán hợp lệ
		if (!PaymentStatusEnum.isValidStatus(wrapper.getPaymentStatus())) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Invalid payment status");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// Cập nhật trạng thái thanh toán
		order.setPaymentStatus(wrapper.getPaymentStatus());

		// Nếu đã thanh toán thành công, cập nhật trạng thái đơn hàng sang PROCESSING
		if (wrapper.getPaymentStatus() == PaymentStatusEnum.PAID.getValue()) {
			order.setStatus(StatusOrderEnum.PROCESSING.getValue());
		}
		// Nếu thanh toán thất bại hoặc bị hủy, cập nhật trạng thái đơn hàng sang CANCELLED
		else if (wrapper.getPaymentStatus() == PaymentStatusEnum.FAILED.getValue()) {
			order.setStatus(StatusOrderEnum.CANCELLED.getValue());
		}

		orderService.update(order);
		
		// Lấy thông tin chi tiết đơn hàng để trả về
		List<OrderDetail> orderDetails = orderDetailService
				.spGListOrderDetail(order.getId(), "", 1, new Pagination(0, 20))
				.getResult();
		List<OrderDetailResponse> orderDetailsResponse = new OrderDetailResponse().mapToList(orderDetails);

		response.setData(new OrderResponse(order, orderDetailsResponse));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private String generateVnPayUrl(BigDecimal amount, String orderId) throws Exception {
		String vnp_TxnRef = ConfigVnpay.getRandomNumber(8);
		String vnp_TmnCode = applicationProperties.getVnpTmnCode();
		String ReturnUrl = applicationProperties.getBaseUrlFe() + "/payment-success";

		Map<String, String> vnp_Params = new Hashtable<>();
		vnp_Params.put("vnp_Version", ConfigVnpay.vnp_Version);
		vnp_Params.put("vnp_Command", ConfigVnpay.vnp_Command);
		vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
		vnp_Params.put("vnp_Amount", String.valueOf(amount.longValue() * 100));
		vnp_Params.put("vnp_CurrCode", "VND");
		vnp_Params.put("vnp_BankCode", "NCB");
		vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
		vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + orderId);
		vnp_Params.put("vnp_OrderType", "other");
		vnp_Params.put("vnp_Locale", "vn");
		vnp_Params.put("vnp_ReturnUrl", ReturnUrl);
		vnp_Params.put("vnp_IpAddr", "13.160.92.202");

		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));
		String vnp_CreateDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

		cld.add(Calendar.MINUTE, 15);
		String vnp_ExpireDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

		List fieldNames = new ArrayList(vnp_Params.keySet());
		Collections.sort(fieldNames);
		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Iterator itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = (String) vnp_Params.get(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				// Build hash data
				hashData.append(fieldName);
				hashData.append('=');
				hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				// Build query
				query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
				query.append('=');
				query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				if (itr.hasNext()) {
					query.append('&');
					hashData.append('&');
				}
			}
		}
		String queryUrl = query.toString();
		String vnp_SecureHash = ConfigVnpay.hmacSHA512(applicationProperties.getVnpaySecretKey(), hashData.toString());
		queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
		return applicationProperties.getVnpPayUrl() + "?" + queryUrl;
	}

}
