package com.shoes.webshoes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.CartDetailDao;
import com.shoes.webshoes.entity.CartDetail;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.service.CartDetailService;

@Service("CartDetailService")
@Transactional(rollbackFor = Error.class)
public class CartDetailServiceImpl implements CartDetailService {
    @Autowired
    private CartDetailDao cartDetailDao;

    @Override
    public void create(CartDetail cartDetail) {
        cartDetailDao.create(cartDetail);
    }

    @Override
    public CartDetail findOne(int id) {
        return cartDetailDao.findOne(id);
    }

    @Override
    public void update(CartDetail cartDetail) {
        cartDetailDao.update(cartDetail);
    }

    @Override
    public List<CartDetail> getAll() {
        return cartDetailDao.getAll();
    }

    @Override
    public CartDetail findByName(String name) {
        return cartDetailDao.findByName(name);
    }

    @Override
	public StoreProcedureListResult<CartDetail> spGListCartDetail(int cartId, String keySearch, int status,
			Pagination pagination) throws Exception {
		return cartDetailDao.spGListCartDetail(cartId, keySearch, status, pagination);
	}
}
