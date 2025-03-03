package com.shoes.webshoes.request;

import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data

public class CRUDVoucherApplicationRequest {
    @Min(value = 1 , message ="voucherId not null")
	@JsonProperty("voucher_id")
	private int voucherId;

	@Min(value = 0 , message ="productId not null")
	@JsonProperty("product_id")
	private int productId;

	@Min(value = 0 , message ="productId not null")
	@JsonProperty("brand_id")
	private int brandId;

	@Min(value = 0 , message ="categoryId not null")
	@JsonProperty("category_id")
	private int categoryId;

}

