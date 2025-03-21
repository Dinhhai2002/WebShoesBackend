package com.shoes.webshoes.service;

import java.util.List;

import com.shoes.webshoes.entity.Cities;

public interface CityService {
	List<Cities> getAll() throws Exception;
}
