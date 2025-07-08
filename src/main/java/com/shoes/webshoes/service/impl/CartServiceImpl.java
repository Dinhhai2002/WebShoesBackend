package com.shoes.webshoes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.CartDao;
import com.shoes.webshoes.entity.Cart;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.service.CartService;

@Service("CartService")
@Transactional(rollbackFor = Error.class)
public class CartServiceImpl implements CartService {
    @Autowired
    private CartDao cartDao;

    @Override
    public void create(Cart cart) {
        cartDao.create(cart);
    }

    @Override
    public Cart findOne(int id) {
        return cartDao.findOne(id);
    }

    @Override
    public void update(Cart cart) {
        cartDao.update(cart);
    }

    @Override
    public List<Cart> getAll() {
        return cartDao.getAll();
    }

    @Override
    public Cart findByName(String name) {
        return cartDao.findByName(name);
    }

    @Override
	public StoreProcedureListResult<Cart> spGListCart(int userId, String keySearch, int status,
			Pagination pagination) throws Exception {
		return cartDao.spGListCart(userId, keySearch, status, pagination);
	}
}
