package com.shoes.webshoes.dao;

import com.shoes.webshoes.entity.CancelOrderHistory;
import java.util.List;

public interface CancelOrderHistoryDAO {
    CancelOrderHistory create(CancelOrderHistory cancelOrderHistory);
    List<CancelOrderHistory> findByCancelRequestId(Integer cancelRequestId);
    void deleteByCancelRequestId(Integer cancelRequestId);
} 