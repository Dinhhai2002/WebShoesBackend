package com.shoes.webshoes.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.Order;

import lombok.Data;

@Data
public class OrderResponse {
	private int id;

	@JsonProperty("user_id")
	private int userId;

	@JsonProperty("voucher_id")
	private Integer voucherId;

	private BigDecimal price;

	@JsonProperty("discount_amount")
	private BigDecimal discountAmount;

	@JsonProperty("total_price")
	private BigDecimal totalPrice;

	@JsonProperty("payment_method")
	private int paymentMethod;

	@JsonProperty("payment_status")
	private int paymentStatus;

	private int status;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("order_detail")
	private List<OrderDetailResponse> orderDetailResponse;

	@JsonProperty("address_id")
	private Integer addressId;

	@JsonProperty("shipping_name")
	private String shippingName;

	@JsonProperty("shipping_phone")
	private String shippingPhone;

	@JsonProperty("shipping_ward_id")
	private Integer shippingWardId;

	@JsonProperty("shipping_ward_name")
	private String shippingWardName;

	@JsonProperty("shipping_district_id")
	private Integer shippingDistrictId;

	@JsonProperty("shipping_district_name")
	private String shippingDistrictName;

	@JsonProperty("shipping_city_id")
	private Integer shippingCityId;

	@JsonProperty("shipping_city_name")
	private String shippingCityName;

	@JsonProperty("shipping_address")
	private String shippingAddress;

	@JsonProperty("customer_phone")
	private String customerPhone;

	@JsonProperty("amount_shipping")
	private BigDecimal amountShipping;

	public OrderResponse() {

	}

	public OrderResponse(Order entity) {
		this.id = entity.getId();
		this.userId = entity.getUserId();
		this.voucherId = entity.getVoucherId();
		this.price = entity.getPrice();
		this.discountAmount = entity.getDiscountAmount();
		this.totalPrice = entity.getTotalPrice();
		this.paymentMethod = entity.getPaymentMethod();
		this.paymentStatus = entity.getPaymentStatus();
		this.status = entity.getStatus();
		this.createdAt = entity.getDatetimeFormatVN(entity.getCreatedAt());
		this.addressId = entity.getAddressId();
		this.shippingName = entity.getShippingName();
		this.shippingPhone = entity.getShippingPhone();
		this.shippingWardId = entity.getShippingWardId();
		this.shippingWardName = entity.getShippingWardName();
		this.shippingDistrictId = entity.getShippingDistrictId();
		this.shippingDistrictName = entity.getShippingDistrictName();
		this.shippingCityId = entity.getShippingCityId();
		this.shippingCityName = entity.getShippingCityName();
		this.shippingAddress = entity.getShippingAddress();
		this.customerPhone = entity.getCustomerPhone();
		this.amountShipping = entity.getAmountShipping();
	}

	public OrderResponse(Order entity, List<OrderDetailResponse> orderDetailResponse) {
		this(entity);
		this.orderDetailResponse = orderDetailResponse;
	}

	public List<OrderResponse> mapToList(List<Order> entities) {
		return entities.stream().map(x -> new OrderResponse(x)).collect(Collectors.toList());
	}
}
