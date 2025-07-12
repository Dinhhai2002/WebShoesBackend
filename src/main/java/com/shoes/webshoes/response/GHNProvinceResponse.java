package com.shoes.webshoes.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GHNProvinceResponse {
    @JsonProperty("ProvinceID")
    private Integer ProvinceID;
    
    @JsonProperty("ProvinceName")
    private String ProvinceName;
    
    @JsonProperty("Code")
    private String Code;
} 