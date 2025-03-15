package com.shoes.webshoes.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.Brand;

import lombok.Data;

@Data
public class BrandResponse {
    private int id;
    private String name;
    
    @JsonProperty("image_url")
    private String imageUrl;
    private int status;

    public BrandResponse() {
    }

    public BrandResponse(Brand entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.imageUrl = entity.getImageUrl();
        this.status = entity.getStatus();
    }

    public List<BrandResponse> mapToList(List<Brand> entities) {
        return entities.stream()
            .map(x -> new BrandResponse(x))
            .collect(Collectors.toList());
    }
}
