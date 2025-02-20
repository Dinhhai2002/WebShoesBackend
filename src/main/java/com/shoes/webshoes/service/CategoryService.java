package com.shoes.webshoes.service;

import java.util.List;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.entity.Category;
import com.shoes.webshoes.model.StoreProcedureListResult;

public interface CategoryService {
	void create(Category Category) throws Exception;

	Category findOne(int id) throws Exception;
	
	List<Category> getAll() throws Exception;

	void update(Category Category) throws Exception;
	
	Category findByName(String name) throws Exception;

	StoreProcedureListResult<Category> spGListCategory(String keySearch,int status,Pagination pagination) throws Exception;
}

