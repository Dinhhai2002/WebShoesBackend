package com.shoes.webshoes.service;

import com.shoes.webshoes.request.GHNFeeRequest;
import com.shoes.webshoes.request.GHNServiceRequest;
import com.shoes.webshoes.response.GHNFeeDetailResponse;
import com.shoes.webshoes.response.GHNServiceListResponse;
import com.shoes.webshoes.response.GHNProvinceListResponse;
import com.shoes.webshoes.response.GHNDistrictListResponse;
import com.shoes.webshoes.response.GHNWardListResponse;

public interface GHNService {
    GHNServiceListResponse getAvailableServices(GHNServiceRequest request);
    GHNFeeDetailResponse calculateShippingFee(GHNFeeRequest request);
    GHNProvinceListResponse getProvinces();
    GHNDistrictListResponse getDistricts(Integer provinceId);
    GHNWardListResponse getWards(Integer districtId);
} 