package com.shoes.webshoes.response;

import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.AddressBook;
import lombok.Data;

@Data
public class AddressBookResponse {
    private int id;
    
    @JsonProperty("user_id")
    private int userId;
    
    @JsonProperty("full_name")
    private String fullName;
    
    private String phone;
    
    @JsonProperty("ward_id")
    private Integer wardId;
    
    @JsonProperty("ward_name")
    private String wardName;
    
    @JsonProperty("district_id")
    private Integer districtId;
    
    @JsonProperty("district_name")
    private String districtName;
    
    @JsonProperty("city_id")
    private Integer cityId;
    
    @JsonProperty("city_name")
    private String cityName;
    
    @JsonProperty("full_address")
    private String fullAddress;
    
    @JsonProperty("is_default")
    private int isDefault;
    
    private int status;
    
    @JsonProperty("created_at")
    private String createdAt;

    public AddressBookResponse() {
    }

    public AddressBookResponse(AddressBook addressBook) {
        this.id = addressBook.getId();
        this.userId = addressBook.getUserId();
        this.fullName = addressBook.getFullName();
        this.phone = addressBook.getPhone();
        this.wardId = addressBook.getWardId();
        this.wardName = addressBook.getWardName();
        this.districtId = addressBook.getDistrictId();
        this.districtName = addressBook.getDistrictName();
        this.cityId = addressBook.getCityId();
        this.cityName = addressBook.getCityName();
        this.fullAddress = addressBook.getFullAddress();
        this.isDefault = addressBook.getIsDefault();
        this.status = addressBook.getStatus();
        this.createdAt = addressBook.getCreatedAt() != null ? addressBook.getCreatedAt().toString() : null;
    }

    public List<AddressBookResponse> mapToList(List<AddressBook> addressBooks) {
        return addressBooks.stream().map(AddressBookResponse::new).collect(Collectors.toList());
    }
}
