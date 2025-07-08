package com.shoes.webshoes.dao;

import java.util.List;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.entity.Category;
import com.shoes.webshoes.model.StoreProcedureListResult;

public interface CategoryDao {
    void create(Category category);

	Category findOne(int id);
	
	void update(Category category);

	List<Category> getAll();
	
	StoreProcedureListResult<Category> spGListCategory(
        int parentId,
        String keySearch,
        int status,
        Pagination pagination
    ) throws Exception;

	Category findByName(String name);
    
    List<Category> findByIds(List<Integer> ids);
}
