package com.shoes.webshoes.service;

import com.shoes.webshoes.entity.ExchangeRequest;
import com.shoes.webshoes.request.ExchangeRequestRequest;
import com.shoes.webshoes.request.ApproveExchangeRequestRequest;
import com.shoes.webshoes.request.RejectExchangeRequestRequest;
import java.util.List;

public interface ExchangeRequestService {
    ExchangeRequest createExchangeRequest(ExchangeRequestRequest request);
    ExchangeRequest updateExchangeRequest(Integer id, ExchangeRequestRequest request);
    ExchangeRequest getExchangeRequestById(Integer id);
    ExchangeRequest getExchangeRequestByReturnRequestId(Integer returnRequestId);
    List<ExchangeRequest> getAllExchangeRequests();
    ExchangeRequest approveExchangeRequest(Integer id, ApproveExchangeRequestRequest request);
    ExchangeRequest rejectExchangeRequest(Integer id, RejectExchangeRequestRequest request);
    ExchangeRequest processExchangeRequest(Integer id);
    ExchangeRequest completeExchangeRequest(Integer id);
    void cancelExchangeRequest(Integer id);
    void deleteExchangeRequest(Integer id);
} 