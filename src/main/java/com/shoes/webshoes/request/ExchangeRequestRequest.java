package com.shoes.webshoes.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;


@Data
public class ExchangeRequestRequest {
    @JsonProperty("return_request_id")
    private Integer returnRequestId;
    @JsonProperty("exchange_reason")
    private String exchangeReason;
    @JsonProperty("price_difference")
    private BigDecimal priceDifference;
    @JsonProperty("details")
    private List<ExchangeRequestDetailRequest> details; // Chỉ có thông tin sản phẩm cũ và lý do đổi
} 