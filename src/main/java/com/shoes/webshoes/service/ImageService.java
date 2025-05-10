package com.shoes.webshoes.service;

import com.shoes.webshoes.entity.Image;

public interface ImageService {
	Image findOne(int id) throws Exception;

	void update(Image image) throws Exception;
	
	Image create(Image image) throws Exception;
	
	void delete(Image image) ;

}
