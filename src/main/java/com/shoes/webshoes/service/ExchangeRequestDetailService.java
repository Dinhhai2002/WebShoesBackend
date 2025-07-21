package com.shoes.webshoes.service;

import com.shoes.webshoes.entity.ExchangeRequestDetail;
import com.shoes.webshoes.request.ExchangeRequestDetailRequest;
import java.util.List;

public interface ExchangeRequestDetailService {
    ExchangeRequestDetail createExchangeRequestDetail(ExchangeRequestDetailRequest request);
    ExchangeRequestDetail updateExchangeRequestDetail(Integer id, ExchangeRequestDetailRequest request);
    ExchangeRequestDetail getExchangeRequestDetailById(Integer id);
    List<ExchangeRequestDetail> getDetailsByExchangeRequestId(Integer exchangeRequestId);
    List<ExchangeRequestDetail> getDetailsByOldProductId(Integer oldProductId);
    List<ExchangeRequestDetail> getDetailsByNewProductId(Integer newProductId);
    void deleteExchangeRequestDetail(Integer id);
    void deleteDetailsByExchangeRequestId(Integer exchangeRequestId);
} 