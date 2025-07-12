package com.shoes.webshoes.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GHNFeeResponse {
    private Integer total;
    
    @JsonProperty("service_fee")
    private Integer service_fee;
    
    @JsonProperty("insurance_fee")
    private Integer insurance_fee;
    
    @JsonProperty("pick_station_fee")
    private Integer pick_station_fee;
    
    @JsonProperty("coupon_value")
    private Integer coupon_value;
    
    @JsonProperty("r2s_fee")
    private Integer r2s_fee;
    
    @JsonProperty("document_return")
    private Integer document_return;
    
    @JsonProperty("double_check")
    private Integer double_check;
    
    @JsonProperty("cod_fee")
    private Integer cod_fee;
    
    @JsonProperty("pick_remote_areas_fee")
    private Integer pick_remote_areas_fee;
    
    @JsonProperty("deliver_remote_areas_fee")
    private Integer deliver_remote_areas_fee;
    
    @JsonProperty("cod_failed_fee")
    private Integer cod_failed_fee;
} 