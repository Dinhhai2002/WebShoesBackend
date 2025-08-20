package com.shoes.webshoes.service.impl;

import com.shoes.webshoes.common.exception.TechresHttpException;
import com.shoes.webshoes.dao.CustomerDao;
import com.shoes.webshoes.entity.Customer;
import com.shoes.webshoes.request.CRUDCustomerRequest;
import com.shoes.webshoes.response.CustomerResponse;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public CustomerResponse create(CRUDCustomerRequest request) throws Exception {
        // Tìm kiếm chính xác số điện thoại khi tạo mới
        List<Customer> existingCustomers = customerDao.searchByPhoneOrName(request.getPhone());
        for (Customer customer : existingCustomers) {
            if (customer.getPhone().equals(request.getPhone())) {
                throw new TechresHttpException(HttpStatus.BAD_REQUEST, "Số điện thoại đã tồn tại");
            }
        }
        if(request.getPhone() != null && !request.getPhone().startsWith("0")) {
        	throw new TechresHttpException(HttpStatus.BAD_REQUEST, "Dữ liệu tạo khách hàng không hợp lệ");
        }

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        customer.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        customerDao.save(customer);
        return new CustomerResponse(customer);
    }

    @Override
    public CustomerResponse update(Long id, CRUDCustomerRequest request) throws Exception {
        Customer customer = customerDao.findById(id);
        if (customer == null) {
            throw new TechresHttpException(HttpStatus.NOT_FOUND, "Không tìm thấy khách hàng");
        }

        // Tìm kiếm chính xác số điện thoại khi cập nhật
        List<Customer> existingCustomers = customerDao.searchByPhoneOrName(request.getPhone());
        for (Customer existingCustomer : existingCustomers) {
            if (existingCustomer.getPhone().equals(request.getPhone()) && !existingCustomer.getId().equals(id)) {
                throw new TechresHttpException(HttpStatus.BAD_REQUEST, "Số điện thoại đã tồn tại");
            }
        }

        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        customerDao.save(customer);
        return new CustomerResponse(customer);
    }

    @Override
    public void delete(Long id) throws Exception {
        Customer customer = customerDao.findById(id);
        if (customer == null) {
            throw new TechresHttpException(HttpStatus.NOT_FOUND, "Không tìm thấy khách hàng");
        }
        customerDao.delete(id);
    }

    @Override
    public CustomerResponse findById(Long id) throws Exception {
        Customer customer = customerDao.findById(id);
        if (customer == null || customer.getIsDeleted()) {
            throw new TechresHttpException(HttpStatus.NOT_FOUND, "Không tìm thấy khách hàng");
        }
        return new CustomerResponse(customer);
    }

    @Override
    public List<CustomerResponse> findByPhone(String phone) throws Exception {
        List<Customer> customers = customerDao.searchByPhoneOrName(phone);
        if (customers.isEmpty()) {
            return null;
        }
		List<CustomerResponse> customerResponses = customers.stream()
                .map(CustomerResponse::new)
                .collect(Collectors.toList());
        return customerResponses;
    }

    @Override
    public BaseListDataResponse findAll(Integer page, Integer limit) throws Exception {
        List<Customer> customers = customerDao.findAll(page, limit);
        List<CustomerResponse> customerResponses = customers.stream()
                .map(CustomerResponse::new)
                .collect(Collectors.toList());

        BaseListDataResponse response = new BaseListDataResponse();
        response.setList(customerResponses);
        response.setTotalRecord(customerDao.count());
        response.setLimit(limit);

        return response;
    }

    @Override
    public BaseListDataResponse search(String keyword, Integer page, Integer limit) throws Exception {
        List<Customer> customers = customerDao.searchByPhoneOrName(keyword);
        
        // Thực hiện phân trang thủ công vì đã có toàn bộ kết quả
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, customers.size());
        List<Customer> paginatedCustomers = customers.subList(start, end);
        
        List<CustomerResponse> customerResponses = paginatedCustomers.stream()
                .map(CustomerResponse::new)
                .collect(Collectors.toList());

        BaseListDataResponse response = new BaseListDataResponse();
        response.setList(customerResponses);
        response.setTotalRecord((long) customers.size());
        response.setLimit(limit);

        return response;
    }
}