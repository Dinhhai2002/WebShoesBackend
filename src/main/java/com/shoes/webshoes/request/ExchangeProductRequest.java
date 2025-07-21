package com.shoes.webshoes.request;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
@Data
public class ExchangeProductRequest {
    @JsonProperty("old_product_id") 
    private Integer oldProductId; // Product ID của sản phẩm cũ
    @JsonProperty("old_product_detail_id")
    private Integer oldProductDetailId; // Sản phẩm cũ
    @JsonProperty("new_product_id")
    private Integer newProductId; // Product ID của sản phẩm mới được admin chọn
    @JsonProperty("new_product_detail_id")
    private Integer newProductDetailId; // Sản phẩm mới được admin chọn
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("exchange_reason")
    private String exchangeReason;
} 