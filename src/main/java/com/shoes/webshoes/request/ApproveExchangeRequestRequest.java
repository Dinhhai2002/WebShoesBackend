package com.shoes.webshoes.request;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ApproveExchangeRequestRequest {
    @JsonProperty("admin_notes")
    private String adminNotes;
    @JsonProperty("price_difference")
    private BigDecimal priceDifference; // Chênh lệch giá
    @JsonProperty("exchange_products")
    private List<ExchangeProductRequest> exchangeProducts; // Danh sách sản phẩm mới được chọn
}
