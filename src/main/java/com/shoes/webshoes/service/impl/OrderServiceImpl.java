package com.shoes.webshoes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.OrderDao;
import com.shoes.webshoes.entity.Order;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.service.OrderService;

@Service("OrderService")
@Transactional(rollbackFor = Error.class)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Override
    public void create(Order order) {
        orderDao.create(order);
    }

    @Override
    public Order findOne(int id) {
        return orderDao.findOne(id);
    }

    @Override
    public void update(Order order) {
        orderDao.update(order);
    }

    @Override
    public List<Order> getAll() {
        return orderDao.getAll();
    }

    @Override
    public Order findByName(String name) {
        return orderDao.findByName(name);
    }

    @Override
    public StoreProcedureListResult<Order> spGListOrder(int userId, String keySearch, int status, int paymentStatus, int paymentMethod,
            Pagination pagination) throws Exception {
        return orderDao.spGListOrder(userId, keySearch, status, paymentStatus, paymentMethod, pagination);
    }

    @Override
    public StoreProcedureListResult<Order> findByPaymentStatuses(List<Integer> paymentStatuses, Pagination pagination) throws Exception {
        return orderDao.findByPaymentStatuses(paymentStatuses, pagination);
    }
}
