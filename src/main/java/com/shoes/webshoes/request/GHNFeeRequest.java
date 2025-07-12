package com.shoes.webshoes.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GHNFeeRequest {
    @JsonProperty("service_id")
    private Integer service_id;

    @JsonProperty("insurance_value")
    private Integer insurance_value;

    @JsonProperty("from_district_id")
    private Integer from_district_id;

    @JsonProperty("to_district_id")
    private Integer to_district_id;

    @JsonProperty("from_ward_code")
    private String from_ward_code;
    
    @JsonProperty("to_ward_code")
    private String to_ward_code;

    private Integer height;
    private Integer length;
    private Integer weight;
    private Integer width;
} 