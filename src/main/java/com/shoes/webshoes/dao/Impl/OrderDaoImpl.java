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
import com.shoes.webshoes.dao.OrderDao;
import com.shoes.webshoes.entity.Order;
import com.shoes.webshoes.model.StoreProcedureListResult;

@Repository("OrderDao")
@Transactional
public class OrderDaoImpl extends AbstractDao<Integer, Order> implements OrderDao {
    @Override
    public void create(Order order) {
       this.getSession().save(order);
    }

    @Override
    public Order findOne(int id) {
       return this.getSession().find(Order.class,id);
    }

    @Override
    public void update(Order order) {
       this.getSession().update(order);	
    }

    @Override
    public List<Order> getAll() {
       CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Order> query = builder.createQuery(Order.class);
		Root<Order> root = query.from(Order.class);

		return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public Order findByName(String name) {
        CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Order> query = builder.createQuery(Order.class);
		Root<Order> root = query.from(Order.class);
		query.where(builder.equal(root.get("name"), name));

		return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
	@Override
	public StoreProcedureListResult<Order> spGListOrder(int userId, String keySearch, int status, int paymentStatus, int paymentMethod, Pagination pagination)
			throws Exception {
		StoredProcedureQuery query = this.getSession().createStoredProcedureQuery("sp_g_list_order", Order.class)
				.registerStoredProcedureParameter("userId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("keySearch", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("status", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("paymentStatus", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("paymentMethod", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_limit", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_offset", Integer.class, ParameterMode.IN)

				.registerStoredProcedureParameter("total_record", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

		query.setParameter("userId", userId);
		query.setParameter("keySearch", keySearch);
		query.setParameter("status", status);
		query.setParameter("paymentStatus", paymentStatus);
		query.setParameter("paymentMethod", paymentMethod);
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
