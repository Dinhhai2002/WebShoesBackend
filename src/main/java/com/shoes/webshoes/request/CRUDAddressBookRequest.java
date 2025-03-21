package com.shoes.webshoes.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CRUDAddressBookRequest {
    @NotEmpty(message = "Tên không được để trống")
    @JsonProperty("full_name")
    private String fullName;

    @NotEmpty(message = "Số điện thoại không được để trống")
    private String phone;

    @NotNull(message = "Ward ID không được để trống")
    @JsonProperty("ward_id")
    private Integer wardId;

    @NotEmpty(message = "Tên phường/xã không được để trống")
    @JsonProperty("ward_name")
    private String wardName;

    @NotNull(message = "District ID không được để trống")
    @JsonProperty("district_id")
    private Integer districtId;

    @NotEmpty(message = "Tên quận/huyện không được để trống")
    @JsonProperty("district_name")
    private String districtName;

    @NotNull(message = "City ID không được để trống")
    @JsonProperty("city_id")
    private Integer cityId;

    @NotEmpty(message = "Tên tỉnh/thành phố không được để trống")
    @JsonProperty("city_name")
    private String cityName;

    @NotEmpty(message = "Địa chỉ đầy đủ không được để trống")
    @JsonProperty("full_address")
    private String fullAddress;

    @JsonProperty("is_default")
    private Integer isDefault = 0;
}
