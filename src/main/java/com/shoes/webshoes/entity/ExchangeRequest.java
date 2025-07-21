package com.shoes.webshoes.entity;

import lombok.Data;
import javax.persistence.*;
import com.shoes.webshoes.common.enums.ExchangeStatus;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "exchange_requests")
@Data
public class ExchangeRequest extends BaseEntity {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "return_request_id")
    private Integer returnRequestId;
    
    @Column(name = "exchange_reason")
    private String exchangeReason;
    
    @Enumerated(EnumType.STRING)
    private ExchangeStatus status;
    
    @Column(name = "admin_notes")
    private String adminNotes;
    
    @Column(name = "price_difference")
    private BigDecimal priceDifference; // Chênh lệch giá (có thể âm hoặc dương)
} 