package com.shoes.webshoes.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.common.enums.StatusOrderEnum;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.CancelOrderHistoryDAO;
import com.shoes.webshoes.dao.CancelOrderRequestDAO;
import com.shoes.webshoes.entity.CancelOrderHistory;
import com.shoes.webshoes.entity.CancelOrderRequest;
import com.shoes.webshoes.entity.Order;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.ApproveCancelOrderRequest;
import com.shoes.webshoes.request.CRUDCancelOrderRequest;
import com.shoes.webshoes.request.RejectCancelOrderRequest;
import com.shoes.webshoes.service.CancelOrderService;
import com.shoes.webshoes.service.OrderService;

@Service("CancelOrderService")
@Transactional(rollbackFor = Error.class)
public class CancelOrderServiceImpl implements CancelOrderService {
    
    @Autowired
    private CancelOrderRequestDAO cancelOrderRequestDao;
    
    @Autowired
    private CancelOrderHistoryDAO cancelOrderHistoryDao;
    
    @Autowired
    private OrderService orderService;

    @Override
    public CancelOrderRequest createCancelRequest(CRUDCancelOrderRequest request) {
        // Kiểm tra đã tồn tại yêu cầu hủy với orderId chưa
        CancelOrderRequest existed = cancelOrderRequestDao.findByOrderId(request.getOrderId());
        if (existed != null) {
            throw new RuntimeException("Đã tồn tại yêu cầu hủy cho đơn hàng này!");
        }
        
        // Kiểm tra đơn hàng có tồn tại không
        var order = orderService.findOne(request.getOrderId());
        if (order == null) {
            throw new RuntimeException("Đơn hàng không tồn tại!");
        }
        
        // Tạo CancelOrderRequest
        CancelOrderRequest cancelRequest = new CancelOrderRequest();
        cancelRequest.setOrderId(request.getOrderId());
        cancelRequest.setUserId(request.getUserId());
        cancelRequest.setCancelReason(request.getCancelReason());
        cancelRequest.setStatus(CancelOrderRequest.CancelOrderStatus.PENDING);
        cancelRequest.setCreatedAt(new Date());
        cancelRequest.setUpdatedAt(new Date());
        
        CancelOrderRequest savedRequest = cancelOrderRequestDao.create(cancelRequest);
        
        // Tạo history
        createHistory(savedRequest.getId(), "PENDING", "Tạo yêu cầu hủy đơn hàng", request.getUserId());
        
        return savedRequest;
    }

    @Override
    public CancelOrderRequest approveCancelRequest(Integer id, ApproveCancelOrderRequest request) {
        CancelOrderRequest cancelRequest = cancelOrderRequestDao.findById(id);
        if (cancelRequest == null) {
            throw new RuntimeException("Không tìm thấy yêu cầu hủy đơn hàng!");
        }
        
        cancelRequest.setStatus(CancelOrderRequest.CancelOrderStatus.APPROVED);
        cancelRequest.setAdminNotes(request.getAdminNotes());
        cancelRequest.setUpdatedAt(new Date());
        
        // Cập nhật trạng thái đơn hàng thành CANCELLED
        Order order = orderService.findOne(cancelRequest.getOrderId());
        order.setStatus(StatusOrderEnum.CANCELLED.getValue());
        orderService.update(order);
        
        createHistory(id, "APPROVED", "Admin duyệt yêu cầu hủy đơn hàng", null);
        
        return cancelOrderRequestDao.update(cancelRequest);
    }

    @Override
    public CancelOrderRequest rejectCancelRequest(Integer id, RejectCancelOrderRequest request) {
        CancelOrderRequest cancelRequest = cancelOrderRequestDao.findById(id);
        if (cancelRequest == null) {
            throw new RuntimeException("Không tìm thấy yêu cầu hủy đơn hàng!");
        }
        
        cancelRequest.setStatus(CancelOrderRequest.CancelOrderStatus.REJECTED);
        cancelRequest.setAdminNotes(request.getAdminNotes());
        cancelRequest.setUpdatedAt(new Date());
        
        createHistory(id, "REJECTED", "Admin từ chối yêu cầu hủy đơn hàng", null);
        
        return cancelOrderRequestDao.update(cancelRequest);
    }

    @Override
    public CancelOrderRequest getCancelRequestById(Integer id) {
        return cancelOrderRequestDao.findById(id);
    }

    @Override
    public CancelOrderRequest getCancelRequestByOrderId(Integer orderId) {
        return cancelOrderRequestDao.findByOrderId(orderId);
    }

    @Override
    public List<CancelOrderRequest> getCancelRequestsByUserId(Integer userId) {
        return cancelOrderRequestDao.findByUserId(userId);
    }

    @Override
    public List<CancelOrderRequest> getCancelRequestsByStatus(String status) {
        return cancelOrderRequestDao.findByStatus(status);
    }

    @Override
    public List<CancelOrderRequest> getAllCancelRequests() {
        return cancelOrderRequestDao.findAll();
    }

    @Override
    public void deleteCancelRequest(Integer id) {
        // Xóa history trước
        cancelOrderHistoryDao.deleteByCancelRequestId(id);
        
        // Xóa cancel request
        cancelOrderRequestDao.delete(id);
    }
    
    @Override
    public StoreProcedureListResult<CancelOrderRequest> spGListCancelRequest(Integer userId, String keySearch, String status, 
            Pagination pagination) throws Exception {
        return cancelOrderRequestDao.spGListCancelRequest(userId, keySearch, status, pagination);
    }
    
    private void createHistory(Integer cancelRequestId, String status, String notes, Integer createdBy) {
        CancelOrderHistory history = new CancelOrderHistory();
        history.setCancelRequestId(cancelRequestId);
        history.setStatus(status);
        history.setNotes(notes);
        history.setCreatedBy(createdBy);
        history.setCreatedAt(new Date());
        
        cancelOrderHistoryDao.create(history);
    }
} 