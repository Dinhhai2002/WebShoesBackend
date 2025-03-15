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
import com.shoes.webshoes.entity.OrderDetail;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDOrderDetailRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.OrderDetailResponse;
import com.shoes.webshoes.service.OrderDetailService;
import com.shoes.webshoes.service.ProductDetailService;
import com.shoes.webshoes.entity.ProductDetail;
import com.shoes.webshoes.response.ProductDetailResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/order-detail")
public class OrderDetailController  {
    @Autowired
    public OrderDetailService orderDetailService;

    @Autowired
    private ProductDetailService productDetailService;

    @GetMapping("")
//	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<OrderDetailResponse>>> getAll(
			@RequestParam(name = "order_id", required = false, defaultValue = "") int orderId,
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<OrderDetailResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<OrderDetail> listOrderDetail = orderDetailService.spGListOrderDetail(orderId, keySearch,
				status, pagination);

		BaseListDataResponse<OrderDetailResponse> listData = new BaseListDataResponse<>();

		listData.setList(new OrderDetailResponse().mapToList(listOrderDetail.getResult()));
		listData.setTotalRecord(listOrderDetail.getTotalRecord());

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @GetMapping("/{id}")
	public ResponseEntity<BaseResponse<OrderDetailResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<OrderDetailResponse> response = new BaseResponse<>();
		OrderDetail orderDetail = orderDetailService.findOne(id);

		if (orderDetail == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.ORDER_DETAIL_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
        response.setData(new OrderDetailResponse(orderDetail));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<OrderDetailResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
		BaseResponse<OrderDetailResponse> response = new BaseResponse<>();
		OrderDetail orderDetail = orderDetailService.findOne(id);

		if (orderDetail == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.ORDER_DETAIL_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		orderDetail.setStatus(orderDetail.getStatus() == 1 ? 0 : 1);

		orderDetailService.update(orderDetail);
        response.setData(new OrderDetailResponse(orderDetail));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<BaseResponse<OrderDetailResponse>> create(
			@Valid @RequestBody CRUDOrderDetailRequest wrapper) throws Exception {

		BaseResponse<OrderDetailResponse> response = new BaseResponse<>();
		OrderDetail orderDetailCheck = orderDetailService.findByName(wrapper.getName());

		if (orderDetailCheck != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.ORDER_DETAIL_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		OrderDetail orderDetail = new OrderDetail();
		// orderDetail.setName(wrapper.getName());
		orderDetail.setStatus(1);

		orderDetailService.create(orderDetail);
		response.setData(new OrderDetailResponse(orderDetail));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update")
	public ResponseEntity<BaseResponse<OrderDetailResponse>> update(@PathVariable("id") int id,
			@Valid @RequestBody CRUDOrderDetailRequest wrapper) throws Exception {

		BaseResponse<OrderDetailResponse> response = new BaseResponse<>();
		OrderDetail orderDetail = orderDetailService.findOne(id);

		if (orderDetail == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.ORDER_DETAIL_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// if (!orderDetail.getName().equals(wrapper.getName())
		// 		&& orderDetailService.findByName(wrapper.getName()) != null) {
		// 	response.setStatus(HttpStatus.BAD_REQUEST);
		// 	response.setMessageError(StringErrorValue.ORDER_DETAIL_IS_EXIST);
		// 	return new ResponseEntity<>(response, HttpStatus.OK);

		// }
		// orderDetail.setName(wrapper.getName());
		orderDetailService.update(orderDetail);

		response.setData(new OrderDetailResponse(orderDetail));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/by-order/{orderId}")
	public ResponseEntity<BaseResponse<BaseListDataResponse<OrderDetailResponse>>> getByOrderId(
			@PathVariable("orderId") int orderId) throws Exception {
		BaseResponse<BaseListDataResponse<OrderDetailResponse>> response = new BaseResponse<>();
		
		// Lấy danh sách OrderDetail theo orderId
		StoreProcedureListResult<OrderDetail> listOrderDetail = orderDetailService.spGListOrderDetail(
			orderId, "", 1, new Pagination(0, 100));

		// Lấy danh sách productDetailIds
		List<Integer> productDetailIds = listOrderDetail.getResult().stream()
			.map(OrderDetail::getProductDetailId)
			.collect(Collectors.toList());

		// Lấy thông tin ProductDetail
		List<ProductDetail> productDetails = productDetailService.findByIds(productDetailIds);
		Map<Integer, ProductDetailResponse> productDetailMap = productDetails.stream()
			.collect(Collectors.toMap(
				ProductDetail::getId,
				pd -> new ProductDetailResponse(pd)
			));

		// Tạo response với ProductDetail được map
		BaseListDataResponse<OrderDetailResponse> listData = new BaseListDataResponse<>();
		List<OrderDetailResponse> orderDetailResponses = listOrderDetail.getResult().stream()
			.map(orderDetail -> {
				ProductDetailResponse productDetailResponse = productDetailMap.get(orderDetail.getProductDetailId());
				return new OrderDetailResponse(orderDetail, productDetailResponse);
			})
			.collect(Collectors.toList());

		listData.setList(orderDetailResponses);
		listData.setTotalRecord(listOrderDetail.getTotalRecord());

		response.setData(listData);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
