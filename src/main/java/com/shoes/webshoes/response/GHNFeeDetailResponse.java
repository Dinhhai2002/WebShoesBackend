package com.shoes.webshoes.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GHNFeeDetailResponse {
    private Integer code;
    private String message;
    private GHNFeeResponse data;
} 