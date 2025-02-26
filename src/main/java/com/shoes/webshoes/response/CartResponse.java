package com.shoes.webshoes.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.Cart;

import lombok.Data;

@Data
public class CartResponse {
    private int id;

	@JsonProperty("user_id")
	private int userId;

	private int status;
	
	@JsonProperty("cart_detail")
	private List<CartDetailResponse> cartDetailResponses;

	public CartResponse() {

	}

	public CartResponse(Cart entity) {
		this.id = entity.getId();
		this.userId = entity.getUserId();
		this.status = entity.getStatus();
	}
	public CartResponse(Cart entity, List<CartDetailResponse> cartDetailResponses) {
		this.id = entity.getId();
		this.userId = entity.getUserId();
		this.status = entity.getStatus();
		this.cartDetailResponses = cartDetailResponses;
	}

	public List<CartResponse> mapToList(List<Cart> entities) {
		return entities.stream().map(x -> new CartResponse(x)).collect(Collectors.toList());
	}
}
