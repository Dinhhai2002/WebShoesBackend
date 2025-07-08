package com.shoes.webshoes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.dao.WardsDao;
import com.shoes.webshoes.entity.Wards;
import com.shoes.webshoes.service.WardsService;

@Service("WardsService")
@Transactional(rollbackFor = Error.class)
public class WardsServiceImpl implements WardsService{
	
	@Autowired
	WardsDao wardsDao;

	@Override
	public List<Wards> findByDistrictId(int districtId) throws Exception {
		return wardsDao.findByDistrictId(districtId);
	}

	@Override
	public Wards findById(int id) throws Exception {
		return wardsDao.findById(id);
	}

}
