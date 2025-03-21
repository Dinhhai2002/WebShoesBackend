package com.shoes.webshoes.dao;

import java.util.List;
import com.shoes.webshoes.entity.AddressBook;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.model.StoreProcedureListResult;

public interface AddressBookDao {
    void create(AddressBook addressBook) throws Exception;
    AddressBook findOne(int id) throws Exception;
    void update(AddressBook addressBook) throws Exception;
    StoreProcedureListResult<AddressBook> spGListAddressBook(int userId, String keySearch, int status, Pagination pagination) throws Exception;
    List<AddressBook> findByUserId(int userId) throws Exception;
    void setDefaultAddress(int userId, int addressId) throws Exception;
}
