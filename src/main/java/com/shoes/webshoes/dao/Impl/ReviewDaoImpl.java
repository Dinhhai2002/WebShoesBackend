package com.shoes.webshoes.dao.Impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.ReviewDao;
import com.shoes.webshoes.entity.ProductDetail;
import com.shoes.webshoes.entity.Review;
import com.shoes.webshoes.model.StoreProcedureListResult;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import org.springframework.http.HttpStatus;
import com.shoes.webshoes.common.enums.StoreProcedureStatusCodeEnum;
import com.shoes.webshoes.common.exception.TechresHttpException;

@Repository("ReviewDao")
@Transactional
public class ReviewDaoImpl extends AbstractDao<Integer, Review> implements ReviewDao {

    @Override
    public void create(Review review) {
        this.getSession().save(review);
    }

    @Override
    public Review findOne(int id) {
        return this.getSession().get(Review.class, id);
    }

    @Override
    public void update(Review review) {
        this.getSession().update(review);
    }

    @Override
    public void delete(int id) {
        Review review = findOne(id);
        if (review != null) {
            this.getSession().delete(review);
        }
    }

    @Override
    public Review findByUserIdAndProductId(int userId, int productId) {
        String hql = "FROM Review WHERE userId = :userId AND productId = :productId";
        return (Review) this.getSession().createQuery(hql)
                .setParameter("userId", userId)
                .setParameter("productId", productId)
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public StoreProcedureListResult<Review> spGListReview(int userId, int productId, String keySearch, int status, Pagination pagination) throws Exception {
        StoredProcedureQuery query = this.getSession().createStoredProcedureQuery("sp_g_list_review", Review.class)
                .registerStoredProcedureParameter("userId", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("productId", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("keySearch", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("status", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("_limit", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("_offset", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("total_record", Integer.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

        query.setParameter("userId", userId);
        query.setParameter("productId", productId);
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
