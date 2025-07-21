package com.shoes.webshoes.dao;

import com.shoes.webshoes.entity.ExchangeRequest;
import java.util.List;

public interface ExchangeRequestDAO {
    ExchangeRequest create(ExchangeRequest exchangeRequest);
    ExchangeRequest update(ExchangeRequest exchangeRequest);
    ExchangeRequest findById(Integer id);
    ExchangeRequest findByReturnRequestId(Integer returnRequestId);
    List<ExchangeRequest> findAll();
    void delete(Integer id);
} 