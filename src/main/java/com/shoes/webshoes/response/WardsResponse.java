package com.shoes.webshoes.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.Wards;

import lombok.Data;

@Data
public class WardsResponse {
	private int id;

	@JsonProperty("district_id")
	private int districtId;

	private String name;

	private String code;

	private int status;

	public WardsResponse() {

	}

	public WardsResponse(Wards entity) {
		this.id = entity.getId();
		this.districtId = entity.getDistrictId();
		this.name = entity.getName();
		this.code = entity.getCode();
		this.status = entity.getStatus();
	}

	public List<WardsResponse> mapToList(List<Wards> entities) {
		return entities.stream().map(x -> new WardsResponse(x)).collect(Collectors.toList());
	}

}
