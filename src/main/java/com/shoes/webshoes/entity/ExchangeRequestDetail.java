package com.shoes.webshoes.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "exchange_request_details")
@Data
public class ExchangeRequestDetail extends BaseEntity {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "exchange_request_id")
    private Integer exchangeRequestId;
    
    @Column(name = "old_product_id")
    private Integer oldProductId; // Product ID của sản phẩm cũ
    
    @Column(name = "old_product_detail_id")
    private Integer oldProductDetailId; // Sản phẩm cũ (từ return_request_details)
    
    @Column(name = "new_product_id")
    private Integer newProductId; // Product ID của sản phẩm mới
    
    @Column(name = "new_product_detail_id")
    private Integer newProductDetailId; // Sản phẩm mới muốn đổi
    
    private Integer quantity;
    
    @Column(name = "exchange_reason")
    private String exchangeReason;
    
    @Column(name = "condition_description")
    private String conditionDescription;
    
    private String images; // JSON string
} 