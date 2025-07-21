package com.shoes.webshoes.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "return_request_history")
@Data
public class ReturnRequestHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "return_request_id")
    private Integer returnRequestId;
    
    private String status;
    
    private String notes;
    
    @Column(name = "created_by")
    private Integer createdBy;
    
    @Column(name = "created_at")
    private Date createdAt;
} 