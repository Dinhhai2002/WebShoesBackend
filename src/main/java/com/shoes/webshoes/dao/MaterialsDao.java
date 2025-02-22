package com.shoes.webshoes.dao;

import java.util.List;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.entity.Materials;

public interface MaterialsDao {
    void create(Materials materials);

    Materials findOne(int id);

    void update(Materials materials);

    List<Materials> getAll();

    StoreProcedureListResult<Materials> spGListMaterials(String keySearch,int status,Pagination pagination) throws Exception;

    Materials findByName(String name);
}