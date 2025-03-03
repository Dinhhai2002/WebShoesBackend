package com.shoes.webshoes.entity;

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
@Table(name = "voucher_applications")
public class VoucherApplication extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "voucher_id")
	private int voucherId;

	@Column(name = "product_id")
	private Integer productId;

	@Column(name = "brand_id")
	private Integer brandId;

	@Column(name = "category_id")
	private Integer categoryId;

	private int status;
}
