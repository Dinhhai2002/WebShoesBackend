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
import com.shoes.webshoes.dao.CancelOrderRequestDAO;
import com.shoes.webshoes.entity.CancelOrderRequest;
import com.shoes.webshoes.model.StoreProcedureListResult;

@Repository("CancelOrderRequestDao")
@Transactional
public class CancelOrderRequestDaoImpl extends AbstractDao<Integer, CancelOrderRequest> implements CancelOrderRequestDAO {
    
    @Override
    public CancelOrderRequest create(CancelOrderRequest cancelOrderRequest) {
        this.getSession().save(cancelOrderRequest);
        return cancelOrderRequest;
    }

    @Override
    public CancelOrderRequest update(CancelOrderRequest cancelOrderRequest) {
        this.getSession().update(cancelOrderRequest);
        return cancelOrderRequest;
    }

    @Override
    public CancelOrderRequest findById(Integer id) {
        return this.getSession().find(CancelOrderRequest.class, id);
    }

    @Override
    public CancelOrderRequest findByOrderId(Integer orderId) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<CancelOrderRequest> query = builder.createQuery(CancelOrderRequest.class);
        Root<CancelOrderRequest> root = query.from(CancelOrderRequest.class);
        query.where(builder.equal(root.get("orderId"), orderId));
        List<CancelOrderRequest> results = this.getSession().createQuery(query).getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<CancelOrderRequest> findByUserId(Integer userId) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<CancelOrderRequest> query = builder.createQuery(CancelOrderRequest.class);
        Root<CancelOrderRequest> root = query.from(CancelOrderRequest.class);
        query.where(builder.equal(root.get("userId"), userId));
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public List<CancelOrderRequest> findByStatus(String status) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<CancelOrderRequest> query = builder.createQuery(CancelOrderRequest.class);
        Root<CancelOrderRequest> root = query.from(CancelOrderRequest.class);
        query.where(builder.equal(root.get("status"), CancelOrderRequest.CancelOrderStatus.valueOf(status)));
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public List<CancelOrderRequest> findAll() {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<CancelOrderRequest> query = builder.createQuery(CancelOrderRequest.class);
        Root<CancelOrderRequest> root = query.from(CancelOrderRequest.class);
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public void delete(Integer id) {
        CancelOrderRequest cancelOrderRequest = findById(id);
        if (cancelOrderRequest != null) {
            this.getSession().delete(cancelOrderRequest);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public StoreProcedureListResult<CancelOrderRequest> spGListCancelRequest(Integer userId, String keySearch, String status, Pagination pagination) throws Exception {
    	 StoredProcedureQuery query = this.getSession().createStoredProcedureQuery("sp_g_list_cancel_request", CancelOrderRequest.class)
 				.registerStoredProcedureParameter("userId", Integer.class, ParameterMode.IN)
 				.registerStoredProcedureParameter("keySearch", String.class, ParameterMode.IN)
 				.registerStoredProcedureParameter("status", String.class, ParameterMode.IN)
 				.registerStoredProcedureParameter("_limit", Integer.class, ParameterMode.IN)
 				.registerStoredProcedureParameter("_offset", Integer.class, ParameterMode.IN)

 				.registerStoredProcedureParameter("total_record", Integer.class, ParameterMode.OUT)
 				.registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
 				.registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

 		// Handle null parameters by providing default values
 		query.setParameter("userId",  userId);
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