package com.shoes.webshoes.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GHNDistrictResponse {
    @JsonProperty("DistrictID")
    private Integer DistrictID;
    
    @JsonProperty("ProvinceID")
    private Integer ProvinceID;
    
    @JsonProperty("DistrictName")
    private String DistrictName;
    
    @JsonProperty("Code")
    private String Code;
    
    @JsonProperty("Type")
    private Integer Type;
    
    @JsonProperty("SupportType")
    private Integer SupportType;
} 