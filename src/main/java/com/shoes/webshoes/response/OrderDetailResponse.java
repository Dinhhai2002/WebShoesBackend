package com.shoes.webshoes.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.OrderDetail;

import lombok.Data;

@Data
public class OrderDetailResponse {
    private int id;

	@JsonProperty("order_id")
	private int orderId;

	@JsonProperty("product_detail_id")
	private int productDetailId;

	private int quantity;

	private BigDecimal price;
	
	@JsonProperty("total_price")
	private BigDecimal totalPrice;

	private int status;

	public OrderDetailResponse() {

	}

	public OrderDetailResponse(OrderDetail entity) {
		this.id = entity.getId();
		this.orderId = entity.getId();
		this.productDetailId = entity.getProductDetailId();
		this.quantity = entity.getQuantity();
		this.price = entity.getPrice();
		this.status = entity.getStatus();
	}

	public List<OrderDetailResponse> mapToList(List<OrderDetail> entities) {
		return entities.stream().map(x -> new OrderDetailResponse(x)).collect(Collectors.toList());
	}
}
