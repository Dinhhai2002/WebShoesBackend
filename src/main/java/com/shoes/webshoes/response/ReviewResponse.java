package com.shoes.webshoes.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.Review;
import com.shoes.webshoes.entity.Users;

import lombok.Data;

@Data
public class ReviewResponse {
	private int id;

	@JsonProperty("user_id")
	private int userId;
	
	@JsonProperty("product_id")
	private int productId;
	
	private int rating;
	private String comment;
	private int status;
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("created_at")
	private String createdAt;
	
	public ReviewResponse() {

	}
	
	public ReviewResponse(Review review) {
		this.id = review.getId();
		this.userId = review.getUserId();
		this.productId = review.getProductId();
		this.rating = review.getRating();
		this.comment = review.getComment();
		this.status = review.getStatus();
		this.createdAt = review.getDatetimeFormatVN(review.getCreatedAt());
	}

	public ReviewResponse(Review review, Users user) {
		this.id = review.getId();
		this.userId = review.getUserId();
		this.productId = review.getProductId();
		this.rating = review.getRating();
		this.comment = review.getComment();
		this.status = review.getStatus();
		this.createdAt = review.getDatetimeFormatVN(review.getCreatedAt());
		if (user != null) {
			this.username = user.getUserName();
		}
	}

	public List<ReviewResponse> mapToList(List<Review> reviews, List<Users> users) {
		return reviews.stream().map(review -> {
			Users user = users.stream()
				.filter(u -> u.getId() == review.getUserId())
				.findFirst()
				.orElse(null);
			return new ReviewResponse(review, user);
		}).collect(Collectors.toList());
	}
}
