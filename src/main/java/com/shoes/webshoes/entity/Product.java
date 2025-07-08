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
@Table(name = "products")
public class Product extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	@Column(name = "brand_id")
	private int brandId;

	@Column(name = "category_id")
	private int categoryId;
	

	private String description;

	private BigDecimal price;

	@Column(name = "average_rating")
	private BigDecimal averageRating;

	@Column(name = "image_url")
	private String imageUrl; 

	private int status;
}
