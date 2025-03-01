package com.shoes.webshoes.request;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CRUDVoucherRequest {
    @NotEmpty(message = "name không được phép để trống")
	@Length(max = 255, message = "Không được phép lớn hơn 255 kí tự")
	private String code;

	@Min(value = 1 , message ="discountType not null")
	@JsonProperty("discount_type")
	private int discountType;

	@Min(value = 0 , message ="discountValue not null")
	@JsonProperty("discount_value")
	private BigDecimal discountValue;

	@Min(value = 1 , message ="minOrderValue not null")
	@JsonProperty("min_order_value")
	private BigDecimal minOrderValue;

	@Min(value = 1 , message ="maxDiscount not null")
	@JsonProperty("max_discount")
	private BigDecimal maxDiscount;

	@JsonProperty("start_date")
	private String startDate;

	@JsonProperty("end_date")
	private String endDate;

	@Min(value = 1 , message ="usageLimit not null")
	@JsonProperty("usage_limit")
	private int usageLimit;

	@Min(value = 0 , message ="usedCount not null")
	@JsonProperty("used_count")
	private int usedCount;

}

