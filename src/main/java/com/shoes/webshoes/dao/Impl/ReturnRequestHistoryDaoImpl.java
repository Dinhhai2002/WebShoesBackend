package com.shoes.webshoes.dao.Impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.ReturnRequestHistoryDAO;
import com.shoes.webshoes.entity.ReturnRequestHistory;

@Repository("ReturnRequestHistoryDao")
@Transactional
public class ReturnRequestHistoryDaoImpl extends AbstractDao<Integer, ReturnRequestHistory> implements ReturnRequestHistoryDAO {
    
    @Override
    public ReturnRequestHistory create(ReturnRequestHistory returnRequestHistory) {
        this.getSession().save(returnRequestHistory);
        return returnRequestHistory;
    }

    @Override
    public ReturnRequestHistory findById(Integer id) {
        return this.getSession().find(ReturnRequestHistory.class, id);
    }

    @Override
    public List<ReturnRequestHistory> findByReturnRequestId(Integer returnRequestId) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<ReturnRequestHistory> query = builder.createQuery(ReturnRequestHistory.class);
        Root<ReturnRequestHistory> root = query.from(ReturnRequestHistory.class);
        query.where(builder.equal(root.get("returnRequestId"), returnRequestId));
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public List<ReturnRequestHistory> findByStatus(String status) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<ReturnRequestHistory> query = builder.createQuery(ReturnRequestHistory.class);
        Root<ReturnRequestHistory> root = query.from(ReturnRequestHistory.class);
        query.where(builder.equal(root.get("status"), status));
        return this.getSession().createQuery(query).getResultList();
    }
} 