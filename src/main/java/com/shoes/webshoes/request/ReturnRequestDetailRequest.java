package com.shoes.webshoes.request;

import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ReturnRequestDetailRequest {
    @JsonProperty("product_id")
    private Integer productId;
    @JsonProperty("product_detail_id")
    private Integer productDetailId;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("return_reason")
    private String returnReason;
    @JsonProperty("condition_description")
    private String conditionDescription;
    @JsonProperty("images")
    private List<String> images;
} 