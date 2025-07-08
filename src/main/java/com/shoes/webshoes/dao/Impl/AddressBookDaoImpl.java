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
import com.shoes.webshoes.dao.AddressBookDao;
import com.shoes.webshoes.entity.AddressBook;
import com.shoes.webshoes.model.StoreProcedureListResult;

@Repository("AddressBookDao")
@Transactional
public class AddressBookDaoImpl extends AbstractDao<Integer, AddressBook> implements AddressBookDao {

    @Override
    public void create(AddressBook addressBook) throws Exception {
        this.getSession().save(addressBook);
    }

    @Override
    public AddressBook findOne(int id) throws Exception {
        return this.getSession().find(AddressBook.class, id);
    }

    @Override
    public void update(AddressBook addressBook) throws Exception {
        this.getSession().update(addressBook);
    }

    @Override
    public StoreProcedureListResult<AddressBook> spGListAddressBook(int userId, String keySearch, int status, Pagination pagination) throws Exception {
        StoredProcedureQuery query = this.getSession()
            .createStoredProcedureQuery("sp_g_list_addressbook", AddressBook.class)
            .registerStoredProcedureParameter("userId", Integer.class, ParameterMode.IN)
            .registerStoredProcedureParameter("keySearch", String.class, ParameterMode.IN)
            .registerStoredProcedureParameter("status", Integer.class, ParameterMode.IN)
            .registerStoredProcedureParameter("_limit", Integer.class, ParameterMode.IN)
            .registerStoredProcedureParameter("_offset", Integer.class, ParameterMode.IN)
            .registerStoredProcedureParameter("total_record", Integer.class, ParameterMode.OUT)
            .registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
            .registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

        query.setParameter("userId", userId);
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
    public List<AddressBook> findByUserId(int userId) throws Exception {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<AddressBook> query = builder.createQuery(AddressBook.class);
        Root<AddressBook> root = query.from(AddressBook.class);
        query.where(builder.equal(root.get("userId"), userId));
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public void setDefaultAddress(int userId, int addressId) throws Exception {
        // Reset all addresses to non-default
        String hql1 = "UPDATE AddressBook SET isDefault = 0 WHERE userId = :userId";
        this.getSession().createQuery(hql1)
            .setParameter("userId", userId)
            .executeUpdate();

        // Set the selected address as default
        String hql2 = "UPDATE AddressBook SET isDefault = 1 WHERE id = :addressId AND userId = :userId";
        this.getSession().createQuery(hql2)
            .setParameter("addressId", addressId)
            .setParameter("userId", userId)
            .executeUpdate();
    }
}
