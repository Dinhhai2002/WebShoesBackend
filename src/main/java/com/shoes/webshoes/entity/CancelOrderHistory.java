package com.shoes.webshoes.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cancel_order_history")
@Data
public class CancelOrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "cancel_request_id")
    private Integer cancelRequestId;
    
    private String status;
    
    private String notes;
    
    @Column(name = "created_by")
    private Integer createdBy;
    
    @Column(name = "created_at")
    private Date createdAt;
} 