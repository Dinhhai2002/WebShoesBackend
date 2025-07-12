package com.shoes.webshoes.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GHNWardResponse {
    @JsonProperty("WardCode")
    private Integer WardCode;
    
    @JsonProperty("DistrictID")
    private Integer DistrictID;
    
    @JsonProperty("WardName")
    private String WardName;
} 