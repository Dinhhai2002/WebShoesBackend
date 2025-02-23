package com.shoes.webshoes.service;

import java.util.List;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.entity.Product;

public interface ProductService {
    void create(Product product);

    Product findOne(int id);

    void update(Product product);

    List<Product> getAll();

    StoreProcedureListResult<Product> spGListProduct(String keySearch,int status,Pagination pagination) throws Exception;

    Product findByName(String name);
}
