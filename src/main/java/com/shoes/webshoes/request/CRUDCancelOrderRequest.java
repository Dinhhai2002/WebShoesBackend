package com.shoes.webshoes.request;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class CRUDCancelOrderRequest {
    @JsonProperty("order_id")
    private Integer orderId;
    
    @JsonProperty("user_id")
    private Integer userId;
    
    @JsonProperty("cancel_reason")
    private String cancelReason;
} 