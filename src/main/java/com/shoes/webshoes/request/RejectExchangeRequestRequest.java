package com.shoes.webshoes.request;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
@Data
public class RejectExchangeRequestRequest {
    @JsonProperty("admin_notes")
    private String adminNotes;
} 