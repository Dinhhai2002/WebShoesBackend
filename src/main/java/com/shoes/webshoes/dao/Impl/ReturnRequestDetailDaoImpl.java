package com.shoes.webshoes.dao.Impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.ReturnRequestDetailDAO;
import com.shoes.webshoes.entity.ReturnRequestDetail;

@Repository("ReturnRequestDetailDao")
@Transactional
public class ReturnRequestDetailDaoImpl extends AbstractDao<Integer, ReturnRequestDetail> implements ReturnRequestDetailDAO {
    
    @Override
    public ReturnRequestDetail create(ReturnRequestDetail returnRequestDetail) {
        this.getSession().save(returnRequestDetail);
        return returnRequestDetail;
    }

    @Override
    public ReturnRequestDetail update(ReturnRequestDetail returnRequestDetail) {
        this.getSession().update(returnRequestDetail);
        return returnRequestDetail;
    }

    @Override
    public ReturnRequestDetail findById(Integer id) {
        return this.getSession().find(ReturnRequestDetail.class, id);
    }

    @Override
    public List<ReturnRequestDetail> findByReturnRequestId(Integer returnRequestId) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<ReturnRequestDetail> query = builder.createQuery(ReturnRequestDetail.class);
        Root<ReturnRequestDetail> root = query.from(ReturnRequestDetail.class);
        query.where(builder.equal(root.get("returnRequestId"), returnRequestId));
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public List<ReturnRequestDetail> findByProductDetailId(Integer productDetailId) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<ReturnRequestDetail> query = builder.createQuery(ReturnRequestDetail.class);
        Root<ReturnRequestDetail> root = query.from(ReturnRequestDetail.class);
        query.where(builder.equal(root.get("productDetailId"), productDetailId));
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public List<ReturnRequestDetail> findByProductId(Integer productId) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<ReturnRequestDetail> query = builder.createQuery(ReturnRequestDetail.class);
        Root<ReturnRequestDetail> root = query.from(ReturnRequestDetail.class);
        query.where(builder.equal(root.get("productId"), productId));
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public void delete(Integer id) {
        ReturnRequestDetail returnRequestDetail = findById(id);
        if (returnRequestDetail != null) {
            this.getSession().delete(returnRequestDetail);
        }
    }

    @Override
    public void deleteByReturnRequestId(Integer returnRequestId) {
        List<ReturnRequestDetail> details = findByReturnRequestId(returnRequestId);
        for (ReturnRequestDetail detail : details) {
            this.getSession().delete(detail);
        }
    }
} 