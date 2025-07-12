package com.shoes.webshoes.controller;

import java.util.List;

import com.shoes.webshoes.request.GHNFeeRequest;
import com.shoes.webshoes.request.GHNServiceRequest;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.GHNFeeDetailResponse;
import com.shoes.webshoes.response.GHNServiceListResponse;
import com.shoes.webshoes.response.GHNServiceResponse;
import com.shoes.webshoes.service.GHNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ghn")
public class GHNController {

    @Autowired
    private GHNService ghnService;

    @PostMapping("/available-services")
    public ResponseEntity<BaseResponse<List<GHNServiceResponse>>> getAvailableServices(@RequestBody GHNServiceRequest request) {
        try {
            GHNServiceListResponse response = ghnService.getAvailableServices(request);
            BaseResponse<List<GHNServiceResponse>> baseResponse = new BaseResponse<>();
            baseResponse.setData(response.getData());
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<List<GHNServiceResponse>> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error: " + e.getMessage());
			return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    @PostMapping("/calculate-fee")
    public ResponseEntity<GHNFeeDetailResponse> calculateShippingFee(@RequestBody GHNFeeRequest request) {
        try {
            GHNFeeDetailResponse response = ghnService.calculateShippingFee(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            GHNFeeDetailResponse errorResponse = new GHNFeeDetailResponse();
            errorResponse.setCode(500);
            errorResponse.setMessage("Error: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
} 