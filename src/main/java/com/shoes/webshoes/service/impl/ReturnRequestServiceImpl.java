package com.shoes.webshoes.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shoes.webshoes.common.enums.ReturnStatus;
import com.shoes.webshoes.common.enums.ReturnType;
import com.shoes.webshoes.dao.ReturnRequestDAO;
import com.shoes.webshoes.dao.ReturnRequestDetailDAO;
import com.shoes.webshoes.dao.ReturnRequestHistoryDAO;
import com.shoes.webshoes.entity.ReturnRequest;
import com.shoes.webshoes.entity.ReturnRequestDetail;
import com.shoes.webshoes.entity.ReturnRequestHistory;
import com.shoes.webshoes.request.ReturnRequestRequest;
import com.shoes.webshoes.request.ApproveReturnRequestRequest;
import com.shoes.webshoes.request.RejectReturnRequestRequest;
import com.shoes.webshoes.service.ReturnRequestService;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.google.gson.Gson;
import com.shoes.webshoes.service.OrderService;
import com.shoes.webshoes.service.ProductDetailService;
import com.shoes.webshoes.common.enums.ExchangeStatus;
import com.shoes.webshoes.dao.ExchangeRequestDAO;
import com.shoes.webshoes.dao.ExchangeRequestDetailDAO;
import com.shoes.webshoes.entity.ExchangeRequest;
import com.shoes.webshoes.entity.ExchangeRequestDetail;

@Service("ReturnRequestService")
@Transactional(rollbackFor = Error.class)
public class ReturnRequestServiceImpl implements ReturnRequestService {
    
    @Autowired
    private ReturnRequestDAO returnRequestDao;
    
    @Autowired
    private ReturnRequestDetailDAO returnRequestDetailDao;
    
    @Autowired
    private ReturnRequestHistoryDAO returnRequestHistoryDao;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ProductDetailService productDetailService;
    
    @Autowired
    private ExchangeRequestDAO exchangeRequestDao;
    
    @Autowired
    private ExchangeRequestDetailDAO exchangeRequestDetailDao;
    
    private final Gson gson = new Gson();

    @Override
    public ReturnRequest createReturnRequest(ReturnRequestRequest request) {
        // Kiểm tra đã tồn tại yêu cầu trả hàng với userId và orderId chưa
        ReturnRequest existed = returnRequestDao.findByUserIdAndOrderId(request.getUserId(), request.getOrderId());
        if (existed != null) {
            throw new RuntimeException("Bạn đã tạo yêu cầu trả hàng cho đơn hàng này rồi!");
        }
        
        // Kiểm tra đơn hàng có tồn tại không
        var order = orderService.findOne(request.getOrderId());
        if (order == null) {
            throw new RuntimeException("Đơn hàng không tồn tại!");
        }
        
        // Kiểm tra ngày mua hàng còn trong 15 ngày không
        Date now = new Date();
        long diff = now.getTime() - order.getCreatedAt().getTime();
        long days = diff / (1000 * 60 * 60 * 24);
        if (days > 15) {
            throw new RuntimeException("Đơn hàng đã quá hạn 15 ngày, không thể tạo yêu cầu trả hàng!");
        }
        
        // Tạo ReturnRequest
        ReturnRequest returnRequest = new ReturnRequest();
        returnRequest.setOrderId(request.getOrderId());
        returnRequest.setUserId(request.getUserId());
        returnRequest.setReturnReason(request.getReturnReason());
        returnRequest.setReturnType(ReturnType.valueOf(request.getReturnType()));
        returnRequest.setStatus(ReturnStatus.PENDING);
        returnRequest.setCreatedAt(new Date());
        returnRequest.setUpdatedAt(new Date());
        
        ReturnRequest savedReturnRequest = returnRequestDao.create(returnRequest);
        
        // Tạo ReturnRequestDetails
        if (request.getDetails() != null) {
            for (var detailRequest : request.getDetails()) {
                ReturnRequestDetail detail = new ReturnRequestDetail();
                detail.setReturnRequestId(savedReturnRequest.getId());
                detail.setProductId(detailRequest.getProductId());
                detail.setProductDetailId(detailRequest.getProductDetailId());
                detail.setPrice(detailRequest.getPrice());
                detail.setQuantity(detailRequest.getQuantity());
                detail.setReturnReason(detailRequest.getReturnReason());
                detail.setConditionDescription(detailRequest.getConditionDescription());
                detail.setImages(gson.toJson(detailRequest.getImages()));
                
                returnRequestDetailDao.create(detail);
            }
        }
        
        // Tạo history
        createHistory(savedReturnRequest.getId(), ReturnStatus.PENDING.name(), "Tạo yêu cầu " + request.getReturnType().toLowerCase(), request.getUserId());
        
        return savedReturnRequest;
    }

    @Override
    public ReturnRequest updateReturnRequest(Integer id, ReturnRequestRequest request) {
        ReturnRequest returnRequest = returnRequestDao.findById(id);
        if (returnRequest == null) {
            throw new RuntimeException("Return request not found");
        }
        
        returnRequest.setReturnReason(request.getReturnReason());
        returnRequest.setReturnType(ReturnType.valueOf(request.getReturnType()));
        returnRequest.setUpdatedAt(new Date());
        
        return returnRequestDao.update(returnRequest);
    }

    @Override
    public ReturnRequest getReturnRequestById(Integer id) {
        return returnRequestDao.findById(id);
    }

