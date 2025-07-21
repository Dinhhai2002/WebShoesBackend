package com.shoes.webshoes.dao.Impl;

import java.util.List;
import java.util.Date;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.ReturnRequestDAO;
import com.shoes.webshoes.entity.ReturnRequest;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.common.enums.StoreProcedureStatusCodeEnum;
import org.springframework.http.HttpStatus;
import com.shoes.webshoes.common.exception.TechresHttpException;
import java.util.ArrayList;

@Repository("ReturnRequestDao")
@Transactional
public class ReturnRequestDaoImpl extends AbstractDao<Integer, ReturnRequest> implements ReturnRequestDAO {
    
    @Override
    public ReturnRequest create(ReturnRequest returnRequest) {
        this.getSession().save(returnRequest);
        return returnRequest;
    }

    @Override
    public ReturnRequest update(ReturnRequest returnRequest) {
        this.getSession().update(returnRequest);
        return returnRequest;
    }

    @Override
    public ReturnRequest findById(Integer id) {
        return this.getSession().find(ReturnRequest.class, id);
    }

    @Override
    public List<ReturnRequest> findByUserId(Integer userId) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<ReturnRequest> query = builder.createQuery(ReturnRequest.class);
        Root<ReturnRequest> root = query.from(ReturnRequest.class);
        query.where(builder.equal(root.get("userId"), userId));
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public List<ReturnRequest> findByOrderId(Integer orderId) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<ReturnRequest> query = builder.createQuery(ReturnRequest.class);
        Root<ReturnRequest> root = query.from(ReturnRequest.class);
        query.where(builder.equal(root.get("orderId"), orderId));
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public List<ReturnRequest> findByStatus(String status) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<ReturnRequest> query = builder.createQuery(ReturnRequest.class);
        Root<ReturnRequest> root = query.from(ReturnRequest.class);
        query.where(builder.equal(root.get("status"), status));
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public List<ReturnRequest> findAll() {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<ReturnRequest> query = builder.createQuery(ReturnRequest.class);
        Root<ReturnRequest> root = query.from(ReturnRequest.class);
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public void delete(Integer id) {
        ReturnRequest returnRequest = findById(id);
        if (returnRequest != null) {
            this.getSession().delete(returnRequest);
        }
    }
    
    @Override
    public StoreProcedureListResult<ReturnRequest> spGListReturnRequest(Integer userId, String keySearch, String status, 
            Pagination pagination) throws Exception {
        
        StoredProcedureQuery query = this.getSession().createStoredProcedureQuery("sp_g_list_return_request", ReturnRequest.class)
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

    @Override
    public ReturnRequest findByUserIdAndOrderId(Integer userId, Integer orderId) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<ReturnRequest> query = builder.createQuery(ReturnRequest.class);
        Root<ReturnRequest> root = query.from(ReturnRequest.class);
        query.where(
            builder.and(
                builder.equal(root.get("userId"), userId),
                builder.equal(root.get("orderId"), orderId)
            )
        );
        List<ReturnRequest> result = this.getSession().createQuery(query).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
} 