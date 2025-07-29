package com.shoes.webshoes.dao.Impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.CancelOrderHistoryDAO;
import com.shoes.webshoes.entity.CancelOrderHistory;

@Repository("CancelOrderHistoryDao")
@Transactional
public class CancelOrderHistoryDaoImpl extends AbstractDao<Integer, CancelOrderHistory> implements CancelOrderHistoryDAO {
    
    @Override
    public CancelOrderHistory create(CancelOrderHistory cancelOrderHistory) {
        this.getSession().save(cancelOrderHistory);
        return cancelOrderHistory;
    }

    @Override
    public List<CancelOrderHistory> findByCancelRequestId(Integer cancelRequestId) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<CancelOrderHistory> query = builder.createQuery(CancelOrderHistory.class);
        Root<CancelOrderHistory> root = query.from(CancelOrderHistory.class);
        query.where(builder.equal(root.get("cancelRequestId"), cancelRequestId));
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public void deleteByCancelRequestId(Integer cancelRequestId) {
        List<CancelOrderHistory> histories = findByCancelRequestId(cancelRequestId);
        for (CancelOrderHistory history : histories) {
            this.getSession().delete(history);
        }
    }
} 