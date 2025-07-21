package com.shoes.webshoes.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shoes.webshoes.common.enums.ExchangeStatus;
import com.shoes.webshoes.dao.ExchangeRequestDAO;
import com.shoes.webshoes.dao.ExchangeRequestDetailDAO;
import com.shoes.webshoes.dao.ReturnRequestDAO;
import com.shoes.webshoes.entity.ExchangeRequest;
import com.shoes.webshoes.entity.ExchangeRequestDetail;
import com.shoes.webshoes.entity.ReturnRequest;
import com.shoes.webshoes.request.ExchangeRequestRequest;
import com.shoes.webshoes.request.ApproveExchangeRequestRequest;
import com.shoes.webshoes.request.RejectExchangeRequestRequest;
import com.shoes.webshoes.service.ExchangeRequestService;
import com.shoes.webshoes.service.ProductDetailService;
import com.google.gson.Gson;

@Service("ExchangeRequestService")
@Transactional(rollbackFor = Error.class)
public class ExchangeRequestServiceImpl implements ExchangeRequestService {
    
    @Autowired
    private ExchangeRequestDAO exchangeRequestDao;
    
    @Autowired
    private ExchangeRequestDetailDAO exchangeRequestDetailDao;
    
    @Autowired
    private ReturnRequestDAO returnRequestDao;
    
    @Autowired
    private ProductDetailService productDetailService;
    
    private final Gson gson = new Gson();

    @Override
    public ExchangeRequest createExchangeRequest(ExchangeRequestRequest request) {
        // Kiểm tra return request có tồn tại không
        ReturnRequest returnRequest = returnRequestDao.findById(request.getReturnRequestId());
        if (returnRequest == null) {
            throw new RuntimeException("Return request không tồn tại!");
        }
        
        // Kiểm tra return request có phải là loại đổi hàng không
        if (!returnRequest.getReturnType().isExchangeType()) {
            throw new RuntimeException("Return request này không phải là yêu cầu đổi hàng!");
        }
        
        // Kiểm tra đã có exchange request cho return request này chưa
        ExchangeRequest existed = exchangeRequestDao.findByReturnRequestId(request.getReturnRequestId());
        if (existed != null) {
            throw new RuntimeException("Đã có yêu cầu đổi hàng cho return request này!");
        }
        
        // Tạo ExchangeRequest
        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setReturnRequestId(request.getReturnRequestId());
        exchangeRequest.setExchangeReason(request.getExchangeReason());
        exchangeRequest.setStatus(ExchangeStatus.PENDING);
        exchangeRequest.setPriceDifference(request.getPriceDifference());
        exchangeRequest.setCreatedAt(new Date());
        exchangeRequest.setUpdatedAt(new Date());
        
        ExchangeRequest savedExchangeRequest = exchangeRequestDao.create(exchangeRequest);
        
        // Tạo ExchangeRequestDetails (chỉ có thông tin sản phẩm cũ)
        if (request.getDetails() != null) {
            for (var detailRequest : request.getDetails()) {
                ExchangeRequestDetail detail = new ExchangeRequestDetail();
                detail.setExchangeRequestId(savedExchangeRequest.getId());
                detail.setOldProductId(detailRequest.getOldProductId());
                detail.setOldProductDetailId(detailRequest.getOldProductDetailId());
                detail.setNewProductId(detailRequest.getNewProductId()); // Có thể null khi tạo ban đầu
                detail.setNewProductDetailId(detailRequest.getNewProductDetailId()); // Có thể null khi tạo ban đầu
                detail.setQuantity(detailRequest.getQuantity());
                detail.setExchangeReason(detailRequest.getExchangeReason());
                detail.setConditionDescription(detailRequest.getConditionDescription());
                detail.setImages(gson.toJson(detailRequest.getImages()));
                
                exchangeRequestDetailDao.create(detail);
            }
        }
        
        return savedExchangeRequest;
    }

    @Override
    public ExchangeRequest updateExchangeRequest(Integer id, ExchangeRequestRequest request) {
        ExchangeRequest exchangeRequest = exchangeRequestDao.findById(id);
        if (exchangeRequest == null) {
            throw new RuntimeException("Exchange request not found");
        }
        
        exchangeRequest.setExchangeReason(request.getExchangeReason());
        exchangeRequest.setUpdatedAt(new Date());
        
        return exchangeRequestDao.update(exchangeRequest);
    }

    @Override
    public ExchangeRequest getExchangeRequestById(Integer id) {
        return exchangeRequestDao.findById(id);
    }

    @Override
    public ExchangeRequest getExchangeRequestByReturnRequestId(Integer returnRequestId) {
        return exchangeRequestDao.findByReturnRequestId(returnRequestId);
    }

    @Override
    public List<ExchangeRequest> getAllExchangeRequests() {
        return exchangeRequestDao.findAll();
    }

