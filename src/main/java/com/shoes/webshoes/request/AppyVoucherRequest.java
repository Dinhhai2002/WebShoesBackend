package com.shoes.webshoes.request;

import java.math.BigDecimal;

import javax.validation.constraints.Min;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AppyVoucherRequest {
	@Min(value = 1 , message ="totalAmount not null")
	@JsonProperty("total_amount")
	private BigDecimal totalAmount;
}
