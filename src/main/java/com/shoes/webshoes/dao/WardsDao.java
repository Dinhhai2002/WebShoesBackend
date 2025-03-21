package com.shoes.webshoes.dao;

import java.util.List;

import com.shoes.webshoes.entity.Wards;

public interface WardsDao {
	List<Wards> findByDistrictId(int districtId) throws Exception;
}
