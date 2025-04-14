package com.shoes.webshoes.service;

import java.util.List;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.entity.Order;

public interface OrderService {
    void create(Order order);

    Order findOne(int id);

    void update(Order order);

    List<Order> getAll();

    StoreProcedureListResult<Order> spGListOrder(int userId, String keySearch, int status, int paymentStatus, int paymentMethod, Pagination pagination) throws Exception;

    Order findByName(String name);
}
