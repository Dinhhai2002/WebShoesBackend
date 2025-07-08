package com.shoes.webshoes.service;

import java.util.List;

import com.shoes.webshoes.entity.Wards;

public interface WardsService {
	List<Wards> findByDistrictId(int districtId) throws Exception;
	Wards findById(int id) throws Exception;
}
