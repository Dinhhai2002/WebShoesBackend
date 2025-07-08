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
import com.shoes.webshoes.dao.CartDetailDao;
import com.shoes.webshoes.entity.CartDetail;
import com.shoes.webshoes.model.StoreProcedureListResult;

@Repository("CartDetailDao")
@Transactional
public class CartDetailDaoImpl extends AbstractDao<Integer, CartDetail> implements CartDetailDao {
    @Override
    public void create(CartDetail cartDetail) {
       this.getSession().save(cartDetail);
    }

    @Override
    public CartDetail findOne(int id) {
       return this.getSession().find(CartDetail.class,id);
    }

    @Override
    public void update(CartDetail cartDetail) {
       this.getSession().update(cartDetail);	
    }

    @Override
    public List<CartDetail> getAll() {
       CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<CartDetail> query = builder.createQuery(CartDetail.class);
		Root<CartDetail> root = query.from(CartDetail.class);

		return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public CartDetail findByName(String name) {
        CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<CartDetail> query = builder.createQuery(CartDetail.class);
		Root<CartDetail> root = query.from(CartDetail.class);
		query.where(builder.equal(root.get("name"), name));

		return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
	@Override
	public StoreProcedureListResult<CartDetail> spGListCartDetail(int cartId,int productDetailId, String keySearch, int status, Pagination pagination)
			throws Exception {
		StoredProcedureQuery query = this.getSession().createStoredProcedureQuery("sp_g_list_cart_detail", CartDetail.class)
				.registerStoredProcedureParameter("cartId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("productDetailId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("keySearch", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("status", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_limit", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_offset", Integer.class, ParameterMode.IN)

				.registerStoredProcedureParameter("total_record", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

		query.setParameter("cartId", cartId);
		query.setParameter("productDetailId", productDetailId);
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
    
    @Override
    public void delete(int id) {
        CartDetail cartDetail = this.findOne(id);
        if (cartDetail != null) {
            this.getSession().delete(cartDetail);
        }
    }
}
