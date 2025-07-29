package com.shoes.webshoes.dao.Impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.SaveForLaterDAO;
import com.shoes.webshoes.entity.SaveForLater;

@Repository("SaveForLaterDao")
@Transactional
public class SaveForLaterDaoImpl extends AbstractDao<Integer, SaveForLater> implements SaveForLaterDAO {
    
    @Override
    public SaveForLater create(SaveForLater saveForLater) {
        this.getSession().save(saveForLater);
        return saveForLater;
    }

    @Override
    public SaveForLater findById(Integer id) {
        return this.getSession().find(SaveForLater.class, id);
    }

    @Override
    public List<SaveForLater> findByUserId(Integer userId) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<SaveForLater> query = builder.createQuery(SaveForLater.class);
        Root<SaveForLater> root = query.from(SaveForLater.class);
        query.where(builder.equal(root.get("userId"), userId));
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public SaveForLater findByUserIdAndProductDetailId(Integer userId, Integer productDetailId) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<SaveForLater> query = builder.createQuery(SaveForLater.class);
        Root<SaveForLater> root = query.from(SaveForLater.class);
        query.where(
            builder.and(
                builder.equal(root.get("userId"), userId),
                builder.equal(root.get("productDetailId"), productDetailId)
            )
        );
        List<SaveForLater> results = this.getSession().createQuery(query).getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public void delete(Integer id) {
        SaveForLater saveForLater = findById(id);
        if (saveForLater != null) {
            this.getSession().delete(saveForLater);
        }
    }

    @Override
    public void deleteByUserIdAndProductDetailId(Integer userId, Integer productDetailId) {
        SaveForLater saveForLater = findByUserIdAndProductDetailId(userId, productDetailId);
        if (saveForLater != null) {
            this.getSession().delete(saveForLater);
        }
    }
} 