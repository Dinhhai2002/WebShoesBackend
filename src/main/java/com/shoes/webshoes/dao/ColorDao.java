package com.shoes.webshoes.dao;

import java.util.List;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.entity.Color;

public interface ColorDao {
    void create(Color color);

    Color findOne(int id);

    void update(Color color);

    List<Color> getAll();

    StoreProcedureListResult<Color> spGListColor(String keySearch,int status,Pagination pagination) throws Exception;

    Color findByName(String name);
}