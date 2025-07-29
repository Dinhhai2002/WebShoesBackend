package com.shoes.webshoes.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "save_for_later")
@Data
public class SaveForLater extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "product_detail_id")
    private Integer productDetailId;
    
} 