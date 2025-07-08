package com.shoes.webshoes.service;

import java.util.List;

public interface StatisticalService {
	List<Object> statisticalAmount(int numberWeek, String fromDate, String toDate, int type) throws Exception;
	
}
