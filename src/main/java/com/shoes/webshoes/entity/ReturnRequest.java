package com.shoes.webshoes.entity;

import lombok.Data;
import javax.persistence.*;

import com.shoes.webshoes.common.enums.ReturnStatus;
import com.shoes.webshoes.common.enums.ReturnType;

import java.util.Date;

@Entity
@Table(name = "return_requests")
@Data
public class ReturnRequest extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "order_id")
    private Integer orderId;
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "return_reason")
    private String returnReason;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "return_type")
    private ReturnType returnType;
    
    @Enumerated(EnumType.STRING)
    private ReturnStatus status;
    
    @Column(name = "admin_notes")
    private String adminNotes;
    
} 