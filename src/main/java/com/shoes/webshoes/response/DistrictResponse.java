package com.shoes.webshoes.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.Districts;

import lombok.Data;

@Data
public class DistrictResponse {
	private int id;

	@JsonProperty("city_id")
	private int cityId;

	private String name;

	private String code;

	private int status;

	public DistrictResponse() {

	}

	public DistrictResponse(Districts entity) {
		this.id = entity.getId();
		this.cityId = entity.getCityId();
		this.name = entity.getName();
		this.code = entity.getCode();
		this.status = entity.getStatus();
	}

	public List<DistrictResponse> mapToList(List<Districts> entities) {
		return entities.stream().map(x -> new DistrictResponse(x)).collect(Collectors.toList());
	}

}
