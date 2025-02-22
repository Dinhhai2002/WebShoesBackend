package com.shoes.webshoes.dao;

import java.util.List;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.entity.Brand;

public interface BrandDao {
    void create(Brand brand);

    Brand findOne(int id);

    void update(Brand brand);

    List<Brand> getAll();

    StoreProcedureListResult<Brand> spGListBrand(String keySearch,int status,Pagination pagination) throws Exception;

    Brand findByName(String name);
}