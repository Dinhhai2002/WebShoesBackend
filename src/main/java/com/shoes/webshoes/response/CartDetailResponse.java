package com.shoes.webshoes.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.CartDetail;

import lombok.Data;

@Data
public class CartDetailResponse {
    private int id;

	@JsonProperty("cart_id")
	private int cartId;

	@JsonProperty("product_detail_id")
	private int productDetailId;

	private int quantity;
	
	@JsonProperty("product_detail")
	private ProductDetailResponse productDetailResponse;

	public CartDetailResponse() {

	}

	public CartDetailResponse(CartDetail entity) {
		this.id = entity.getId();
		this.cartId = entity.getCartId();
		this.productDetailId = entity.getProductDetailId();
		this.quantity = entity.getQuantity();
	}
	
	public CartDetailResponse(CartDetail entity, ProductDetailResponse productDetailResponse) {
		this.id = entity.getId();
		this.cartId = entity.getCartId();
		this.productDetailId = entity.getProductDetailId();
		this.quantity = entity.getQuantity();
		this.productDetailResponse = productDetailResponse;
	}

	public List<CartDetailResponse> mapToList(List<CartDetail> entities) {
		return entities.stream().map(x -> new CartDetailResponse(x)).collect(Collectors.toList());
	}
}
