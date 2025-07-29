package com.shoes.webshoes.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "cancel_order_requests")
@Data
public class CancelOrderRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "order_id")
    private Integer orderId;
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "cancel_reason")
    private String cancelReason;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CancelOrderStatus status = CancelOrderStatus.PENDING;
    
    @Column(name = "admin_notes")
    private String adminNotes;
    
    public enum CancelOrderStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
} 