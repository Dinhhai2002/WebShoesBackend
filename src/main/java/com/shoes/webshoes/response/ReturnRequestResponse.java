package com.shoes.webshoes.response;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.ReturnRequest;
import com.shoes.webshoes.entity.ReturnRequestDetail;

import lombok.Data;

@Data
public class ReturnRequestResponse {
    private Integer id;
    @JsonProperty("order_id")
    private Integer orderId;
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("return_reason")
    private String returnReason;
    @JsonProperty("return_type")
    private String returnType;
    private String status;
    @JsonProperty("admin_notes")
    private String adminNotes;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonProperty("details")
    private List<ReturnRequestDetailResponse> details;
    
    public ReturnRequestResponse() {}
    
    public ReturnRequestResponse(ReturnRequest entity) {
        this.id = entity.getId();
        this.orderId = entity.getOrderId();
        this.userId = entity.getUserId();
        this.returnReason = entity.getReturnReason();
        this.returnType = entity.getReturnType() != null ? entity.getReturnType().name() : null;
        this.status = entity.getStatus() != null ? entity.getStatus().name() : null;
        this.adminNotes = entity.getAdminNotes();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
    
    public ReturnRequestResponse(ReturnRequest entity, List<ReturnRequestDetail> details) {
        this(entity);
        if (details != null) {
            this.details = details.stream()
                    .map(ReturnRequestDetailResponse::new)
                    .collect(Collectors.toList());
        }
    }
    
    public List<ReturnRequestResponse> mapToList(List<ReturnRequest> entities) {
		return entities.stream().map(x -> new ReturnRequestResponse(x)).collect(Collectors.toList());
	}
} 