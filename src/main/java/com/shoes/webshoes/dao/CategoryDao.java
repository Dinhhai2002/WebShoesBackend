package com.shoes.webshoes.dao;

import java.util.List;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.entity.Category;
import com.shoes.webshoes.model.StoreProcedureListResult;

public interface CategoryDao {
    void create(Category category) throws Exception;

	Category findOne(int id) throws Exception;
	
	void update(Category category) throws Exception;

	List<Category> getAll() throws Exception;
	
	StoreProcedureListResult<Category> spGListCategory(String keySearch,int status,Pagination pagination) throws Exception;

	Category findByName(String name) throws Exception;
}
