package com.shoes.webshoes.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.Banner;

import lombok.Data;

@Data
public class BannerResponse {
    private int id;
    private String url;
    private int status;
    
    @JsonProperty("is_deleted")
    private int isDeleted;

    public BannerResponse() {
    }

    public BannerResponse(Banner entity) {
        this.id = entity.getId();
        this.url = entity.getUrl();
        this.status = entity.getStatus();
        this.isDeleted = entity.getIsDeleted();
    }

    public List<BannerResponse> mapToList(List<Banner> entities) {
        return entities.stream()
            .map(x -> new BannerResponse(x))
            .collect(Collectors.toList());
    }
}
