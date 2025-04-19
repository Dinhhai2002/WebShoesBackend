package com.shoes.webshoes.dao.Impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.DistrictDao;
import com.shoes.webshoes.entity.Districts;

@Repository("DistrictDao")
@Transactional
public class DistrictDaoImpl extends AbstractDao<Integer, Districts> implements DistrictDao {

	@Override
	public List<Districts> findByCityId(int cityId) throws Exception {
		CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Districts> query = builder.createQuery(Districts.class);
		Root<Districts> root = query.from(Districts.class);
		query.where(builder.equal(root.get("cityId"), cityId));

		return (List<Districts>) this.getSession().createQuery(query).getResultList();
	}

    @Override
    public Districts findById(int id) throws Exception {
    	return this.getSession().find(Districts.class, id);
    }
}
