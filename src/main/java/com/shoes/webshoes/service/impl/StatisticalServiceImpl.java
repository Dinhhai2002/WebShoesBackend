package com.shoes.webshoes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.dao.StatisticalDao;
import com.shoes.webshoes.service.StatisticalService;

@Service("StatisticalService")
@Transactional(rollbackFor = Error.class)
public class StatisticalServiceImpl implements StatisticalService {

	@Autowired
	StatisticalDao statisticalDao;

	@Override
	public List<Object> statisticalAmount(int numberWeek, String fromDate, String toDate, int type) throws Exception {
		return statisticalDao.statisticalAmount(numberWeek, fromDate, toDate, type);
	}

}
