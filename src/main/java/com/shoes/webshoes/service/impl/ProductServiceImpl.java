package com.shoes.webshoes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.ProductDao;
import com.shoes.webshoes.entity.Product;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.service.ProductService;

@Service("ProductService")
@Transactional(rollbackFor = Error.class)
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

    @Override
    public void create(Product product) {
        productDao.create(product);
    }

    @Override
    public Product findOne(int id) {
        return productDao.findOne(id);
    }

    @Override
    public void update(Product product) {
        productDao.update(product);
    }

    @Override
    public List<Product> getAll() {
        return productDao.getAll();
    }

    @Override
    public Product findByName(String name) {
        return productDao.findByName(name);
    }

    @Override
	public StoreProcedureListResult<Product> spGListProduct(String keySearch, int status,
			Pagination pagination) throws Exception {
		return productDao.spGListProduct(keySearch, status, pagination);
	}
}