    @Override
    public ExchangeRequest approveExchangeRequest(Integer id, ApproveExchangeRequestRequest request) {
        ExchangeRequest exchangeRequest = exchangeRequestDao.findById(id);
        if (exchangeRequest == null) {
            throw new RuntimeException("Exchange request not found");
        }
        
        // Kiểm tra sản phẩm mới có phù hợp không
        if (request.getExchangeProducts() != null) {
            for (var exchangeProduct : request.getExchangeProducts()) {
                var newProductDetail = productDetailService.findOne(exchangeProduct.getNewProductDetailId());
                if (newProductDetail == null) {
                    throw new RuntimeException("Sản phẩm mới không tồn tại!");
                }
                if (newProductDetail.getStock() < exchangeProduct.getQuantity()) {
                    throw new RuntimeException("Sản phẩm " + newProductDetail.getName() + " không đủ tồn kho!");
                }
            }
        }
        
        // Cập nhật thông tin sản phẩm mới vào exchange request details
        List<ExchangeRequestDetail> details = exchangeRequestDetailDao.findByExchangeRequestId(id);
        for (ExchangeRequestDetail detail : details) {
            // Tìm sản phẩm mới tương ứng
            for (var exchangeProduct : request.getExchangeProducts()) {
                if (detail.getOldProductDetailId().equals(exchangeProduct.getOldProductDetailId())) {
                    detail.setNewProductId(exchangeProduct.getNewProductId());
                    detail.setNewProductDetailId(exchangeProduct.getNewProductDetailId());
                    detail.setExchangeReason(exchangeProduct.getExchangeReason());
                    exchangeRequestDetailDao.update(detail);
                    break;
                }
            }
        }
        
        // Xử lý tồn kho
        for (var exchangeProduct : request.getExchangeProducts()) {
            // Cộng lại tồn kho sản phẩm cũ
            var oldProductDetail = productDetailService.findOne(exchangeProduct.getOldProductDetailId());
            if (oldProductDetail != null) {
                oldProductDetail.setStock(oldProductDetail.getStock() + exchangeProduct.getQuantity());
                productDetailService.update(oldProductDetail);
            }
            
            // Trừ tồn kho sản phẩm mới
            var newProductDetail = productDetailService.findOne(exchangeProduct.getNewProductDetailId());
            if (newProductDetail != null) {
                newProductDetail.setStock(newProductDetail.getStock() - exchangeProduct.getQuantity());
                productDetailService.update(newProductDetail);
            }
        }
        
        // Cập nhật trạng thái
        exchangeRequest.setStatus(ExchangeStatus.APPROVED);
        exchangeRequest.setAdminNotes(request.getAdminNotes());
        exchangeRequest.setPriceDifference(request.getPriceDifference());
        exchangeRequest.setUpdatedAt(new Date());
        
        return exchangeRequestDao.update(exchangeRequest);
    }

    @Override
    public ExchangeRequest rejectExchangeRequest(Integer id, RejectExchangeRequestRequest request) {
        ExchangeRequest exchangeRequest = exchangeRequestDao.findById(id);
        if (exchangeRequest == null) {
            throw new RuntimeException("Exchange request not found");
        }
        
        exchangeRequest.setStatus(ExchangeStatus.REJECTED);
        exchangeRequest.setAdminNotes(request.getAdminNotes());
        exchangeRequest.setUpdatedAt(new Date());
        
        return exchangeRequestDao.update(exchangeRequest);
    }

    @Override
    public ExchangeRequest processExchangeRequest(Integer id) {
        ExchangeRequest exchangeRequest = exchangeRequestDao.findById(id);
        if (exchangeRequest == null) {
            throw new RuntimeException("Exchange request not found");
        }
        
        exchangeRequest.setStatus(ExchangeStatus.PROCESSING);
        exchangeRequest.setUpdatedAt(new Date());
        
        return exchangeRequestDao.update(exchangeRequest);
    }

    @Override
    public ExchangeRequest completeExchangeRequest(Integer id) {
        ExchangeRequest exchangeRequest = exchangeRequestDao.findById(id);
        if (exchangeRequest == null) {
            throw new RuntimeException("Exchange request not found");
        }
        
        exchangeRequest.setStatus(ExchangeStatus.COMPLETED);
        exchangeRequest.setUpdatedAt(new Date());
        
        return exchangeRequestDao.update(exchangeRequest);
    }

    @Override
    public void cancelExchangeRequest(Integer id) {
        ExchangeRequest exchangeRequest = exchangeRequestDao.findById(id);
        if (exchangeRequest == null) {
            throw new RuntimeException("Exchange request not found");
        }
        
        exchangeRequest.setStatus(ExchangeStatus.CANCELLED);
        exchangeRequest.setUpdatedAt(new Date());
        
        exchangeRequestDao.update(exchangeRequest);
    }

    @Override
    public void deleteExchangeRequest(Integer id) {
        // Xóa details trước
        exchangeRequestDetailDao.deleteByExchangeRequestId(id);
        
        // Xóa exchange request
        exchangeRequestDao.delete(id);
    }
} 