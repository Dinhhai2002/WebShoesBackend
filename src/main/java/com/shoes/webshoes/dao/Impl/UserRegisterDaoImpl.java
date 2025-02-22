package com.shoes.webshoes.dao.Impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.UserRegisterDao;
import com.shoes.webshoes.entity.UserRegister;

@Repository("UserRegisterDao")
@Transactional
public class UserRegisterDaoImpl extends AbstractDao<Integer, UserRegister> implements UserRegisterDao {

	@Override
	public void create(UserRegister userRegister) throws Exception {
		this.getSession().save(userRegister);
	}

	@Override
	public UserRegister findOne(int id) throws Exception {
		return this.getSession().find(UserRegister.class, id);
	}

	@Override
	public void update(UserRegister userRegister) throws Exception {
		this.getSession().update(userRegister);

	}

	@Override
	public void delete(UserRegister userRegister) {
		this.getSession().delete(userRegister);

	}

	@Override
	public UserRegister findUsersRegisterByUsersNameAndEmail(String UsersName, String email) throws Exception {
		CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<UserRegister> query = builder.createQuery(UserRegister.class);
		Root<UserRegister> root = query.from(UserRegister.class);
//		query.select(root).where(builder.and(builder.equal(root.get("userName"), UsersName),
//				builder.equal(root.get("email"), email), builder.equal(root.get("status"), 1)));

		Predicate condition1 = builder.equal(root.get("userName"), UsersName);
		Predicate condition2 = builder.equal(root.get("email"), email);
		Predicate condition3 = builder.equal(root.get("status"), 1);
		Predicate combinedCondition = builder.and(condition1, condition2, condition3);

		query.where(combinedCondition);

		return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
	}

}
