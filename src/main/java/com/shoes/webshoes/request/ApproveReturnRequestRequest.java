package com.shoes.webshoes.request;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ApproveReturnRequestRequest {
    @JsonProperty("admin_notes")
    private String adminNotes;
} 