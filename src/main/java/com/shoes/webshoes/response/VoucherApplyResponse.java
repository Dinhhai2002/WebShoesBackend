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

	@JsonProperty("voucher")
	private VoucherResponse voucher;

	public VoucherApplyResponse(BigDecimal totalAmount, BigDecimal amountVoucher) {
		this.totalAmount = totalAmount;
		this.amountVoucher = amountVoucher;
	}

	public VoucherApplyResponse(BigDecimal totalAmount, BigDecimal amountVoucher, VoucherResponse voucher) {
		this.totalAmount = totalAmount;
		this.amountVoucher = amountVoucher;
		this.voucher = voucher;
	}

	public void setVoucher(VoucherResponse voucher) {
		this.voucher = voucher;
	}

	public VoucherResponse getVoucher() {
		return voucher;
	}
}
