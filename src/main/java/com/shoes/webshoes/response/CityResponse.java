package com.shoes.webshoes.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.Cities;

import lombok.Data;

@Data
public class CityResponse {
	private int id;

	@JsonProperty("country_id")
	private int countryId;

	private String name;

	private String code;

	private int status;

	public CityResponse() {

	}

	public CityResponse(Cities entity) {
		this.id = entity.getId();
		this.countryId = entity.getCountryId();
		this.name = entity.getName();
		this.code = entity.getCode();
		this.status = entity.getStatus();
	}

	public List<CityResponse> mapToList(List<Cities> entities) {
		return entities.stream().map(x -> new CityResponse(x)).collect(Collectors.toList());
	}

}
