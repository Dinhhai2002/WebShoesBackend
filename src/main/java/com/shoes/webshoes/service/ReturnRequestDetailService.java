package com.shoes.webshoes.service;

import com.shoes.webshoes.entity.ReturnRequestDetail;
import com.shoes.webshoes.request.ReturnRequestDetailRequest;
import java.util.List;

public interface ReturnRequestDetailService {
    ReturnRequestDetail createReturnRequestDetail(ReturnRequestDetailRequest request);
    ReturnRequestDetail updateReturnRequestDetail(Integer id, ReturnRequestDetailRequest request);
    ReturnRequestDetail getReturnRequestDetailById(Integer id);
    List<ReturnRequestDetail> getDetailsByReturnRequestId(Integer returnRequestId);
    List<ReturnRequestDetail> getDetailsByProductDetailId(Integer productDetailId);
    List<ReturnRequestDetail> getDetailsByProductId(Integer productId);
    void deleteReturnRequestDetail(Integer id);
    void deleteDetailsByReturnRequestId(Integer returnRequestId);
} 