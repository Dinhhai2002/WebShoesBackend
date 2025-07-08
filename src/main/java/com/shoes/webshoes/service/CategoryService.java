package com.shoes.webshoes.service;

import java.util.List;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.entity.Category;
import com.shoes.webshoes.model.StoreProcedureListResult;

public interface CategoryService {
	void create(Category category);

	Category findOne(int id);
	
	List<Category> getAll();

	void update(Category category);
	
	Category findByName(String name);

	List<Category> findByIds(List<Integer> ids);

	StoreProcedureListResult<Category> spGListCategory(
		int parentId,
		String keySearch,
		int status,
		Pagination pagination
	) throws Exception;
}
