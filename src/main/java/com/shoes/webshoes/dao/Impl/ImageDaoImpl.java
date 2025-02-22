package com.shoes.webshoes.dao.Impl;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.ImageDao;
import com.shoes.webshoes.entity.Image;

@Repository("ImageDao")
@Transactional
public class ImageDaoImpl extends AbstractDao<Integer, Image> implements ImageDao {

	@Override
	public Image findOne(int id) throws Exception {
		return this.getSession().find(Image.class, id);
	}

	@Override
	public void update(Image image) throws Exception {
		this.getSession().update(image);
		
	}
	
	@Override
	public void delete(Image image)  {
		this.getSession().delete(image);
	}

	@Override
	public Image create(Image image) throws Exception {
		this.getSession().save(image);
		return  image;
		
	}

}
