package com.shoes.webshoes.entity;

import java.sql.Timestamp;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "addressbook")
public class AddressBook extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "full_name")
    private String fullName;

    private String phone;

    @Column(name = "ward_id")
    private Integer wardId;

    @Column(name = "ward_name")
    private String wardName;

    @Column(name = "district_id")
    private Integer districtId;

    @Column(name = "district_name")
    private String districtName;

    @Column(name = "city_id")
    private Integer cityId;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "full_address")
    private String fullAddress;

    @Column(name = "is_default")
    private int isDefault;

    private int status;

}
