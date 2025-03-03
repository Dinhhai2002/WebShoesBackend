package com.shoes.webshoes.request;

import java.math.BigDecimal;

import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data

public class CRUDOrderRequest {

	@Min(value = 0 , message ="tổng tiền đơn hàng not null")
	private BigDecimal price;

	@Min(value = 0 , message ="giảm giá đơn hàng not null")
	@JsonProperty("discount_amount")
	private BigDecimal discountAmount;

	@Min(value = 0 , message ="tổng giá trị cuối cùng của đơn hàng not null")
	@JsonProperty("total_price")
	private BigDecimal totalPrice;

	@Min(value = 0 , message ="tổng giá trị cuối cùng của đơn hàng not null")
	@JsonProperty("payment_method")
	private int paymentMethod;

}

