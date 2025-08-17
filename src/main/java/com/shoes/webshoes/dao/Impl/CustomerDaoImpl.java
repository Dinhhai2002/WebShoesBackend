package com.shoes.webshoes.dao.Impl;

import com.shoes.webshoes.common.enums.StoreProcedureStatusCodeEnum;
import com.shoes.webshoes.common.exception.TechresHttpException;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.CustomerDao;
import com.shoes.webshoes.entity.Customer;
import com.shoes.webshoes.model.StoreProcedureListResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository("CustomerDao")
@Transactional
public class CustomerDaoImpl extends AbstractDao<Integer, Customer> implements CustomerDao {

    @Override
    public Customer findById(Long id) {
        return this.getSession().find(Customer.class, id);
    }

    @Override
    public Customer findByPhone(String phone) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<Customer> query = builder.createQuery(Customer.class);
        Root<Customer> root = query.from(Customer.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("isDeleted"), false));

        if (phone != null && !phone.trim().isEmpty()) {
            predicates.add(builder.like(root.get("phone"), "%" + phone + "%"));
        }

        query.where(builder.and(predicates.toArray(new Predicate[0])));
        return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<Customer> findAll(Integer page, Integer limit) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<Customer> query = builder.createQuery(Customer.class);
        Root<Customer> root = query.from(Customer.class);
        query.where(builder.equal(root.get("isDeleted"), false));
        query.orderBy(builder.desc(root.get("id")));

        return this.getSession().createQuery(query)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public void save(Customer customer) {
        this.getSession().save(customer);
    }

    @Override
    public void delete(Long id) {
        Customer customer = findById(id);
        if (customer != null) {
            customer.setIsDeleted(true);
            this.getSession().save(customer);
        }
    }

    @Override
    public Long count() {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Customer> root = query.from(Customer.class);
        query.select(builder.count(root));
        query.where(builder.equal(root.get("isDeleted"), false));
        return this.getSession().createQuery(query).getSingleResult();
    }

    public List<Customer> searchByPhoneOrName(String keyword) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<Customer> query = builder.createQuery(Customer.class);
        Root<Customer> root = query.from(Customer.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("isDeleted"), false));

        if (keyword != null && !keyword.trim().isEmpty()) {
            predicates.add(
                builder.or(
                    builder.like(root.get("phone"), "%" + keyword + "%"),
                    builder.like(root.get("name"), "%" + keyword + "%")
                )
            );
        }

        query.where(builder.and(predicates.toArray(new Predicate[0])));
        query.orderBy(builder.desc(root.get("id")));

        return this.getSession().createQuery(query).getResultList();
    }
}