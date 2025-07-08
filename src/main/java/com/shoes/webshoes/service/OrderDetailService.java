package com.shoes.webshoes.service;

import java.util.List;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.entity.OrderDetail;

public interface OrderDetailService {
    void create(OrderDetail orderDetail);

    OrderDetail findOne(int id);

    void update(OrderDetail orderDetail);

    List<OrderDetail> getAll();

    StoreProcedureListResult<OrderDetail> spGListOrderDetail(int orderId, String keySearch,int status,Pagination pagination) throws Exception;

    OrderDetail findByName(String name);
}
