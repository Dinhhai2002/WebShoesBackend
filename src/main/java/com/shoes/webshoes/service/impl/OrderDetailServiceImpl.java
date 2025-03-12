package com.shoes.webshoes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.OrderDetailDao;
import com.shoes.webshoes.entity.OrderDetail;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.service.OrderDetailService;

@Service("OrderDetailService")
@Transactional(rollbackFor = Error.class)
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailDao orderDetailDao;

    @Override
    public void create(OrderDetail orderDetail) {
        orderDetailDao.create(orderDetail);
    }

    @Override
    public OrderDetail findOne(int id) {
        return orderDetailDao.findOne(id);
    }

    @Override
    public void update(OrderDetail orderDetail) {
        orderDetailDao.update(orderDetail);
    }

    @Override
    public List<OrderDetail> getAll() {
        return orderDetailDao.getAll();
    }

    @Override
    public OrderDetail findByName(String name) {
        return orderDetailDao.findByName(name);
    }

    @Override
	public StoreProcedureListResult<OrderDetail> spGListOrderDetail(int orderId, String keySearch, int status,
			Pagination pagination) throws Exception {
		return orderDetailDao.spGListOrderDetail(orderId, keySearch, status, pagination);
	}
}
