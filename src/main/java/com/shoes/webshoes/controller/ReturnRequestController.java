package com.shoes.webshoes.controller;

import com.shoes.webshoes.entity.Category;
import com.shoes.webshoes.entity.ReturnRequest;
import com.shoes.webshoes.entity.ReturnRequestDetail;
import com.shoes.webshoes.request.ReturnRequestRequest;
import com.shoes.webshoes.request.ApproveReturnRequestRequest;
import com.shoes.webshoes.request.RejectReturnRequestRequest;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.CategoryResponse;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.ReturnRequestResponse;
import com.shoes.webshoes.service.ReturnRequestService;
import com.shoes.webshoes.service.IFirebaseImageService;
import com.shoes.webshoes.service.ReturnRequestDetailService;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.common.utils.StringErrorValue;
import com.shoes.webshoes.model.StoreProcedureListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/return-requests")
public class ReturnRequestController {

    @Autowired
    private ReturnRequestService returnRequestService;

    @Autowired
    private ReturnRequestDetailService returnRequestDetailService;
    
    @Autowired
    private IFirebaseImageService iFirebaseImageService;

    // Lấy danh sách yêu cầu trả hàng với bộ lọc
    @GetMapping("")
    public ResponseEntity<BaseResponse<BaseListDataResponse<ReturnRequestResponse>>> getAll(
            @RequestParam(name = "user_id", required = false, defaultValue = "-1") int userId,
            @RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
        
        BaseResponse<BaseListDataResponse<ReturnRequestResponse>> response = new BaseResponse<>();
        Pagination pagination = new Pagination(page, limit);
        
        
        // Convert userId to -1 if null (no filter) for stored procedure
        Integer userIdParam = (userId == -1) ? -1 : userId;
        
        StoreProcedureListResult<ReturnRequest> listReturnRequest = returnRequestService.spGListReturnRequest(
                userIdParam, keySearch, status, pagination);

        BaseListDataResponse<ReturnRequestResponse> listData = new BaseListDataResponse<>();

        // Map to response with details
        List<ReturnRequestResponse> responses = listReturnRequest.getResult().stream()
                .map(request -> {
                    List<ReturnRequestDetail> details = returnRequestDetailService.getDetailsByReturnRequestId(request.getId());
                    return new ReturnRequestResponse(request, details);
                })
                .collect(Collectors.toList());

        listData.setList(responses);
        listData.setTotalRecord(listReturnRequest.getTotalRecord());

        response.setData(listData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Tạo yêu cầu trả hàng
    @PostMapping
    public ResponseEntity<BaseResponse<ReturnRequestResponse>> createReturnRequest(
            @RequestBody ReturnRequestRequest request) {
        try {
            ReturnRequest returnRequest = returnRequestService.createReturnRequest(request);
            
            // Lấy details
            List<ReturnRequestDetail> details = returnRequestDetailService.getDetailsByReturnRequestId(returnRequest.getId());
            ReturnRequestResponse response = new ReturnRequestResponse(returnRequest, details);
            
            BaseResponse<ReturnRequestResponse> baseResponse = new BaseResponse<>();
            baseResponse.setData(response);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<ReturnRequestResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error creating return request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Lấy yêu cầu trả hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ReturnRequestResponse>> getReturnRequestById(@PathVariable Integer id) {
        try {
            ReturnRequest returnRequest = returnRequestService.getReturnRequestById(id);
            if (returnRequest == null) {
                BaseResponse<ReturnRequestResponse> errorResponse = new BaseResponse<>();
                errorResponse.setStatus(HttpStatus.NOT_FOUND);
                errorResponse.setMessageError("Return request not found");
                return new ResponseEntity<>(errorResponse, HttpStatus.OK);
            }
            
            // Lấy details
            List<ReturnRequestDetail> details = returnRequestDetailService.getDetailsByReturnRequestId(returnRequest.getId());
            ReturnRequestResponse response = new ReturnRequestResponse(returnRequest, details);
            
            BaseResponse<ReturnRequestResponse> baseResponse = new BaseResponse<>();
            baseResponse.setData(response);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<ReturnRequestResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error getting return request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Lấy danh sách yêu cầu trả hàng của user
    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse<List<ReturnRequestResponse>>> getReturnRequestsByUserId(
            @PathVariable Integer userId) {
        try {
            List<ReturnRequest> returnRequests = returnRequestService.getReturnRequestsByUserId(userId);
            List<ReturnRequestResponse> responses = returnRequests.stream()
                    .map(request -> {
                        List<ReturnRequestDetail> details = returnRequestDetailService.getDetailsByReturnRequestId(request.getId());
                        return new ReturnRequestResponse(request, details);
                    })
                    .collect(Collectors.toList());
            
            BaseResponse<List<ReturnRequestResponse>> baseResponse = new BaseResponse<>();
            baseResponse.setData(responses);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<List<ReturnRequestResponse>> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error getting user return requests: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Lấy danh sách yêu cầu trả hàng theo đơn hàng
    @GetMapping("/order/{orderId}")
    public ResponseEntity<BaseResponse<List<ReturnRequestResponse>>> getReturnRequestsByOrderId(
            @PathVariable Integer orderId) {
        try {
            List<ReturnRequest> returnRequests = returnRequestService.getReturnRequestsByOrderId(orderId);
            List<ReturnRequestResponse> responses = returnRequests.stream()
                    .map(request -> {
                        List<ReturnRequestDetail> details = returnRequestDetailService.getDetailsByReturnRequestId(request.getId());
                        return new ReturnRequestResponse(request, details);
                    })
                    .collect(Collectors.toList());
            
            BaseResponse<List<ReturnRequestResponse>> baseResponse = new BaseResponse<>();
            baseResponse.setData(responses);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<List<ReturnRequestResponse>> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error getting order return requests: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Admin: Lấy tất cả yêu cầu trả hàng
    @GetMapping("/admin")
    public ResponseEntity<BaseResponse<List<ReturnRequestResponse>>> getAllReturnRequests(
            @RequestParam(required = false) String status) {
        try {
            List<ReturnRequest> returnRequests;
            if (status != null && !status.isEmpty()) {
                returnRequests = returnRequestService.getReturnRequestsByStatus(status);
            } else {
                returnRequests = returnRequestService.getAllReturnRequests();
            }
            
            List<ReturnRequestResponse> responses = returnRequests.stream()
                    .map(request -> {
                        List<ReturnRequestDetail> details = returnRequestDetailService.getDetailsByReturnRequestId(request.getId());
                        return new ReturnRequestResponse(request, details);
                    })
                    .collect(Collectors.toList());
            
            BaseResponse<List<ReturnRequestResponse>> baseResponse = new BaseResponse<>();
            baseResponse.setData(responses);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<List<ReturnRequestResponse>> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error getting all return requests: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Admin: Duyệt yêu cầu trả hàng
    @PostMapping("/{id}/approve")
    public ResponseEntity<BaseResponse<ReturnRequestResponse>> approveReturnRequest(
            @PathVariable Integer id,
            @RequestBody ApproveReturnRequestRequest request) {
        try {
            ReturnRequest returnRequest = returnRequestService.approveReturnRequest(id, request);
            
            // Lấy details
            List<ReturnRequestDetail> details = returnRequestDetailService.getDetailsByReturnRequestId(returnRequest.getId());
            ReturnRequestResponse response = new ReturnRequestResponse(returnRequest, details);
            
            BaseResponse<ReturnRequestResponse> baseResponse = new BaseResponse<>();
            baseResponse.setData(response);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<ReturnRequestResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error approving return request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Admin: Từ chối yêu cầu trả hàng
    @PostMapping("/{id}/reject")
    public ResponseEntity<BaseResponse<ReturnRequestResponse>> rejectReturnRequest(
            @PathVariable Integer id,
            @RequestBody RejectReturnRequestRequest request) {
        try {
            ReturnRequest returnRequest = returnRequestService.rejectReturnRequest(id, request);
            
            // Lấy details
            List<ReturnRequestDetail> details = returnRequestDetailService.getDetailsByReturnRequestId(returnRequest.getId());
            ReturnRequestResponse response = new ReturnRequestResponse(returnRequest, details);
            
            BaseResponse<ReturnRequestResponse> baseResponse = new BaseResponse<>();
            baseResponse.setData(response);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<ReturnRequestResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error rejecting return request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Admin: Bắt đầu xử lý trả hàng
    @PostMapping("/{id}/process")
    public ResponseEntity<BaseResponse<ReturnRequestResponse>> processReturnRequest(@PathVariable Integer id) {
        try {
            ReturnRequest returnRequest = returnRequestService.processReturnRequest(id);
            
            // Lấy details
            List<ReturnRequestDetail> details = returnRequestDetailService.getDetailsByReturnRequestId(returnRequest.getId());
            ReturnRequestResponse response = new ReturnRequestResponse(returnRequest, details);
            
            BaseResponse<ReturnRequestResponse> baseResponse = new BaseResponse<>();
            baseResponse.setData(response);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<ReturnRequestResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error processing return request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Admin: Hoàn thành trả hàng
    @PostMapping("/{id}/complete")
    public ResponseEntity<BaseResponse<ReturnRequestResponse>> completeReturnRequest(@PathVariable Integer id) {
        try {
            ReturnRequest returnRequest = returnRequestService.completeReturnRequest(id);
            
            // Lấy details
            List<ReturnRequestDetail> details = returnRequestDetailService.getDetailsByReturnRequestId(returnRequest.getId());
            ReturnRequestResponse response = new ReturnRequestResponse(returnRequest, details);
            
            BaseResponse<ReturnRequestResponse> baseResponse = new BaseResponse<>();
            baseResponse.setData(response);
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<ReturnRequestResponse> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error completing return request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Hủy yêu cầu trả hàng
    @PostMapping("/{id}/cancel")
    public ResponseEntity<BaseResponse<Void>> cancelReturnRequest(@PathVariable Integer id) {
        try {
            returnRequestService.cancelReturnRequest(id);
            BaseResponse<Void> baseResponse = new BaseResponse<>();
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<Void> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error cancelling return request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    // Xóa yêu cầu trả hàng
    @PostMapping("/{id}/deleted")
    public ResponseEntity<BaseResponse<Void>> deleteReturnRequest(@PathVariable Integer id) {
        try {
            returnRequestService.deleteReturnRequest(id);
            BaseResponse<Void> baseResponse = new BaseResponse<>();
            return ResponseEntity.ok(baseResponse);
        } catch (Exception e) {
            BaseResponse<Void> errorResponse = new BaseResponse<>();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setMessageError("Error deleting return request: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/{id}/image")
	public ResponseEntity<BaseResponse> uploadImage(@RequestParam(name = "file") MultipartFile file,
			@PathVariable("id") int id) throws Exception {
		BaseResponse response = new BaseResponse();
		String fileName = iFirebaseImageService.save(file);

		String imageUrl = iFirebaseImageService.getImageUrl(fileName);

		response.setData(imageUrl);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
} 