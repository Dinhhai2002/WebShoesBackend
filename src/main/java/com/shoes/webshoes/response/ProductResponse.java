package com.shoes.webshoes.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.Product;

import lombok.Data;

@Data
public class ProductResponse {
    private int id;

	private String name;

	@JsonProperty("brand_id")
	private int brandId;

	@JsonProperty("category_id")
	private int categoryId;

	private String description;

	private BigDecimal price;

	@JsonProperty("average_rating")
	private BigDecimal averageRating;

	@JsonProperty("image_url")
	private String imageUrl; 


	private int status;

	public ProductResponse() {

	}

	public ProductResponse(Product entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.brandId = entity.getBrandId();
		this.categoryId = entity.getCategoryId();
		this.description = entity.getDescription();
		this.price = entity.getPrice();
		this.averageRating = entity.getAverageRating();
		this.imageUrl = entity.getImageUrl();
		this.status = entity.getStatus();
	}

	public List<ProductResponse> mapToList(List<Product> entities) {
		return entities.stream().map(x -> new ProductResponse(x)).collect(Collectors.toList());
	}
}
