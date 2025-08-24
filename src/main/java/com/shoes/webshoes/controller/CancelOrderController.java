package com.shoes.webshoes.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.entity.CancelOrderRequest;
import com.shoes.webshoes.request.ApproveCancelOrderRequest;
import com.shoes.webshoes.request.CRUDCancelOrderRequest;
import com.shoes.webshoes.request.RejectCancelOrderRequest;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.CancelOrderResponse;
import com.shoes.webshoes.service.CancelOrderService;

@RestController
@RequestMapping("/api/v1/cancel-orders")
public class CancelOrderController {
    
    @Autowired
    private CancelOrderService cancelOrderService;
    
    // Lấy danh sách yêu cầu hủy đơn hàng với bộ lọc
    @GetMapping("")
    public ResponseEntity<BaseResponse<BaseListDataResponse<CancelOrderResponse>>> getAll(
            @RequestParam(name = "user_id", required = false, defaultValue = "-1") int userId,
            @RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
            @RequestParam(name = "status", required = false, defaultValue = "-1") String status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
        
        BaseResponse<BaseListDataResponse<CancelOrderResponse>> response = new BaseResponse<>();
        Pagination pagination = new Pagination(page, limit);
        if("-1".equals(status)) {
        	status = "";
        }
        // Convert userId to -1 if null (no filter)
        Integer userIdParam = (userId == -1) ? -1 : userId;
        
        var result = cancelOrderService.spGListCancelRequest(userIdParam, keySearch, status, pagination);
        
        BaseListDataResponse<CancelOrderResponse> listData = new BaseListDataResponse<>();
        List<CancelOrderResponse> responses = result.getResult().stream()
                .map(CancelOrderResponse::new)
                .collect(Collectors.toList());
                
        listData.setList(responses);
        listData.setTotalRecord(result.getTotalRecord());
        
        response.setData(listData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Tạo yêu cầu hủy đơn hàng
    @PostMapping
    public ResponseEntity<BaseResponse<CancelOrderResponse>> createCancelRequest(
            @RequestBody CRUDCancelOrderRequest request) {
        try {
            CancelOrderRequest cancelRequest = cancelOrderService.createCancelRequest(request);
            
            BaseResponse<CancelOrderResponse> response = new BaseResponse<>();
            response.setData(new CancelOrderResponse(cancelRequest));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BaseResponse<CancelOrderResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error creating cancel request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }
    
    // Lấy yêu cầu hủy đơn hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<CancelOrderResponse>> getCancelRequestById(
            @PathVariable Integer id) {
        try {
            CancelOrderRequest cancelRequest = cancelOrderService.getCancelRequestById(id);
            if (cancelRequest == null) {
                BaseResponse<CancelOrderResponse> errorResponse = new BaseResponse<>();
                errorResponse.setStatus(HttpStatus.NOT_FOUND);
                errorResponse.setMessageError("Cancel request not found");
                return new ResponseEntity<>(errorResponse, HttpStatus.OK);
            }
            
            BaseResponse<CancelOrderResponse> response = new BaseResponse<>();
            response.setData(new CancelOrderResponse(cancelRequest));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BaseResponse<CancelOrderResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error getting cancel request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }
    
    // Lấy yêu cầu hủy đơn hàng theo Order ID
    @GetMapping("/order/{orderId}")
    public ResponseEntity<BaseResponse<CancelOrderResponse>> getCancelRequestByOrderId(
            @PathVariable Integer orderId) {
        try {
            CancelOrderRequest cancelRequest = cancelOrderService.getCancelRequestByOrderId(orderId);
            if (cancelRequest == null) {
                BaseResponse<CancelOrderResponse> errorResponse = new BaseResponse<>();
                errorResponse.setStatus(HttpStatus.NOT_FOUND);
                errorResponse.setMessageError("Cancel request not found for order");
                return new ResponseEntity<>(errorResponse, HttpStatus.OK);
            }
            
            BaseResponse<CancelOrderResponse> response = new BaseResponse<>();
            response.setData(new CancelOrderResponse(cancelRequest));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BaseResponse<CancelOrderResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error getting cancel request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }
    
    // Lấy danh sách yêu cầu hủy đơn hàng của user
    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse<List<CancelOrderResponse>>> getCancelRequestsByUserId(
            @PathVariable Integer userId) {
        try {
            List<CancelOrderRequest> cancelRequests = cancelOrderService.getCancelRequestsByUserId(userId);
            List<CancelOrderResponse> responses = cancelRequests.stream()
                    .map(CancelOrderResponse::new)
                    .collect(Collectors.toList());
            
            BaseResponse<List<CancelOrderResponse>> response = new BaseResponse<>();
            response.setData(responses);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BaseResponse<List<CancelOrderResponse>> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error getting user cancel requests: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }
    
    // Lấy danh sách yêu cầu hủy đơn hàng theo trạng thái
    @GetMapping("/status/{status}")
    public ResponseEntity<BaseResponse<List<CancelOrderResponse>>> getCancelRequestsByStatus(
            @PathVariable String status) {
        try {
            List<CancelOrderRequest> cancelRequests = cancelOrderService.getCancelRequestsByStatus(status);
            List<CancelOrderResponse> responses = cancelRequests.stream()
                    .map(CancelOrderResponse::new)
                    .collect(Collectors.toList());
            
            BaseResponse<List<CancelOrderResponse>> response = new BaseResponse<>();
            response.setData(responses);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BaseResponse<List<CancelOrderResponse>> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error getting cancel requests by status: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }
    
    // Admin: Lấy tất cả yêu cầu hủy đơn hàng
    @GetMapping("/admin")
    public ResponseEntity<BaseResponse<List<CancelOrderResponse>>> getAllCancelRequests(
            @RequestParam(required = false) String status) {
        try {
            List<CancelOrderRequest> cancelRequests;
            if (status != null && !status.isEmpty()) {
                cancelRequests = cancelOrderService.getCancelRequestsByStatus(status);
            } else {
                cancelRequests = cancelOrderService.getAllCancelRequests();
            }
            
            List<CancelOrderResponse> responses = cancelRequests.stream()
                    .map(CancelOrderResponse::new)
                    .collect(Collectors.toList());
            
            BaseResponse<List<CancelOrderResponse>> response = new BaseResponse<>();
            response.setData(responses);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BaseResponse<List<CancelOrderResponse>> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error getting all cancel requests: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }
    
    // Admin: Duyệt yêu cầu hủy đơn hàng
    @PostMapping("/{id}/approve")
    public ResponseEntity<BaseResponse<CancelOrderResponse>> approveCancelRequest(
            @PathVariable Integer id,
            @RequestBody ApproveCancelOrderRequest request) {
        try {
            CancelOrderRequest cancelRequest = cancelOrderService.approveCancelRequest(id, request);
            
            BaseResponse<CancelOrderResponse> response = new BaseResponse<>();
            response.setData(new CancelOrderResponse(cancelRequest));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BaseResponse<CancelOrderResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error approving cancel request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }
    
    // Admin: Từ chối yêu cầu hủy đơn hàng
    @PostMapping("/{id}/reject")
    public ResponseEntity<BaseResponse<CancelOrderResponse>> rejectCancelRequest(
            @PathVariable Integer id,
            @RequestBody RejectCancelOrderRequest request) {
        try {
            CancelOrderRequest cancelRequest = cancelOrderService.rejectCancelRequest(id, request);
            
            BaseResponse<CancelOrderResponse> response = new BaseResponse<>();
            response.setData(new CancelOrderResponse(cancelRequest));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BaseResponse<CancelOrderResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error rejecting cancel request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }
    
    // Xóa yêu cầu hủy đơn hàng
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteCancelRequest(@PathVariable Integer id) {
        try {
            cancelOrderService.deleteCancelRequest(id);
            BaseResponse<Void> response = new BaseResponse<>();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BaseResponse<Void> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error deleting cancel request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }
} 