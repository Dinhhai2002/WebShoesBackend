package com.shoes.webshoes.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.Image;

import lombok.Data;

@Data
public class ImageResponse {
	private int id;

	private String url;

	@JsonProperty("user_id")
	private int userId;

	public ImageResponse() {

	}

	public ImageResponse(Image entity) {
		this.id = entity.getId();
		this.url = entity.getUrl();
		this.userId = entity.getUserId();
	}
}
