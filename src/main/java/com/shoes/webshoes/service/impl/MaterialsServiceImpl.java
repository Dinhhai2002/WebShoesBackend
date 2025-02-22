package com.shoes.webshoes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.MaterialsDao;
import com.shoes.webshoes.entity.Materials;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.service.MaterialsService;

@Service("MaterialsService")
@Transactional(rollbackFor = Error.class)
public class MaterialsServiceImpl implements MaterialsService {
    @Autowired
    private MaterialsDao materialsDao;

    @Override
    public void create(Materials materials) {
        materialsDao.create(materials);
    }

    @Override
    public Materials findOne(int id) {
        return materialsDao.findOne(id);
    }

    @Override
    public void update(Materials materials) {
        materialsDao.update(materials);
    }

    @Override
    public List<Materials> getAll() {
        return materialsDao.getAll();
    }

    @Override
    public Materials findByName(String name) {
        return materialsDao.findByName(name);
    }

    @Override
	public StoreProcedureListResult<Materials> spGListMaterials(String keySearch, int status,
			Pagination pagination) throws Exception {
		return materialsDao.spGListMaterials(keySearch, status, pagination);
	}
}
