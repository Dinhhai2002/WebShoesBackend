package com.shoes.webshoes.request;

import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data

public class CRUDCartDetailRequest {

	@Min(value = 1 , message ="cartId not null")
	@JsonProperty("cart_id")
	private int cartId;

	@Min(value = 1 , message ="productDetailId not null")
	@JsonProperty("product_detail_id")
	private int productDetailId;

	@Min(value = 1 , message ="quantity not null")
	private int quantity;


}

