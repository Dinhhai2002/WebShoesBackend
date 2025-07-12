package com.shoes.webshoes.response;

import lombok.Data;
import java.util.List;

@Data
public class GHNWardListResponse {
    private Integer code;
    private String message;
    private List<GHNWardResponse> data;
} 