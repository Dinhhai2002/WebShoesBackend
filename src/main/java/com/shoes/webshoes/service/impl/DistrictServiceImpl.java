package com.shoes.webshoes.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoes.webshoes.dao.DistrictDao;
import com.shoes.webshoes.entity.Districts;
import com.shoes.webshoes.service.DistrictService;

@Service("DistrictService")
@Transactional(rollbackOn = Error.class )
public class DistrictServiceImpl implements DistrictService {
	
	@Autowired
	DistrictDao districtDao;
	
	@Override
	public List<Districts> findByCityId(int cityId) throws Exception {
		return districtDao.findByCityId(cityId);
	}

}
