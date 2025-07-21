package com.shoes.webshoes.dao.Impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.ExchangeRequestDAO;
import com.shoes.webshoes.entity.ExchangeRequest;

@Repository("ExchangeRequestDao")
@Transactional
public class ExchangeRequestDaoImpl extends AbstractDao<Integer, ExchangeRequest> implements ExchangeRequestDAO {
    
    @Override
    public ExchangeRequest create(ExchangeRequest exchangeRequest) {
        this.getSession().save(exchangeRequest);
        return exchangeRequest;
    }

    @Override
    public ExchangeRequest update(ExchangeRequest exchangeRequest) {
        this.getSession().update(exchangeRequest);
        return exchangeRequest;
    }

    @Override
    public ExchangeRequest findById(Integer id) {
        return this.getSession().find(ExchangeRequest.class, id);
    }

    @Override
    public ExchangeRequest findByReturnRequestId(Integer returnRequestId) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<ExchangeRequest> query = builder.createQuery(ExchangeRequest.class);
        Root<ExchangeRequest> root = query.from(ExchangeRequest.class);
        query.where(builder.equal(root.get("returnRequestId"), returnRequestId));
        List<ExchangeRequest> result = this.getSession().createQuery(query).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public List<ExchangeRequest> findAll() {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<ExchangeRequest> query = builder.createQuery(ExchangeRequest.class);
        Root<ExchangeRequest> root = query.from(ExchangeRequest.class);
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public void delete(Integer id) {
        ExchangeRequest exchangeRequest = findById(id);
        if (exchangeRequest != null) {
            this.getSession().delete(exchangeRequest);
        }
    }
} 