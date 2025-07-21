package com.shoes.webshoes.dao.Impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.ExchangeRequestDetailDAO;
import com.shoes.webshoes.entity.ExchangeRequestDetail;

@Repository("ExchangeRequestDetailDao")
@Transactional
public class ExchangeRequestDetailDaoImpl extends AbstractDao<Integer, ExchangeRequestDetail> implements ExchangeRequestDetailDAO {
    
    @Override
    public ExchangeRequestDetail create(ExchangeRequestDetail exchangeRequestDetail) {
        this.getSession().save(exchangeRequestDetail);
        return exchangeRequestDetail;
    }

    @Override
    public ExchangeRequestDetail update(ExchangeRequestDetail exchangeRequestDetail) {
        this.getSession().update(exchangeRequestDetail);
        return exchangeRequestDetail;
    }

    @Override
    public ExchangeRequestDetail findById(Integer id) {
        return this.getSession().find(ExchangeRequestDetail.class, id);
    }

    @Override
    public List<ExchangeRequestDetail> findByExchangeRequestId(Integer exchangeRequestId) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<ExchangeRequestDetail> query = builder.createQuery(ExchangeRequestDetail.class);
        Root<ExchangeRequestDetail> root = query.from(ExchangeRequestDetail.class);
        query.where(builder.equal(root.get("exchangeRequestId"), exchangeRequestId));
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public List<ExchangeRequestDetail> findByOldProductId(Integer oldProductId) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<ExchangeRequestDetail> query = builder.createQuery(ExchangeRequestDetail.class);
        Root<ExchangeRequestDetail> root = query.from(ExchangeRequestDetail.class);
        query.where(builder.equal(root.get("oldProductId"), oldProductId));
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public List<ExchangeRequestDetail> findByNewProductId(Integer newProductId) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<ExchangeRequestDetail> query = builder.createQuery(ExchangeRequestDetail.class);
        Root<ExchangeRequestDetail> root = query.from(ExchangeRequestDetail.class);
        query.where(builder.equal(root.get("newProductId"), newProductId));
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public List<ExchangeRequestDetail> findAll() {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<ExchangeRequestDetail> query = builder.createQuery(ExchangeRequestDetail.class);
        Root<ExchangeRequestDetail> root = query.from(ExchangeRequestDetail.class);
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public void delete(Integer id) {
        ExchangeRequestDetail exchangeRequestDetail = findById(id);
        if (exchangeRequestDetail != null) {
            this.getSession().delete(exchangeRequestDetail);
        }
    }

    @Override
    public void deleteByExchangeRequestId(Integer exchangeRequestId) {
        List<ExchangeRequestDetail> details = findByExchangeRequestId(exchangeRequestId);
        for (ExchangeRequestDetail detail : details) {
            this.getSession().delete(detail);
        }
    }
} 