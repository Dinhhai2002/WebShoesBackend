package com.shoes.webshoes.entity;

import java.math.BigDecimal;
import java.util.Date;

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
@Table(name = "vouchers")
public class Voucher extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String code;
	
	@Column(name = "discount_type")
	private int discountType;

	@Column(name = "discount_value")
	private BigDecimal discountValue;

	@Column(name = "min_order_value")
	private BigDecimal minOrderValue;

	@Column(name = "max_discount")
	private BigDecimal maxDiscount;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "usage_limit")
	private int usageLimit;

	@Column(name = "used_count")
	private int usedCount;

	private int status;

	public boolean isCurrentDateInRange() {
		Date currentDate = new Date();
		return (startDate != null && endDate != null) 
			   && (currentDate.compareTo(startDate) >= 0 && currentDate.compareTo(endDate) <= 0);
	}

	public boolean isNumberLimit() {
		return usedCount >= usageLimit;
	}
}
