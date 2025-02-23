package com.shoes.webshoes.request;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data

public class CRUDProductRequest {
    @NotEmpty(message = "name không được phép để trống")
	@Length(max = 255, message = "Không được phép lớn hơn 255 kí tự")
	private String name;

	@Min(value = 1 , message ="brand not null")
	@JsonProperty("brand_id")
	private int brandId;

	@Min(value = 1 , message ="category not null")
	@JsonProperty("category_id")
	private int categoryId;

	@NotEmpty(message = "description không được phép để trống")
	@Length(max = 255, message = "Không được phép lớn hơn 255 kí tự")
	private String description;

	@Min(value = 0 , message ="giá sản phẩm not null")
	private BigDecimal price;

}

