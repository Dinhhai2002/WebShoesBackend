package com.shoes.webshoes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.ColorDao;
import com.shoes.webshoes.entity.Color;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.service.ColorService;

@Service("ColorService")
@Transactional(rollbackFor = Error.class)
public class ColorServiceImpl implements ColorService {
    @Autowired
    private ColorDao colorDao;

    @Override
    public void create(Color color) {
        colorDao.create(color);
    }

    @Override
    public Color findOne(int id) {
        return colorDao.findOne(id);
    }

    @Override
    public void update(Color color) {
        colorDao.update(color);
    }

    @Override
    public List<Color> getAll() {
        return colorDao.getAll();
    }

    @Override
    public Color findByName(String name) {
        return colorDao.findByName(name);
    }

    @Override
	public StoreProcedureListResult<Color> spGListColor(String keySearch, int status,
			Pagination pagination) throws Exception {
		return colorDao.spGListColor(keySearch, status, pagination);
	}
}
