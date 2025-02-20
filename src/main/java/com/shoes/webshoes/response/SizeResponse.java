package com.shoes.webshoes.response;

import java.util.List;
import java.util.stream.Collectors;

import com.shoes.webshoes.entity.Size;

import lombok.Data;

@Data
public class SizeResponse {
    private int id;

    private String name;

    private int status;

    public SizeResponse() {

    }

    public SizeResponse(Size entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.status = entity.getStatus();
    }

    public List<SizeResponse> mapToList(List<Size> entities) {
        return entities.stream().map(x -> new SizeResponse(x)).collect(Collectors.toList());
    }
}
