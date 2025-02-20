package com.shoes.webshoes.service;

import java.util.List;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.entity.Size;

public interface SizeService {
    void create(Size size);

    Size findOne(int id);

    void update(Size size);

    List<Size> getAll();

    StoreProcedureListResult<Size> spGListSize(String keySearch,int status,Pagination pagination) throws Exception;

    Size findByName(String name);
}
