package com.shoes.webshoes.dao;

import java.util.List;

import com.shoes.webshoes.entity.Cities;

public interface CityDao {
	List<Cities> getAll() throws Exception;
}
