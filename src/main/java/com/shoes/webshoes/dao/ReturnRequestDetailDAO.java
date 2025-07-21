package com.shoes.webshoes.dao;

import com.shoes.webshoes.entity.ReturnRequestDetail;
import java.util.List;

public interface ReturnRequestDetailDAO {
    ReturnRequestDetail create(ReturnRequestDetail returnRequestDetail);
    ReturnRequestDetail update(ReturnRequestDetail returnRequestDetail);
    ReturnRequestDetail findById(Integer id);
    List<ReturnRequestDetail> findByReturnRequestId(Integer returnRequestId);
    List<ReturnRequestDetail> findByProductDetailId(Integer productDetailId);
    List<ReturnRequestDetail> findByProductId(Integer productId);
    void delete(Integer id);
    void deleteByReturnRequestId(Integer returnRequestId);
} 