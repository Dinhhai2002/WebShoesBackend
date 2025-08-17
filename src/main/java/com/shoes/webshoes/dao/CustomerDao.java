package com.shoes.webshoes.dao;

import com.shoes.webshoes.entity.Customer;
import java.util.List;

public interface CustomerDao {
    Customer findById(Long id);
    Customer findByPhone(String phone);
    List<Customer> findAll(Integer page, Integer limit);
    void save(Customer customer);
    void delete(Long id);
    Long count();
    List<Customer> searchByPhoneOrName(String keyword);
}