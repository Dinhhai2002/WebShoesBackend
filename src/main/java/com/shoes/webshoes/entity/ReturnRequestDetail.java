package com.shoes.webshoes.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "return_request_details")
@Data
public class ReturnRequestDetail extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "return_request_id")
    private Integer returnRequestId;

    @Column(name = "product_id")
    private Integer productId;
    
    @Column(name = "product_detail_id")
    private Integer productDetailId;
    
    @Column(name = "price")
    private Double price;

    private Integer quantity;
    
    @Column(name = "return_reason")
    private String returnReason;
    
    @Column(name = "condition_description")
    private String conditionDescription;
    
    private String images; // JSON array of image URLs
} 