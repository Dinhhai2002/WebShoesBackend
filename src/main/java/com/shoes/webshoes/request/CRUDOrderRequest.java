package com.shoes.webshoes.request;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

	@NotNull(message = "voucher_id not null")
	@JsonProperty("voucher_id")
	private int voucherId;

	@NotNull(message = "Địa chỉ giao hàng không được để trống")
	@JsonProperty("address_id")
	private Integer addressId;

	@Min(value = 0 , message ="amount shipping not null")
	@JsonProperty("amount_shipping")
	private BigDecimal amountShipping;
}