    @Override
    public List<ReturnRequest> getReturnRequestsByUserId(Integer userId) {
        return returnRequestDao.findByUserId(userId);
    }

    @Override
    public List<ReturnRequest> getReturnRequestsByOrderId(Integer orderId) {
        return returnRequestDao.findByOrderId(orderId);
    }

    @Override
    public List<ReturnRequest> getReturnRequestsByStatus(String status) {
        return returnRequestDao.findByStatus(status);
    }

    @Override
    public List<ReturnRequest> getAllReturnRequests() {
        return returnRequestDao.findAll();
    }

    @Override
    public ReturnRequest approveReturnRequest(Integer id, ApproveReturnRequestRequest request) {
        ReturnRequest returnRequest = returnRequestDao.findById(id);
        if (returnRequest == null) {
            throw new RuntimeException("Return request not found");
        }
        
        returnRequest.setStatus(ReturnStatus.APPROVED);
        returnRequest.setAdminNotes(request.getAdminNotes());
        returnRequest.setUpdatedAt(new Date());
        
        createHistory(id, ReturnStatus.APPROVED.name(), "Admin duyệt yêu cầu " + returnRequest.getReturnType().name().toLowerCase(), null);
        
        // Xử lý tồn kho dựa trên loại trả hàng
//        if (returnRequest.getReturnType().isReturnType()) {
//            // Hoàn trả hàng: cộng lại tồn kho cho sản phẩm
//            List<ReturnRequestDetail> details = returnRequestDetailDao.findByReturnRequestId(id);
//            for (ReturnRequestDetail detail : details) {
//                var productDetail = productDetailService.findOne(detail.getProductDetailId());
//                if (productDetail != null) {
//                    productDetail.setStock(productDetail.getStock() + detail.getQuantity());
//                    productDetailService.update(productDetail);
//                }
//            }
//        }
        // Đổi hàng sẽ được xử lý riêng trong ExchangeRequestService
        
        return returnRequestDao.update(returnRequest);
    }

    @Override
    public ReturnRequest rejectReturnRequest(Integer id, RejectReturnRequestRequest request) {
        ReturnRequest returnRequest = returnRequestDao.findById(id);
        if (returnRequest == null) {
            throw new RuntimeException("Return request not found");
        }
        
        returnRequest.setStatus(ReturnStatus.REJECTED);
        returnRequest.setAdminNotes(request.getAdminNotes());
        returnRequest.setUpdatedAt(new Date());
        
        createHistory(id, ReturnStatus.REJECTED.name(), "Admin từ chối yêu cầu trả hàng", null);
        
        return returnRequestDao.update(returnRequest);
    }

    @Override
    public ReturnRequest processReturnRequest(Integer id) {
        ReturnRequest returnRequest = returnRequestDao.findById(id);
        if (returnRequest == null) {
            throw new RuntimeException("Return request not found");
        }
        
        returnRequest.setStatus(ReturnStatus.PROCESSING);
        returnRequest.setUpdatedAt(new Date());
        
        createHistory(id, ReturnStatus.PROCESSING.name(), "Bắt đầu xử lý trả hàng", null);
        
        return returnRequestDao.update(returnRequest);
    }

    @Override
    public ReturnRequest completeReturnRequest(Integer id) {
        ReturnRequest returnRequest = returnRequestDao.findById(id);
        if (returnRequest == null) {
            throw new RuntimeException("Return request not found");
        }
        
        returnRequest.setStatus(ReturnStatus.COMPLETED);
        returnRequest.setUpdatedAt(new Date());
        
        createHistory(id, ReturnStatus.COMPLETED.name(), "Hoàn thành trả hàng", null);
        
        return returnRequestDao.update(returnRequest);
    }

    @Override
    public void cancelReturnRequest(Integer id) {
        ReturnRequest returnRequest = returnRequestDao.findById(id);
        if (returnRequest == null) {
            throw new RuntimeException("Return request not found");
        }
        
        returnRequest.setStatus(ReturnStatus.CANCELLED);
        returnRequest.setUpdatedAt(new Date());
        
        createHistory(id, ReturnStatus.CANCELLED.name(), "Hủy yêu cầu trả hàng", returnRequest.getUserId());
        
        returnRequestDao.update(returnRequest);
    }

    @Override
    public void deleteReturnRequest(Integer id) {
        // Xóa details trước
        returnRequestDetailDao.deleteByReturnRequestId(id);
        
        // Xóa history
        List<ReturnRequestHistory> histories = returnRequestHistoryDao.findByReturnRequestId(id);
        for (ReturnRequestHistory history : histories) {
            // Note: Không có method delete trong DAO, cần thêm nếu cần
        }
        
        // Xóa return request
        returnRequestDao.delete(id);
    }
    
    @Override
    public StoreProcedureListResult<ReturnRequest> spGListReturnRequest(Integer userId, String keySearch, String status, 
    		Pagination pagination) throws Exception {
        return returnRequestDao.spGListReturnRequest(userId, keySearch, status, pagination);
    }
    
    private void createHistory(Integer returnRequestId, String status, String notes, Integer createdBy) {
        ReturnRequestHistory history = new ReturnRequestHistory();
        history.setReturnRequestId(returnRequestId);
        history.setStatus(status);
        history.setNotes(notes);
        history.setCreatedBy(createdBy);
        history.setCreatedAt(new Date());
        
        returnRequestHistoryDao.create(history);
    }
} 