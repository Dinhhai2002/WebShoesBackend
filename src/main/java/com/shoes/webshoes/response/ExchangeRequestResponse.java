package com.shoes.webshoes.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.ExchangeRequest;
import com.shoes.webshoes.entity.ExchangeRequestDetail;

import lombok.Data;

@Data
public class ExchangeRequestResponse {
    private Integer id;
    @JsonProperty("return_request_id")
    private Integer returnRequestId;
    @JsonProperty("exchange_reason")
    private String exchangeReason;
    private String status;
    @JsonProperty("admin_notes")
    private String adminNotes;
    @JsonProperty("price_difference")
    private BigDecimal priceDifference;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonProperty("details")
    private List<ExchangeRequestDetailResponse> details;
    
    public ExchangeRequestResponse() {}
    
    public ExchangeRequestResponse(ExchangeRequest entity) {
        this.id = entity.getId();
        this.returnRequestId = entity.getReturnRequestId();
        this.exchangeReason = entity.getExchangeReason();
        this.status = entity.getStatus() != null ? entity.getStatus().name() : null;
        this.adminNotes = entity.getAdminNotes();
        this.priceDifference = entity.getPriceDifference();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
    
    public ExchangeRequestResponse(ExchangeRequest entity, List<ExchangeRequestDetail> details) {
        this(entity);
        if (details != null) {
            this.details = details.stream()
                    .map(ExchangeRequestDetailResponse::new)
                    .collect(Collectors.toList());
        }
    }
} 