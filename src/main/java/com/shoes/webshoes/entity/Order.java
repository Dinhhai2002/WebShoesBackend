package com.shoes.webshoes.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "orders")
public class Order extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "voucher_id")
	private Integer voucherId;

	private BigDecimal price;
	
	@Column(name = "discount_amount")
	private BigDecimal discountAmount;

	@Column(name = "total_price")
	private BigDecimal totalPrice;

	@Column(name = "payment_method")
	private int paymentMethod;

	@Column(name = "payment_status")
	private int paymentStatus;

	private int status;

	@Column(name = "address_id")
	private Integer addressId;

	@Column(name = "shipping_name")
	private String shippingName;

	@Column(name = "shipping_phone")
	private String shippingPhone;

	@Column(name = "shipping_ward_id")
	private Integer shippingWardId;

	@Column(name = "shipping_ward_name")
	private String shippingWardName;

	@Column(name = "shipping_district_id")
	private Integer shippingDistrictId;

	@Column(name = "shipping_district_name")
	private String shippingDistrictName;

	@Column(name = "shipping_city_id")
	private Integer shippingCityId;

	@Column(name = "shipping_city_name")
	private String shippingCityName;

	@Column(name = "shipping_address")
	private String shippingAddress;
	
	@Column(name = "customer_phone")
	private String customerPhone;
}
