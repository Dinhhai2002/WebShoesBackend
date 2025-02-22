package com.shoes.webshoes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.dao.ImageDao;
import com.shoes.webshoes.entity.Image;
import com.shoes.webshoes.service.ImageService;

@Service("ImageService")
@Transactional(rollbackFor = Error.class)
public class ImageServiceImpl implements ImageService {
	
	@Autowired
	ImageDao imageDao;

	@Override
	public Image findOne(int id) throws Exception {
		// TODO Auto-generated method stub
		return imageDao.findOne(id);
	}

	@Override
	public void update(Image image) throws Exception {
		imageDao.update(image);
		
	}

	@Override
	public Image create(Image image) throws Exception {
		return imageDao.create(image);
	}

	@Override
	public void delete(Image image) {
		imageDao.delete(image);
		
	}

}
