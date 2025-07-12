package com.shoes.webshoes.service.impl;

import com.google.gson.Gson;
import com.shoes.webshoes.request.GHNFeeRequest;
import com.shoes.webshoes.request.GHNServiceRequest;
import com.shoes.webshoes.response.GHNFeeDetailResponse;
import com.shoes.webshoes.response.GHNServiceListResponse;
import com.shoes.webshoes.response.GHNProvinceListResponse;
import com.shoes.webshoes.response.GHNDistrictListResponse;
import com.shoes.webshoes.response.GHNWardListResponse;
import com.shoes.webshoes.service.GHNService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GHNServiceImpl implements GHNService {

    @Value("${ghn.api.token}")
    private String ghnToken;

    @Value("${ghn.api.shop-id}")
    private Integer ghnShopId;

    private static final String GHN_BASE_URL = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order";
    private static final String GHN_MASTER_DATA_URL = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data";
    private static final String AVAILABLE_SERVICES_URL = GHN_BASE_URL + "/available-services";
    private static final String CALCULATE_FEE_URL = GHN_BASE_URL + "/fee";
    private static final String PROVINCES_URL = GHN_MASTER_DATA_URL + "/province";
    private static final String DISTRICTS_URL = GHN_MASTER_DATA_URL + "/district";
    private static final String WARDS_URL = GHN_MASTER_DATA_URL + "/ward";

    private final Gson gson = new Gson();

    @Override
    public GHNServiceListResponse getAvailableServices(GHNServiceRequest request) {
        try {
            // Set shop_id from configuration if not provided
            if (request.getShop_id() == null) {
                request.setShop_id(ghnShopId);
            }

            String requestBody = gson.toJson(request);
            String response = makeHttpPostRequest(AVAILABLE_SERVICES_URL, requestBody);
            
            return gson.fromJson(response, GHNServiceListResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error calling GHN available services API", e);
        }
    }

    @Override
    public GHNFeeDetailResponse calculateShippingFee(GHNFeeRequest request) {
        try {
            // Set shop_id from configuration if not provided
            if (request.getService_id() == null) {
                throw new IllegalArgumentException("service_id is required");
            }

            String requestBody = gson.toJson(request);
            String response = makeHttpPostRequest(CALCULATE_FEE_URL, requestBody);
            
            return gson.fromJson(response, GHNFeeDetailResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error calling GHN calculate fee API", e);
        }
    }

    @Override
    public GHNProvinceListResponse getProvinces() {
        try {
            String response = makeHttpGetRequest(PROVINCES_URL);
            return gson.fromJson(response, GHNProvinceListResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error calling GHN provinces API", e);
        }
    }

    @Override
    public GHNDistrictListResponse getDistricts(Integer provinceId) {
        try {
            String url = DISTRICTS_URL + "?province_id=" + provinceId;
            String response = makeHttpGetRequest(url);
            return gson.fromJson(response, GHNDistrictListResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error calling GHN districts API", e);
        }
    }

    @Override
    public GHNWardListResponse getWards(Integer districtId) {
        try {
            String url = WARDS_URL + "?district_id=" + districtId;
            String response = makeHttpGetRequest(url);
            return gson.fromJson(response, GHNWardListResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error calling GHN wards API", e);
        }
    }

    private String makeHttpPostRequest(String url, String requestBody) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        
        // Set headers
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Token", ghnToken);
        
        // Set request body
        httpPost.setEntity(new StringEntity(requestBody, "UTF-8"));
        
        // Execute request
        HttpResponse response = httpClient.execute(httpPost);
        return EntityUtils.toString(response.getEntity());
    }

    private String makeHttpGetRequest(String url) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        
        // Set headers
        httpGet.setHeader("Token", ghnToken);
        
        // Execute request
        HttpResponse response = httpClient.execute(httpGet);
        return EntityUtils.toString(response.getEntity());
    }
} 