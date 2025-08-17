package com.shoes.webshoes.controller;

import com.shoes.webshoes.common.utils.StringErrorValue;
import com.shoes.webshoes.request.CRUDCustomerRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.CustomerResponse;
import com.shoes.webshoes.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("")
    public ResponseEntity<BaseResponse<BaseListDataResponse<CustomerResponse>>> getAll(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        
        BaseResponse<BaseListDataResponse<CustomerResponse>> response = new BaseResponse<>();
        try {
            BaseListDataResponse listData;
            if (keyword != null && !keyword.trim().isEmpty()) {
                listData = customerService.search(keyword, page, limit);
            } else {
                listData = customerService.findAll(page, limit);
            }
            response.setData(listData);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<BaseResponse<BaseListDataResponse<CustomerResponse>>> getAllCustomers() {
        BaseResponse<BaseListDataResponse<CustomerResponse>> response = new BaseResponse<>();
        try {
            BaseListDataResponse listData = customerService.findAll(0, Integer.MAX_VALUE);
            response.setData(listData);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<CustomerResponse>> findOneById(@PathVariable("id") Long id) {
        BaseResponse<CustomerResponse> response = new BaseResponse<>();
        try {
            CustomerResponse customer = customerService.findById(id);
            response.setData(customer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<BaseResponse<List<CustomerResponse>>> findByPhone(@PathVariable String phone) {
        BaseResponse<List<CustomerResponse>> response = new BaseResponse<>();
        try {
        	List<CustomerResponse> customer = customerService.findByPhone(phone);
            if(customer == null) {
            	 response.setStatus(HttpStatus.BAD_REQUEST);
                 response.setMessageError("Không tìm thấy khách hàng");
                 return new ResponseEntity<>(response, HttpStatus.OK);
            }
            response.setData(customer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public ResponseEntity<BaseResponse<CustomerResponse>> create(
            @Valid @RequestBody CRUDCustomerRequest request) {
        BaseResponse<CustomerResponse> response = new BaseResponse<>();
        try {
            CustomerResponse customer = customerService.create(request);
            response.setData(customer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public ResponseEntity<BaseResponse<CustomerResponse>> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody CRUDCustomerRequest request) {
        BaseResponse<CustomerResponse> response = new BaseResponse<>();
        try {
            CustomerResponse customer = customerService.update(id, request);
            response.setData(customer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<BaseResponse<String>> delete(@PathVariable("id") Long id) {
        BaseResponse<String> response = new BaseResponse<>();
        try {
            customerService.delete(id);
            response.setData("Xóa khách hàng thành công");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}