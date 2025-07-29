package com.shoes.webshoes.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.CancelOrderRequest;
import lombok.Data;
import java.util.Date;

@Data
public class CancelOrderResponse {
    private Integer id;
    
    @JsonProperty("order_id")
    private Integer orderId;
    
    @JsonProperty("user_id")
    private Integer userId;
    
    @JsonProperty("cancel_reason")
    private String cancelReason;
    
    private String status;
    
    @JsonProperty("admin_notes")
    private String adminNotes;
    
    @JsonProperty("created_at")
    private Date createdAt;
    
    @JsonProperty("updated_at")
    private Date updatedAt;
    
    public CancelOrderResponse() {}
    
    public CancelOrderResponse(CancelOrderRequest entity) {
        this.id = entity.getId();
        this.orderId = entity.getOrderId();
        this.userId = entity.getUserId();
        this.cancelReason = entity.getCancelReason();
        this.status = entity.getStatus().name();
        this.adminNotes = entity.getAdminNotes();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
} 