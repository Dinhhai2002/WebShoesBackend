package com.shoes.webshoes.service;

import com.shoes.webshoes.entity.ReturnRequestHistory;
import java.util.List;

public interface ReturnRequestHistoryService {
    ReturnRequestHistory createHistory(Integer returnRequestId, String status, String notes, Integer createdBy);
    ReturnRequestHistory getHistoryById(Integer id);
    List<ReturnRequestHistory> getHistoryByReturnRequestId(Integer returnRequestId);
    List<ReturnRequestHistory> getHistoryByStatus(String status);
} 