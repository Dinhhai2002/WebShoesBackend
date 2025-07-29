package com.shoes.webshoes.dao;

import com.shoes.webshoes.entity.CancelOrderRequest;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.model.StoreProcedureListResult;
import java.util.List;

public interface CancelOrderRequestDAO {
    CancelOrderRequest create(CancelOrderRequest cancelOrderRequest);
    CancelOrderRequest update(CancelOrderRequest cancelOrderRequest);
    CancelOrderRequest findById(Integer id);
    CancelOrderRequest findByOrderId(Integer orderId);
    List<CancelOrderRequest> findByUserId(Integer userId);
    List<CancelOrderRequest> findByStatus(String status);
    List<CancelOrderRequest> findAll();
    void delete(Integer id);
    StoreProcedureListResult<CancelOrderRequest> spGListCancelRequest(Integer userId, String keySearch, String status, Pagination pagination) throws Exception;
} 