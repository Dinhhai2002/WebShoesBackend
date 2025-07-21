package com.shoes.webshoes.dao;

import com.shoes.webshoes.entity.ReturnRequestHistory;
import java.util.List;

public interface ReturnRequestHistoryDAO {
    ReturnRequestHistory create(ReturnRequestHistory returnRequestHistory);
    ReturnRequestHistory findById(Integer id);
    List<ReturnRequestHistory> findByReturnRequestId(Integer returnRequestId);
    List<ReturnRequestHistory> findByStatus(String status);
} 