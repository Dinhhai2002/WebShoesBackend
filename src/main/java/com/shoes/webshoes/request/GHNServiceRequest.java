package com.shoes.webshoes.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GHNServiceRequest {
    @JsonProperty("shop_id")
    private Integer shop_id;

    @JsonProperty("from_district")
    private Integer from_district;
    
    @JsonProperty("to_district")
    private Integer to_district;
} 