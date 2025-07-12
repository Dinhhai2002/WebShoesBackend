package com.shoes.webshoes.response;

import lombok.Data;
import java.util.List;

@Data
public class GHNDistrictListResponse {
    private Integer code;
    private String message;
    private List<GHNDistrictResponse> data;
} 