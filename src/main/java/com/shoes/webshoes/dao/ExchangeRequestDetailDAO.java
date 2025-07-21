package com.shoes.webshoes.dao;

import com.shoes.webshoes.entity.ExchangeRequestDetail;
import java.util.List;

public interface ExchangeRequestDetailDAO {
    ExchangeRequestDetail create(ExchangeRequestDetail exchangeRequestDetail);
    ExchangeRequestDetail update(ExchangeRequestDetail exchangeRequestDetail);
    ExchangeRequestDetail findById(Integer id);
    List<ExchangeRequestDetail> findByExchangeRequestId(Integer exchangeRequestId);
    List<ExchangeRequestDetail> findByOldProductId(Integer oldProductId);
    List<ExchangeRequestDetail> findByNewProductId(Integer newProductId);
    List<ExchangeRequestDetail> findAll();
    void delete(Integer id);
    void deleteByExchangeRequestId(Integer exchangeRequestId);
} 