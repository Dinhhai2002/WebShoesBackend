package com.shoes.webshoes.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.common.utils.StringErrorValue;
import com.shoes.webshoes.entity.AddressBook;
import com.shoes.webshoes.entity.Users;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDAddressBookRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.AddressBookResponse;
import com.shoes.webshoes.service.AddressBookService;

@RestController
@RequestMapping("/api/v1/address-book")
public class AddressBookController extends BaseController {

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("")
    public ResponseEntity<BaseResponse<BaseListDataResponse<AddressBookResponse>>> getAll(
            @RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
            @RequestParam(name = "status", required = false, defaultValue = "-1") int status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
        
        BaseResponse<BaseListDataResponse<AddressBookResponse>> response = new BaseResponse<>();
        Users currentUser = this.getUser();
        
        Pagination pagination = new Pagination(page, limit);
        StoreProcedureListResult<AddressBook> listResult = addressBookService.spGListAddressBook(
            currentUser.getId(), keySearch, status, pagination);

        BaseListDataResponse<AddressBookResponse> listData = new BaseListDataResponse<>();
        listData.setList(new AddressBookResponse().mapToList(listResult.getResult()));
        listData.setTotalRecord(listResult.getTotalRecord());

        response.setData(listData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<AddressBookResponse>> findOne(@PathVariable("id") int id) throws Exception {
        BaseResponse<AddressBookResponse> response = new BaseResponse<>();
        Users currentUser = this.getUser();
        
        AddressBook addressBook = addressBookService.findOne(id);
        if (addressBook == null || addressBook.getUserId() != currentUser.getId()) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.ADDRESS_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.setData(new AddressBookResponse(addressBook));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<AddressBookResponse>> create(
            @Valid @RequestBody CRUDAddressBookRequest wrapper) throws Exception {
        BaseResponse<AddressBookResponse> response = new BaseResponse<>();
        Users currentUser = this.getUser();

        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(currentUser.getId());
        addressBook.setFullName(wrapper.getFullName());
        addressBook.setPhone(wrapper.getPhone());
        addressBook.setWardId(wrapper.getWardId());
        addressBook.setWardName(wrapper.getWardName());
        addressBook.setDistrictId(wrapper.getDistrictId());
        addressBook.setDistrictName(wrapper.getDistrictName());
        addressBook.setCityId(wrapper.getCityId());
        addressBook.setCityName(wrapper.getCityName());
        addressBook.setFullAddress(wrapper.getFullAddress());
        addressBook.setIsDefault(wrapper.getIsDefault());
        addressBook.setStatus(1);

        addressBookService.create(addressBook);
        
        if (wrapper.getIsDefault() == 1) {
            addressBookService.setDefaultAddress(currentUser.getId(), addressBook.getId());
        }

        response.setData(new AddressBookResponse(addressBook));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<BaseResponse<AddressBookResponse>> update(
            @PathVariable("id") int id,
            @Valid @RequestBody CRUDAddressBookRequest wrapper) throws Exception {
        BaseResponse<AddressBookResponse> response = new BaseResponse<>();
        Users currentUser = this.getUser();

        AddressBook addressBook = addressBookService.findOne(id);
        if (addressBook == null || addressBook.getUserId() != currentUser.getId()) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.ADDRESS_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        addressBook.setFullName(wrapper.getFullName());
        addressBook.setPhone(wrapper.getPhone());
        addressBook.setWardId(wrapper.getWardId());
        addressBook.setWardName(wrapper.getWardName());
        addressBook.setDistrictId(wrapper.getDistrictId());
        addressBook.setDistrictName(wrapper.getDistrictName());
        addressBook.setCityId(wrapper.getCityId());
        addressBook.setCityName(wrapper.getCityName());
        addressBook.setFullAddress(wrapper.getFullAddress());
        addressBook.setIsDefault(wrapper.getIsDefault());

        addressBookService.update(addressBook);
        
        if (wrapper.getIsDefault() == 1) {
            addressBookService.setDefaultAddress(currentUser.getId(), addressBook.getId());
        }

        response.setData(new AddressBookResponse(addressBook));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/change-status")
    public ResponseEntity<BaseResponse<AddressBookResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
        BaseResponse<AddressBookResponse> response = new BaseResponse<>();
        Users currentUser = this.getUser();

        AddressBook addressBook = addressBookService.findOne(id);
        if (addressBook == null || addressBook.getUserId() != currentUser.getId()) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.ADDRESS_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        addressBook.setStatus(addressBook.getStatus() == 1 ? 0 : 1);
        addressBookService.update(addressBook);

        response.setData(new AddressBookResponse(addressBook));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/set-default")
    public ResponseEntity<BaseResponse<AddressBookResponse>> setDefault(@PathVariable("id") int id) throws Exception {
        BaseResponse<AddressBookResponse> response = new BaseResponse<>();
        Users currentUser = this.getUser();

        AddressBook addressBook = addressBookService.findOne(id);
        if (addressBook == null || addressBook.getUserId() != currentUser.getId()) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.ADDRESS_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        addressBookService.setDefaultAddress(currentUser.getId(), id);
        addressBook = addressBookService.findOne(id); // Refresh data

        response.setData(new AddressBookResponse(addressBook));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
