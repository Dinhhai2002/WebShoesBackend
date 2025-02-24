package com.shoes.webshoes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.ProductDetailDao;
import com.shoes.webshoes.entity.ProductDetail;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.service.ProductDetailService;

@Service("ProductDetailService")
@Transactional(rollbackFor = Error.class)
public class ProductDetailServiceImpl implements ProductDetailService {
    @Autowired
    private ProductDetailDao productDetailDao;

    @Override
    public void create(ProductDetail productDetail) {
        productDetailDao.create(productDetail);
    }

    @Override
    public ProductDetail findOne(int id) {
        return productDetailDao.findOne(id);
    }

    @Override
    public void update(ProductDetail productDetail) {
        productDetailDao.update(productDetail);
    }

    @Override
    public List<ProductDetail> getAll() {
        return productDetailDao.getAll();
    }

    @Override
    public ProductDetail findByName(String name) {
        return productDetailDao.findByName(name);
    }

    @Override
	public StoreProcedureListResult<ProductDetail> spGListProductDetail(int productId, int colorId, int sizeId, int materialId, String keySearch, int status,
			Pagination pagination) throws Exception {
		return productDetailDao.spGListProductDetail(productId, colorId, sizeId, materialId, keySearch, status, pagination);
	}
}
