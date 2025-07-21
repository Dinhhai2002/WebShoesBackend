package com.shoes.webshoes.dao;

import java.util.List;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.entity.ReturnRequest;
import com.shoes.webshoes.model.StoreProcedureListResult;

public interface ReturnRequestDAO {
    ReturnRequest create(ReturnRequest returnRequest);
    ReturnRequest update(ReturnRequest returnRequest);
    ReturnRequest findById(Integer id);
    List<ReturnRequest> findByUserId(Integer userId);
    List<ReturnRequest> findByOrderId(Integer orderId);
    List<ReturnRequest> findByStatus(String status);
    List<ReturnRequest> findAll();
    void delete(Integer id);
    StoreProcedureListResult<ReturnRequest> spGListReturnRequest(Integer userId, String keySearch, String status, Pagination pagination) throws Exception;
    // Thêm hàm kiểm tra trùng
    ReturnRequest findByUserIdAndOrderId(Integer userId, Integer orderId);
} 