package com.shoes.webshoes.request;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class StaffOrderRequest {
	@Min(value = 0, message = "Tổng tiền đơn hàng không được âm")
	private BigDecimal price;

	@Min(value = 0, message = "Giảm giá đơn hàng không được âm")
	@JsonProperty("discount_amount")
	private BigDecimal discountAmount;

	@Min(value = 0, message = "Tổng giá trị đơn hàng không được âm")
	@JsonProperty("total_price")
	private BigDecimal totalPrice;

	@Min(value = 0, message = "Phương thức thanh toán không hợp lệ")
	@JsonProperty("payment_method")
	private int paymentMethod;

	@NotNull(message = "Địa chỉ giao hàng không được để trống")
	@JsonProperty("address_id")
	private Integer addressId;
	
	@JsonProperty("voucher_id")
	private Integer voucherId;

	@NotEmpty(message = "Danh sách sản phẩm không được để trống")
	@JsonProperty("products")
	private List<StaffOrderProductRequest> products;
	
    @JsonProperty("customer_phone")
    private String customerPhone;

	@JsonProperty("customer_name")
    private String customerName;


}
