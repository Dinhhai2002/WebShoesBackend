package com.shoes.webshoes.service;

import com.shoes.webshoes.entity.ReturnRequest;
import com.shoes.webshoes.request.ReturnRequestRequest;
import com.shoes.webshoes.request.ApproveReturnRequestRequest;
import com.shoes.webshoes.request.RejectReturnRequestRequest;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.model.StoreProcedureListResult;
import java.util.List;
import java.util.Date;

public interface ReturnRequestService {
    ReturnRequest createReturnRequest(ReturnRequestRequest request);
    ReturnRequest updateReturnRequest(Integer id, ReturnRequestRequest request);
    ReturnRequest getReturnRequestById(Integer id);
    List<ReturnRequest> getReturnRequestsByUserId(Integer userId);
    List<ReturnRequest> getReturnRequestsByOrderId(Integer orderId);
    List<ReturnRequest> getReturnRequestsByStatus(String status);
    List<ReturnRequest> getAllReturnRequests();
    ReturnRequest approveReturnRequest(Integer id, ApproveReturnRequestRequest request);
    ReturnRequest rejectReturnRequest(Integer id, RejectReturnRequestRequest request);
    ReturnRequest processReturnRequest(Integer id);
    ReturnRequest completeReturnRequest(Integer id);
    void cancelReturnRequest(Integer id);
    void deleteReturnRequest(Integer id);
    
    // New method for filtering with multiple parameters
    StoreProcedureListResult<ReturnRequest> spGListReturnRequest(Integer userId, String keySearch, String status, Pagination pagination) throws Exception;
} 