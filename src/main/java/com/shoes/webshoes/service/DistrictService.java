package com.shoes.webshoes.service;

import java.util.List;

import com.shoes.webshoes.entity.Districts;

public interface DistrictService {
	List<Districts> findByCityId(int cityId) throws Exception;
    Districts findById(int id) throws Exception;
}
