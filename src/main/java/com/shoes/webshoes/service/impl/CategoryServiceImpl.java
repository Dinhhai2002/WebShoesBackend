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

/**
 * 
 * @author 
 *
 */
@Service("CategoryService")
@Transactional(rollbackFor = Error.class)
public class CategoryServiceImpl implements CategoryService {
    @Autowired
	CategoryDao categoryDao;

	@Override
	public void create(Category category) {
		categoryDao.create(category);
	}

	@Override
	public Category findOne(int id) {
		return categoryDao.findOne(id);
	}

	@Override
	public void update(Category category) {
		categoryDao.update(category);
	}

	@Override
	public Category findByName(String name) {
		return categoryDao.findByName(name);
	}

	@Override
	public List<Category> getAll() {
		return categoryDao.getAll();
	}

	@Override
	public StoreProcedureListResult<Category> spGListCategory(
			int parentId,
			String keySearch,
			int status,
			Pagination pagination) throws Exception {
		return categoryDao.spGListCategory(parentId, keySearch, status, pagination);
	}

    @Override
    public List<Category> findByIds(List<Integer> ids) {
        return categoryDao.findByIds(ids);
    }
}
