package com.shoes.webshoes.dao.Impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.WardsDao;
import com.shoes.webshoes.entity.Wards;

@Repository("wardsDao")
@Transactional
public class wardsDaoImpl extends AbstractDao<Integer, Wards> implements WardsDao {

	@Override
	public List<Wards> findByDistrictId(int districtId) throws Exception {
		CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Wards> query = builder.createQuery(Wards.class);
		Root<Wards> root = query.from(Wards.class);
		query.where(builder.equal(root.get("districtId"), districtId));

		return (List<Wards>) this.getSession().createQuery(query).getResultList();
	}

    @Override
    public Wards findById(int id) throws Exception {
    	return this.getSession().find(Wards.class, id);
    }

}
