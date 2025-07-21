package com.shoes.webshoes.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoes.webshoes.entity.ReturnRequestDetail;
import lombok.Data;

import java.util.List;

@Data
public class ReturnRequestDetailResponse {
    private Integer id;
    @JsonProperty("return_request_id")
    private Integer returnRequestId;
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
    
    public ReturnRequestDetailResponse() {}
    
    public ReturnRequestDetailResponse(ReturnRequestDetail entity) {
        this.id = entity.getId();
        this.returnRequestId = entity.getReturnRequestId();
        this.productId = entity.getProductId();
        this.productDetailId = entity.getProductDetailId();
        this.price = entity.getPrice();
        this.quantity = entity.getQuantity();
        this.returnReason = entity.getReturnReason();
        this.conditionDescription = entity.getConditionDescription();
        // Parse images JSON string to List<String>
        if (entity.getImages() != null && !entity.getImages().isEmpty()) {
            this.images = new Gson().fromJson(entity.getImages(), new TypeToken<List<String>>(){}.getType());
        }
    }
} 