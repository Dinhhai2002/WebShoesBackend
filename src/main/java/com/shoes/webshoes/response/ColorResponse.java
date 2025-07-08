package com.shoes.webshoes.response;

import java.util.List;
import java.util.stream.Collectors;

import com.shoes.webshoes.entity.Color;

import lombok.Data;

@Data
public class ColorResponse {
    private int id;

	private String name;

	private int status;

	public ColorResponse() {

	}

	public ColorResponse(Color entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.status = entity.getStatus();
	}

	public List<ColorResponse> mapToList(List<Color> entities) {
		return entities.stream().map(x -> new ColorResponse(x)).collect(Collectors.toList());
	}
}
