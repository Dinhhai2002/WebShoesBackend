package com.shoes.webshoes.controller;

import com.shoes.webshoes.entity.ExchangeRequest;
import com.shoes.webshoes.entity.ExchangeRequestDetail;
import com.shoes.webshoes.request.ExchangeRequestRequest;
import com.shoes.webshoes.request.ApproveExchangeRequestRequest;
import com.shoes.webshoes.request.RejectExchangeRequestRequest;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.ExchangeRequestResponse;
import com.shoes.webshoes.service.ExchangeRequestService;
import com.shoes.webshoes.service.ExchangeRequestDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/exchange-requests")
public class ExchangeRequestController {

    @Autowired
    private ExchangeRequestService exchangeRequestService;

    @Autowired
    private ExchangeRequestDetailService exchangeRequestDetailService;

    // Tạo yêu cầu đổi hàng
    @PostMapping
    public ResponseEntity<BaseResponse<ExchangeRequestResponse>> createExchangeRequest(
            @RequestBody ExchangeRequestRequest request) {
        try {
            ExchangeRequest exchangeRequest = exchangeRequestService.createExchangeRequest(request);
            
            // Lấy details
            List<ExchangeRequestDetail> details = exchangeRequestDetailService.getDetailsByExchangeRequestId(exchangeRequest.getId());
            ExchangeRequestResponse response = new ExchangeRequestResponse(exchangeRequest, details);
            
            BaseResponse<ExchangeRequestResponse> baseResponse = new BaseResponse<>();
            baseResponse.setData(response);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<ExchangeRequestResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error creating exchange request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Lấy yêu cầu đổi hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ExchangeRequestResponse>> getExchangeRequestById(@PathVariable Integer id) {
        try {
            ExchangeRequest exchangeRequest = exchangeRequestService.getExchangeRequestById(id);
            if (exchangeRequest == null) {
                BaseResponse<ExchangeRequestResponse> errorResponse = new BaseResponse<>();
                errorResponse.setStatus(HttpStatus.NOT_FOUND);
                errorResponse.setMessageError("Exchange request not found");
                return new ResponseEntity<>(errorResponse, HttpStatus.OK);
            }
            
            // Lấy details
            List<ExchangeRequestDetail> details = exchangeRequestDetailService.getDetailsByExchangeRequestId(exchangeRequest.getId());
            ExchangeRequestResponse response = new ExchangeRequestResponse(exchangeRequest, details);
            
            BaseResponse<ExchangeRequestResponse> baseResponse = new BaseResponse<>();
            baseResponse.setData(response);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<ExchangeRequestResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error getting exchange request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Lấy yêu cầu đổi hàng theo return request ID
    @GetMapping("/return-request/{returnRequestId}")
    public ResponseEntity<BaseResponse<ExchangeRequestResponse>> getExchangeRequestByReturnRequestId(@PathVariable Integer returnRequestId) {
        try {
            ExchangeRequest exchangeRequest = exchangeRequestService.getExchangeRequestByReturnRequestId(returnRequestId);
            if (exchangeRequest == null) {
                BaseResponse<ExchangeRequestResponse> errorResponse = new BaseResponse<>();
                errorResponse.setStatus(HttpStatus.NOT_FOUND);
                errorResponse.setMessageError("Exchange request not found for return request ID: " + returnRequestId);
                return new ResponseEntity<>(errorResponse, HttpStatus.OK);
            }
            
            // Lấy details
            List<ExchangeRequestDetail> details = exchangeRequestDetailService.getDetailsByExchangeRequestId(exchangeRequest.getId());
            ExchangeRequestResponse response = new ExchangeRequestResponse(exchangeRequest, details);
            
            BaseResponse<ExchangeRequestResponse> baseResponse = new BaseResponse<>();
            baseResponse.setData(response);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<ExchangeRequestResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error getting exchange request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Admin: Lấy tất cả yêu cầu đổi hàng
    @GetMapping("/admin")
    public ResponseEntity<BaseResponse<List<ExchangeRequestResponse>>> getAllExchangeRequests() {
        try {
            List<ExchangeRequest> exchangeRequests = exchangeRequestService.getAllExchangeRequests();
            
            List<ExchangeRequestResponse> responses = exchangeRequests.stream()
                    .map(request -> {
                        List<ExchangeRequestDetail> details = exchangeRequestDetailService.getDetailsByExchangeRequestId(request.getId());
                        return new ExchangeRequestResponse(request, details);
                    })
                    .collect(Collectors.toList());
            
            BaseResponse<List<ExchangeRequestResponse>> baseResponse = new BaseResponse<>();
            baseResponse.setData(responses);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<List<ExchangeRequestResponse>> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error getting all exchange requests: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Admin: Duyệt yêu cầu đổi hàng
    @PostMapping("/{id}/approve")
    public ResponseEntity<BaseResponse<ExchangeRequestResponse>> approveExchangeRequest(
            @PathVariable Integer id,
            @RequestBody ApproveExchangeRequestRequest request) {
        try {
            ExchangeRequest exchangeRequest = exchangeRequestService.approveExchangeRequest(id, request);
            
            // Lấy details
            List<ExchangeRequestDetail> details = exchangeRequestDetailService.getDetailsByExchangeRequestId(exchangeRequest.getId());
            ExchangeRequestResponse response = new ExchangeRequestResponse(exchangeRequest, details);
            
            BaseResponse<ExchangeRequestResponse> baseResponse = new BaseResponse<>();
            baseResponse.setData(response);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<ExchangeRequestResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error approving exchange request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Admin: Từ chối yêu cầu đổi hàng
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<ExchangeRequestResponse>> rejectExchangeRequest(
            @PathVariable Integer id,
            @RequestBody RejectExchangeRequestRequest request) {
        try {
            ExchangeRequest exchangeRequest = exchangeRequestService.rejectExchangeRequest(id, request);
            
            // Lấy details
            List<ExchangeRequestDetail> details = exchangeRequestDetailService.getDetailsByExchangeRequestId(exchangeRequest.getId());
            ExchangeRequestResponse response = new ExchangeRequestResponse(exchangeRequest, details);
            
            BaseResponse<ExchangeRequestResponse> baseResponse = new BaseResponse<>();
            baseResponse.setData(response);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<ExchangeRequestResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error rejecting exchange request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Admin: Bắt đầu xử lý đổi hàng
    @PostMapping("/{id}/process")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<ExchangeRequestResponse>> processExchangeRequest(@PathVariable Integer id) {
        try {
            ExchangeRequest exchangeRequest = exchangeRequestService.processExchangeRequest(id);
            
            // Lấy details
            List<ExchangeRequestDetail> details = exchangeRequestDetailService.getDetailsByExchangeRequestId(exchangeRequest.getId());
            ExchangeRequestResponse response = new ExchangeRequestResponse(exchangeRequest, details);
            
            BaseResponse<ExchangeRequestResponse> baseResponse = new BaseResponse<>();
            baseResponse.setData(response);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<ExchangeRequestResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error processing exchange request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Admin: Hoàn thành đổi hàng
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<ExchangeRequestResponse>> completeExchangeRequest(@PathVariable Integer id) {
        try {
            ExchangeRequest exchangeRequest = exchangeRequestService.completeExchangeRequest(id);
            
            // Lấy details
            List<ExchangeRequestDetail> details = exchangeRequestDetailService.getDetailsByExchangeRequestId(exchangeRequest.getId());
            ExchangeRequestResponse response = new ExchangeRequestResponse(exchangeRequest, details);
            
            BaseResponse<ExchangeRequestResponse> baseResponse = new BaseResponse<>();
            baseResponse.setData(response);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<ExchangeRequestResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error completing exchange request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Hủy yêu cầu đổi hàng
    @PostMapping("/{id}/cancel")
    public ResponseEntity<BaseResponse<Void>> cancelExchangeRequest(@PathVariable Integer id) {
        try {
            exchangeRequestService.cancelExchangeRequest(id);
            BaseResponse<Void> baseResponse = new BaseResponse<>();
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<Void> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error cancelling exchange request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Xóa yêu cầu đổi hàng
    @PostMapping("/{id}/deleted")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<Void>> deleteExchangeRequest(@PathVariable Integer id) {
        try {
            exchangeRequestService.deleteExchangeRequest(id);
            BaseResponse<Void> baseResponse = new BaseResponse<>();
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<Void> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error deleting exchange request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }
} 