package com.shoes.webshoes.service;

import java.util.List;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.entity.Cart;

public interface CartService {
    void create(Cart cart);

    Cart findOne(int id);

    void update(Cart cart);

    List<Cart> getAll();

    StoreProcedureListResult<Cart> spGListCart(int userId, String keySearch,int status,Pagination pagination) throws Exception;

    Cart findByName(String name);
}
