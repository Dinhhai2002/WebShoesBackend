package com.shoes.webshoes.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.VoucherApplication;

import lombok.Data;

@Data
public class VoucherApplicationResponse {
    private int id;

	@JsonProperty("voucher_id")
	private int voucherId;

	@JsonProperty("product_id")
	private Integer productId;

	@JsonProperty("brand_id")
	private Integer brandId;

	@JsonProperty("category_id")
	private Integer categoryId;

	private int status;

	public VoucherApplicationResponse() {

	}

	public VoucherApplicationResponse(VoucherApplication entity) {
		this.id = entity.getId();
		this.voucherId = entity.getVoucherId();
		this.productId = entity.getProductId();
		this.brandId = entity.getBrandId();
		this.categoryId = entity.getCategoryId();
		this.status = entity.getStatus();
	}

	public List<VoucherApplicationResponse> mapToList(List<VoucherApplication> entities) {
		return entities.stream().map(x -> new VoucherApplicationResponse(x)).collect(Collectors.toList());
	}
}
