package com.shoes.webshoes.dao.Impl;

import java.util.List;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.shoes.webshoes.common.enums.StoreProcedureStatusCodeEnum;
import com.shoes.webshoes.common.exception.TechresHttpException;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.CartDao;
import com.shoes.webshoes.entity.Cart;
import com.shoes.webshoes.model.StoreProcedureListResult;

@Repository("CartDao")
@Transactional
public class CartDaoImpl extends AbstractDao<Integer, Cart> implements CartDao {
    @Override
    public void create(Cart cart) {
       this.getSession().save(cart);
    }

    @Override
    public Cart findOne(int id) {
       return this.getSession().find(Cart.class,id);
    }

    @Override
    public void update(Cart cart) {
       this.getSession().update(cart);	
    }

    @Override
    public List<Cart> getAll() {
       CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Cart> query = builder.createQuery(Cart.class);
		Root<Cart> root = query.from(Cart.class);

		return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public Cart findByName(String name) {
        CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Cart> query = builder.createQuery(Cart.class);
		Root<Cart> root = query.from(Cart.class);
		query.where(builder.equal(root.get("name"), name));

		return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
	@Override
	public StoreProcedureListResult<Cart> spGListCart(int userId, String keySearch, int status, Pagination pagination)
			throws Exception {
		StoredProcedureQuery query = this.getSession().createStoredProcedureQuery("sp_g_list_cart", Cart.class)
				.registerStoredProcedureParameter("userId", Integer.class, ParameterMode.IN)	
				.registerStoredProcedureParameter("keySearch", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("status", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_limit", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_offset", Integer.class, ParameterMode.IN)

				.registerStoredProcedureParameter("total_record", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);
		
		query.setParameter("userId", userId);
		query.setParameter("keySearch", keySearch);
		query.setParameter("status", status);
		query.setParameter("_limit", pagination.getLimit());
		query.setParameter("_offset", pagination.getOffset());

		int statusCode = (int) query.getOutputParameterValue("status_code");
		String messageError = query.getOutputParameterValue("message_error").toString();

		switch (StoreProcedureStatusCodeEnum.valueOf(statusCode)) {
		case SUCCESS:
			int totalRecord = (int) query.getOutputParameterValue("total_record");
			return new StoreProcedureListResult<>(statusCode, messageError, totalRecord, query.getResultList());
		case INPUT_INVALID:
			throw new TechresHttpException(HttpStatus.BAD_REQUEST, messageError);
		default:
			throw new Exception(messageError);
		}
	}
}
