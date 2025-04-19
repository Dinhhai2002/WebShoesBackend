package com.shoes.webshoes.dao;

import java.util.List;

import com.shoes.webshoes.entity.Districts;

public interface DistrictDao {
	List<Districts> findByCityId(int cityId) throws Exception;
    Districts findById(int id) throws Exception;
}
