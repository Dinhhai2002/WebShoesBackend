package com.shoes.webshoes.request;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CRUDProductDetailRequest {
	@NotEmpty(message = "name không được phép để trống")
	@Length(max = 255, message = "Không được phép lớn hơn 255 kí tự")
	private String name;

	@Min(value = 1 , message ="productId not null")
	@JsonProperty("product_id")
	private int productId;

	@Min(value = 1 , message ="colorId not null")
	@JsonProperty("color_id")
	private int colorId;

	@Min(value = 1 , message ="sizeId not null")
	@JsonProperty("size_id")
	private int sizeId;

	@Min(value = 1 , message ="materialId not null")
	@JsonProperty("material_id")
	private int materialId;

	@Min(value = 1, message = "brandId not null")
	@JsonProperty("brand_id")
	private int brandId;

	@Min(value = 1, message = "categoryId not null")
	@JsonProperty("category_id")
	private int categoryId;

	@Min(value = 0 , message ="stock not null")
	private int stock;

	@Min(value = 0 , message ="giá sản phẩm not null")
	private BigDecimal price;

}

