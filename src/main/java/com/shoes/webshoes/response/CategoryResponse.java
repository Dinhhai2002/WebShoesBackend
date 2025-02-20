package com.shoes.webshoes.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.Category;

import lombok.Data;

@Data
public class CategoryResponse {
    private int id;

	private String name;

    @JsonProperty("parent_id")
	private int parentId;

	private int status;

	public CategoryResponse() {

	}

	public CategoryResponse(Category entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.parentId = entity.getParentId();
		this.status = entity.getStatus();
	}

	public List<CategoryResponse> mapToList(List<Category> entities) {
		return entities.stream().map(x -> new CategoryResponse(x)).collect(Collectors.toList());
	}
}
