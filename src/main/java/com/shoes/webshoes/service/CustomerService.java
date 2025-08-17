package com.shoes.webshoes.service;

import java.util.List;

import com.shoes.webshoes.request.CRUDCustomerRequest;
import com.shoes.webshoes.response.CustomerResponse;
import com.shoes.webshoes.response.BaseListDataResponse;

public interface CustomerService {
    CustomerResponse create(CRUDCustomerRequest request) throws Exception;
    CustomerResponse update(Long id, CRUDCustomerRequest request) throws Exception;
    void delete(Long id) throws Exception;
    CustomerResponse findById(Long id) throws Exception;
    List<CustomerResponse> findByPhone(String phone) throws Exception;
    BaseListDataResponse findAll(Integer page, Integer limit) throws Exception;
    BaseListDataResponse search(String keyword, Integer page, Integer limit) throws Exception;
}