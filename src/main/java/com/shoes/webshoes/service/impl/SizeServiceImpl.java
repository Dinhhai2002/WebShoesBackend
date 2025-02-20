package com.shoes.webshoes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.SizeDao;
import com.shoes.webshoes.entity.Size;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.service.SizeService;

@Service("SizeService")
@Transactional(rollbackFor = Error.class)
public class SizeServiceImpl implements SizeService {
    @Autowired
    private SizeDao sizeDao;

    @Override
    public void create(Size size) {
        sizeDao.create(size);
    }

    @Override
    public Size findOne(int id) {
        return sizeDao.findOne(id);
    }

    @Override
    public void update(Size size) {
        sizeDao.update(size);
    }

    @Override
    public List<Size> getAll() {
        return sizeDao.getAll();
    }

    @Override
    public Size findByName(String name) {
        return sizeDao.findByName(name);
    }

    @Override
	public StoreProcedureListResult<Size> spGListSize(String keySearch, int status,
			Pagination pagination) throws Exception {
		return sizeDao.spGListSize(keySearch, status, pagination);
	}
}
