package com.shoes.webshoes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.CategoryDao;
import com.shoes.webshoes.entity.Category;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.service.CategoryService;


@Service("CategoryService")
@Transactional(rollbackFor = Error.class)
public class CategoryServiceImpl implements CategoryService {
    @Autowired
	CategoryDao CategoryDao;

	@Override
	public void create(Category category) throws Exception {
		CategoryDao.create(category);

	}

	@Override
	public Category findOne(int id) throws Exception {
		return CategoryDao.findOne(id);
	}

	@Override
	public void update(Category category) throws Exception {
			CategoryDao.update(category);
	}

	@Override
	public Category findByName(String name) throws Exception {
		return CategoryDao.findByName(name);
	}

	@Override
	public List<Category> getAll() throws Exception {
		return CategoryDao.getAll();
	}

	@Override
	public StoreProcedureListResult<Category> spGListCategory(String keySearch, int status,
			Pagination pagination) throws Exception {
		return CategoryDao.spGListCategory(keySearch, status, pagination);
	}
}
