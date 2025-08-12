package com.shoes.webshoes.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Statistical {

	@Id
	private String date;
	
	@Column(name="total_amount")
	private BigDecimal totalAmount;
	
	@Column(name="total_amount_all")
	private BigDecimal totalAmountAll;
	
}
