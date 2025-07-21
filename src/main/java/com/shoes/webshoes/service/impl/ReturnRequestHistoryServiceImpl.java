package com.shoes.webshoes.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shoes.webshoes.dao.ReturnRequestHistoryDAO;
import com.shoes.webshoes.entity.ReturnRequestHistory;
import com.shoes.webshoes.service.ReturnRequestHistoryService;

@Service("ReturnRequestHistoryService")
@Transactional(rollbackFor = Error.class)
public class ReturnRequestHistoryServiceImpl implements ReturnRequestHistoryService {
    
    @Autowired
    private ReturnRequestHistoryDAO returnRequestHistoryDao;

    @Override
    public ReturnRequestHistory createHistory(Integer returnRequestId, String status, String notes, Integer createdBy) {
        ReturnRequestHistory history = new ReturnRequestHistory();
        history.setReturnRequestId(returnRequestId);
        history.setStatus(status);
        history.setNotes(notes);
        history.setCreatedBy(createdBy);
        history.setCreatedAt(new Date());
        
        return returnRequestHistoryDao.create(history);
    }

    @Override
    public ReturnRequestHistory getHistoryById(Integer id) {
        return returnRequestHistoryDao.findById(id);
    }

    @Override
    public List<ReturnRequestHistory> getHistoryByReturnRequestId(Integer returnRequestId) {
        return returnRequestHistoryDao.findByReturnRequestId(returnRequestId);
    }

    @Override
    public List<ReturnRequestHistory> getHistoryByStatus(String status) {
        return returnRequestHistoryDao.findByStatus(status);
    }
} 