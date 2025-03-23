package com.shoes.webshoes.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.Voucher;

import lombok.Data;

@Data
public class VoucherResponse {
    private int id;

	private String code;
	
	@JsonProperty("discount_type")
	private int discountType;

	@JsonProperty("discount_value")
	private BigDecimal discountValue;

	@JsonProperty("min_order_value")
	private BigDecimal minOrderValue;

	@JsonProperty("max_discount")
	private BigDecimal maxDiscount;

	@JsonProperty("start_date")
	private String startDate;

	@JsonProperty("end_date")
	private String endDate;

	@JsonProperty("usage_limit")
	private int usageLimit;

	@JsonProperty("used_count")
	private int usedCount;

	private int status;

	public VoucherResponse() {

	}

	public VoucherResponse(Voucher entity) {
		this.id = entity.getId();
		this.code = entity.getCode();
		this.discountType = entity.getDiscountType();
		this.discountValue = entity.getDiscountValue();
		this.minOrderValue = entity.getMinOrderValue();
		this.maxDiscount = entity.getMaxDiscount();
		this.startDate = entity.getFullDatetimeFormatVN(entity.getStartDate());
		this.endDate = entity.getFullDatetimeFormatVN(entity.getEndDate());
		this.usageLimit = entity.getUsageLimit();
		this.usedCount = entity.getUsedCount();
		this.status = entity.getStatus();
	}

	public List<VoucherResponse> mapToList(List<Voucher> entities) {
		return entities.stream().map(x -> new VoucherResponse(x)).collect(Collectors.toList());
	}
}
