package com.shoes.webshoes.dao;

import com.shoes.webshoes.entity.Image;

public interface ImageDao {
	Image findOne(int id) throws Exception;

	void update(Image image) throws Exception;
	
	Image create(Image image) throws Exception;
	
	void delete(Image image) ;
}
