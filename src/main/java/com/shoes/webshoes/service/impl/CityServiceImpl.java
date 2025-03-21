package com.shoes.webshoes.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoes.webshoes.dao.CityDao;
import com.shoes.webshoes.entity.Cities;
import com.shoes.webshoes.service.CityService;

@Service("CityService")
@Transactional(rollbackOn = Error.class)
public class CityServiceImpl implements CityService {
	@Autowired
	CityDao cityDao;

	@Override
	public List<Cities> getAll() throws Exception {
		return cityDao.getAll();
	}

}
