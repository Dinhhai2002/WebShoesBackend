package com.shoes.webshoes.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.ExchangeRequestDetail;
import lombok.Data;
import java.util.List;

@Data
public class ExchangeRequestDetailResponse {
    private Integer id;
    @JsonProperty("exchange_request_id")
    private Integer exchangeRequestId;
    @JsonProperty("old_product_id")
    private Integer oldProductId;
    @JsonProperty("old_product_detail_id")
    private Integer oldProductDetailId;
    @JsonProperty("new_product_id")
    private Integer newProductId;
    @JsonProperty("new_product_detail_id")
    private Integer newProductDetailId;
    private Integer quantity;
    @JsonProperty("exchange_reason")
    private String exchangeReason;
    @JsonProperty("condition_description")
    private String conditionDescription;
    @JsonProperty("images")
    private List<String> images;
    
    public ExchangeRequestDetailResponse() {}
    
    public ExchangeRequestDetailResponse(ExchangeRequestDetail entity) {
        this.id = entity.getId();
        this.exchangeRequestId = entity.getExchangeRequestId();
        this.oldProductId = entity.getOldProductId();
        this.oldProductDetailId = entity.getOldProductDetailId();
        this.newProductId = entity.getNewProductId();
        this.newProductDetailId = entity.getNewProductDetailId();
        this.quantity = entity.getQuantity();
        this.exchangeReason = entity.getExchangeReason();
        this.conditionDescription = entity.getConditionDescription();
        
        // Parse images JSON string to List<String>
        if (entity.getImages() != null && !entity.getImages().isEmpty()) {
            this.images = new com.google.gson.Gson().fromJson(
                entity.getImages(), 
                new com.google.gson.reflect.TypeToken<List<String>>(){}.getType()
            );
        }
    }
} 