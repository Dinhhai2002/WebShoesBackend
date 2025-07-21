package com.shoes.webshoes.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shoes.webshoes.dao.ExchangeRequestDetailDAO;
import com.shoes.webshoes.entity.ExchangeRequestDetail;
import com.shoes.webshoes.request.ExchangeRequestDetailRequest;
import com.shoes.webshoes.service.ExchangeRequestDetailService;
import com.google.gson.Gson;

@Service("ExchangeRequestDetailService")
@Transactional(rollbackFor = Error.class)
public class ExchangeRequestDetailServiceImpl implements ExchangeRequestDetailService {
    
    @Autowired
    private ExchangeRequestDetailDAO exchangeRequestDetailDao;
    
    private final Gson gson = new Gson();

    @Override
    public ExchangeRequestDetail createExchangeRequestDetail(ExchangeRequestDetailRequest request) {
        ExchangeRequestDetail detail = new ExchangeRequestDetail();
        // Note: exchangeRequestId cần được truyền từ bên ngoài
        // detail.setExchangeRequestId(exchangeRequestId);
        detail.setOldProductId(request.getOldProductId());
        detail.setOldProductDetailId(request.getOldProductDetailId());
        detail.setNewProductId(request.getNewProductId());
        detail.setNewProductDetailId(request.getNewProductDetailId());
        detail.setQuantity(request.getQuantity());
        detail.setExchangeReason(request.getExchangeReason());
        detail.setConditionDescription(request.getConditionDescription());
        detail.setImages(gson.toJson(request.getImages()));
        
        return exchangeRequestDetailDao.create(detail);
    }

    @Override
    public ExchangeRequestDetail updateExchangeRequestDetail(Integer id, ExchangeRequestDetailRequest request) {
        ExchangeRequestDetail detail = exchangeRequestDetailDao.findById(id);
        if (detail == null) {
            throw new RuntimeException("Exchange request detail not found");
        }
        
        detail.setOldProductId(request.getOldProductId());
        detail.setOldProductDetailId(request.getOldProductDetailId());
        detail.setNewProductId(request.getNewProductId());
        detail.setNewProductDetailId(request.getNewProductDetailId());
        detail.setQuantity(request.getQuantity());
        detail.setExchangeReason(request.getExchangeReason());
        detail.setConditionDescription(request.getConditionDescription());
        detail.setImages(gson.toJson(request.getImages()));
        
        return exchangeRequestDetailDao.update(detail);
    }

    @Override
    public ExchangeRequestDetail getExchangeRequestDetailById(Integer id) {
        return exchangeRequestDetailDao.findById(id);
    }

    @Override
    public List<ExchangeRequestDetail> getDetailsByExchangeRequestId(Integer exchangeRequestId) {
        return exchangeRequestDetailDao.findByExchangeRequestId(exchangeRequestId);
    }

    @Override
    public void deleteExchangeRequestDetail(Integer id) {
        exchangeRequestDetailDao.delete(id);
    }

    @Override
    public void deleteDetailsByExchangeRequestId(Integer exchangeRequestId) {
        exchangeRequestDetailDao.deleteByExchangeRequestId(exchangeRequestId);
    }

    @Override
    public List<ExchangeRequestDetail> getDetailsByOldProductId(Integer oldProductId) {
        return exchangeRequestDetailDao.findByOldProductId(oldProductId);
    }

    @Override
    public List<ExchangeRequestDetail> getDetailsByNewProductId(Integer newProductId) {
        return exchangeRequestDetailDao.findByNewProductId(newProductId);
    }
} 