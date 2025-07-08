package com.shoes.webshoes.response;

import java.util.List;
import java.util.stream.Collectors;

import com.shoes.webshoes.entity.Materials;

import lombok.Data;

@Data
public class MaterialsResponse {
    private int id;

	private String name;

	private int status;

	public MaterialsResponse() {

	}

	public MaterialsResponse(Materials entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.status = entity.getStatus();
	}

	public List<MaterialsResponse> mapToList(List<Materials> entities) {
		return entities.stream().map(x -> new MaterialsResponse(x)).collect(Collectors.toList());
	}
}
