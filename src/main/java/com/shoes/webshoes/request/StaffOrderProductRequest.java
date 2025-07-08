package com.shoes.webshoes.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class StaffOrderProductRequest {
	@NotNull(message = "ID chi tiết sản phẩm không được để trống")
	@JsonProperty("product_detail_id")
	private Integer productDetailId;

	@Min(value = 1, message = "Số lượng phải lớn hơn 0")
	private int quantity;
}
