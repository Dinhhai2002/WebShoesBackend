package com.shoes.webshoes.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.shoes.webshoes.entity.SaveForLater;
import com.shoes.webshoes.entity.ProductDetail;
import com.shoes.webshoes.request.SaveForLaterRequest;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.SaveForLaterResponse;
import com.shoes.webshoes.service.SaveForLaterService;
import com.shoes.webshoes.service.ProductDetailService;

@RestController
@RequestMapping("/api/v1/save-for-later")
public class SaveForLaterController {
    
    @Autowired
    private SaveForLaterService saveForLaterService;

    @Autowired 
    private ProductDetailService productDetailService;
    
    // Thêm sản phẩm vào danh sách lưu mua sau
    @PostMapping
    public ResponseEntity<BaseResponse<SaveForLaterResponse>> addToSaveForLater(
            @RequestBody SaveForLaterRequest request) {
        try {
            SaveForLater saveForLater = saveForLaterService.addToSaveForLater(request);

            ProductDetail productDetail = productDetailService.findOne(request.getProductDetailId());
            
            BaseResponse<SaveForLaterResponse> response = new BaseResponse<>();
            response.setData(new SaveForLaterResponse(saveForLater, productDetail));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BaseResponse<SaveForLaterResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error adding to save for later: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }
    
    // Lấy danh sách sản phẩm lưu mua sau của user
    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse<List<SaveForLaterResponse>>> getSaveForLaterByUserId(
            @PathVariable Integer userId) {
        try {
            List<SaveForLater> saveForLaterList = saveForLaterService.getSaveForLaterByUserId(userId);
            List<SaveForLaterResponse> responses = saveForLaterList.stream()
                    .map(saveForLater -> {
                        ProductDetail productDetail = productDetailService.findOne(saveForLater.getProductDetailId());
                        return new SaveForLaterResponse(saveForLater, productDetail);
                    })
                    .collect(Collectors.toList());
            
            BaseResponse<List<SaveForLaterResponse>> response = new BaseResponse<>();
            response.setData(responses);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BaseResponse<List<SaveForLaterResponse>> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error getting save for later list: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }
    
    // Xóa sản phẩm khỏi danh sách lưu mua sau
    @DeleteMapping("/{userId}/{productDetailId}")
    public ResponseEntity<BaseResponse<Void>> removeFromSaveForLater(
            @PathVariable Integer userId,
            @PathVariable Integer productDetailId) {
        try {
            saveForLaterService.removeFromSaveForLater(userId, productDetailId);
            
            BaseResponse<Void> response = new BaseResponse<>();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BaseResponse<Void> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error removing from save for later: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }
    
    // Chuyển sản phẩm từ lưu mua sau sang giỏ hàng
    @PostMapping("/{userId}/{productDetailId}/move-to-cart")
    public ResponseEntity<BaseResponse<Void>> moveToCart(
            @PathVariable Integer userId,
            @PathVariable Integer productDetailId) {
        try {
            saveForLaterService.moveToCart(userId, productDetailId);
            
            BaseResponse<Void> response = new BaseResponse<>();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BaseResponse<Void> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error moving to cart: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }
} 