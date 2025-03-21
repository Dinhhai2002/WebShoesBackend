package com.shoes.webshoes.dao.Impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.CityDao;
import com.shoes.webshoes.entity.Cities;
@Repository("CityDao")
@Transactional
public class CityDaoImpl extends AbstractDao<Integer, Cities> implements CityDao {

	@Override
	public List<Cities> getAll() throws Exception {
		CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Cities> query = builder.createQuery(Cities.class);
		Root<Cities> root = query.from(Cities.class);
//		query.where(builder.equal(root.get("cityId"), cityId));

		return this.getSession().createQuery(query).getResultList();
	}

}
