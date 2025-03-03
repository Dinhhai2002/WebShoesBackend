package com.shoes.webshoes.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VoucherApplyResponse {
    @JsonProperty("total_amount")
	private BigDecimal totalAmount;

	@JsonProperty("amount_voucher")
	private BigDecimal amountVoucher;

    public VoucherApplyResponse(BigDecimal totalAmount, BigDecimal amountVoucher) {
		this.totalAmount = totalAmount;
		this.amountVoucher = amountVoucher;
	}
}
