package com.shoes.webshoes.service;

import java.util.List;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.entity.CancelOrderRequest;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.ApproveCancelOrderRequest;
import com.shoes.webshoes.request.CRUDCancelOrderRequest;
import com.shoes.webshoes.request.RejectCancelOrderRequest;

public interface CancelOrderService {
    CancelOrderRequest createCancelRequest(CRUDCancelOrderRequest request);
    CancelOrderRequest approveCancelRequest(Integer id, ApproveCancelOrderRequest request);
    CancelOrderRequest rejectCancelRequest(Integer id, RejectCancelOrderRequest request);
    CancelOrderRequest getCancelRequestById(Integer id);
    CancelOrderRequest getCancelRequestByOrderId(Integer orderId);
    List<CancelOrderRequest> getCancelRequestsByUserId(Integer userId);
    List<CancelOrderRequest> getCancelRequestsByStatus(String status);
    List<CancelOrderRequest> getAllCancelRequests();
    void deleteCancelRequest(Integer id);
    StoreProcedureListResult<CancelOrderRequest> spGListCancelRequest(Integer userId, String keySearch, String status, Pagination pagination) throws Exception;
} 