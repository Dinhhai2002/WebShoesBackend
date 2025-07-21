package com.shoes.webshoes.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shoes.webshoes.dao.ReturnRequestDetailDAO;
import com.shoes.webshoes.entity.ReturnRequestDetail;
import com.shoes.webshoes.request.ReturnRequestDetailRequest;
import com.shoes.webshoes.service.ReturnRequestDetailService;
import com.google.gson.Gson;

@Service("ReturnRequestDetailService")
@Transactional(rollbackFor = Error.class)
public class ReturnRequestDetailServiceImpl implements ReturnRequestDetailService {
    
    @Autowired
    private ReturnRequestDetailDAO returnRequestDetailDao;
    
    private final Gson gson = new Gson();

    @Override
    public ReturnRequestDetail createReturnRequestDetail(ReturnRequestDetailRequest request) {
        ReturnRequestDetail detail = new ReturnRequestDetail();
        // Note: returnRequestId cần được truyền từ bên ngoài
        // detail.setReturnRequestId(returnRequestId);
        detail.setProductId(request.getProductId());
        detail.setProductDetailId(request.getProductDetailId());
        detail.setPrice(request.getPrice());
        detail.setQuantity(request.getQuantity());
        detail.setReturnReason(request.getReturnReason());
        detail.setConditionDescription(request.getConditionDescription());
        detail.setImages(gson.toJson(request.getImages()));
        
        return returnRequestDetailDao.create(detail);
    }

    @Override
    public ReturnRequestDetail updateReturnRequestDetail(Integer id, ReturnRequestDetailRequest request) {
        ReturnRequestDetail detail = returnRequestDetailDao.findById(id);
        if (detail == null) {
            throw new RuntimeException("Return request detail not found");
        }
        
        detail.setProductId(request.getProductId());
        detail.setProductDetailId(request.getProductDetailId());
        detail.setPrice(request.getPrice());
        detail.setQuantity(request.getQuantity());
        detail.setReturnReason(request.getReturnReason());
        detail.setConditionDescription(request.getConditionDescription());
        detail.setImages(gson.toJson(request.getImages()));
        
        return returnRequestDetailDao.update(detail);
    }

    @Override
    public ReturnRequestDetail getReturnRequestDetailById(Integer id) {
        return returnRequestDetailDao.findById(id);
    }

    @Override
    public List<ReturnRequestDetail> getDetailsByReturnRequestId(Integer returnRequestId) {
        return returnRequestDetailDao.findByReturnRequestId(returnRequestId);
    }

    @Override
    public List<ReturnRequestDetail> getDetailsByProductDetailId(Integer productDetailId) {
        return returnRequestDetailDao.findByProductDetailId(productDetailId);
    }

    @Override
    public List<ReturnRequestDetail> getDetailsByProductId(Integer productId) {
        return returnRequestDetailDao.findByProductId(productId);
    }

    @Override
    public void deleteReturnRequestDetail(Integer id) {
        returnRequestDetailDao.delete(id);
    }

    @Override
    public void deleteDetailsByReturnRequestId(Integer returnRequestId) {
        returnRequestDetailDao.deleteByReturnRequestId(returnRequestId);
    }
} 