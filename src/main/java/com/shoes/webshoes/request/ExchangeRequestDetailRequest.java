package com.shoes.webshoes.request;

import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ExchangeRequestDetailRequest {
    @JsonProperty("old_product_id")
    private Integer oldProductId; // Product ID của sản phẩm cũ
    @JsonProperty("old_product_detail_id")
    private Integer oldProductDetailId; // Sản phẩm cũ muốn đổi
    @JsonProperty("new_product_id")
    private Integer newProductId; // Product ID của sản phẩm mới (có thể null khi tạo ban đầu)
    @JsonProperty("new_product_detail_id")
    private Integer newProductDetailId; // Sản phẩm mới muốn đổi (có thể null khi tạo ban đầu)
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("exchange_reason")
    private String exchangeReason;
    @JsonProperty("condition_description")
    private String conditionDescription;
    @JsonProperty("images")
    private List<String> images;
} 