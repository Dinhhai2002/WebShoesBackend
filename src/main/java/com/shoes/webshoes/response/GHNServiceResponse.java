package com.shoes.webshoes.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GHNServiceResponse {
    @JsonProperty("service_id")
    private Integer service_id;
    
    @JsonProperty("short_name")
    private String short_name;
    
    @JsonProperty("service_type_id")
    private Integer service_type_id;
} 