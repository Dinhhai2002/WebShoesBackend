package com.shoes.webshoes.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.AddressBookDao;
import com.shoes.webshoes.entity.AddressBook;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.service.AddressBookService;

@Service("AddressBookService")
@Transactional(rollbackFor = Error.class)
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookDao addressBookDao;

    @Override
    public void create(AddressBook addressBook) throws Exception {
        addressBookDao.create(addressBook);
    }

    @Override
    public AddressBook findOne(int id) throws Exception {
        return addressBookDao.findOne(id);
    }

    @Override
    public void update(AddressBook addressBook) throws Exception {
        addressBookDao.update(addressBook);
    }

    @Override
    public StoreProcedureListResult<AddressBook> spGListAddressBook(int userId, String keySearch, int status, Pagination pagination) throws Exception {
        return addressBookDao.spGListAddressBook(userId, keySearch, status, pagination);
    }

    @Override
    public List<AddressBook> findByUserId(int userId) throws Exception {
        return addressBookDao.findByUserId(userId);
    }

    @Override
    public void setDefaultAddress(int userId, int addressId) throws Exception {
        addressBookDao.setDefaultAddress(userId, addressId);
    }
}
