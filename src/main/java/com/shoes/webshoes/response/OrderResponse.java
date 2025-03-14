package com.shoes.webshoes.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.Order;

import lombok.Data;

@Data
public class OrderResponse {
    private int id;

	@JsonProperty("user_id")
	private int userId;

	@JsonProperty("voucher_id")
	private Integer voucherId;

	private BigDecimal price;
	
	@JsonProperty("discount_amount")
	private BigDecimal discountAmount;

	@JsonProperty("total_price")
	private BigDecimal totalPrice;

	@JsonProperty("payment_method")
	private int paymentMethod;

	@JsonProperty("payment_status")
	private int paymentStatus;

	private int status;
	
	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("order_detail")
	private List<OrderDetailResponse> orderDetailResponse;



	public OrderResponse() {

	}

	public OrderResponse(Order entity) {
		this.id = entity.getId();
		this.userId = entity.getUserId();
		this.voucherId = entity.getVoucherId();
		this.price = entity.getPrice();
		this.discountAmount = entity.getDiscountAmount();
		this.totalPrice = entity.getTotalPrice();
		this.paymentMethod = entity.getPaymentMethod();
		this.paymentStatus = entity.getPaymentStatus();
		this.status = entity.getStatus();
		this.createdAt = entity.getDatetimeFormatVN(entity.getCreatedAt());
	}

	public OrderResponse(Order entity, List<OrderDetailResponse> orderDetailResponse) {
		this.id = entity.getId();
		this.userId = entity.getUserId();
		this.voucherId = entity.getVoucherId();
		this.price = entity.getPrice();
		this.discountAmount = entity.getDiscountAmount();
		this.totalPrice = entity.getTotalPrice();
		this.paymentMethod = entity.getPaymentMethod();
		this.paymentStatus = entity.getPaymentStatus();
		this.status = entity.getStatus();
		this.createdAt = entity.getDatetimeFormatVN(entity.getCreatedAt());
		this.orderDetailResponse = orderDetailResponse;
	}

	public List<OrderResponse> mapToList(List<Order> entities) {
		return entities.stream().map(x -> new OrderResponse(x)).collect(Collectors.toList());
	}
}
