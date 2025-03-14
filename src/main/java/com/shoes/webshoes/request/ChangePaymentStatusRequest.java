package com.shoes.webshoes.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ChangePaymentStatusRequest {
    @NotNull(message = "Payment status is required")
    @JsonProperty("payment_status")
    private Integer paymentStatus;
}