package com.shoes.webshoes.request;

import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ReturnRequestRequest {
    @JsonProperty("order_id")
    private Integer orderId;
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("return_reason")
    private String returnReason;
    @JsonProperty("return_type")
    private String returnType;
    @JsonProperty("details")
    private List<ReturnRequestDetailRequest> details;
}   