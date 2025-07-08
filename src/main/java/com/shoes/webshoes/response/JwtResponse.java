package com.shoes.webshoes.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class JwtResponse {

	@JsonProperty("token")
	private String jwttoken;

	public JwtResponse() {
	}

	public JwtResponse(String jwttoken) {
		this.jwttoken = jwttoken;
	}

}
