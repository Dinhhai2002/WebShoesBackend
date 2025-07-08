package com.shoes.webshoes.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.common.utils.StringErrorValue;
import com.shoes.webshoes.entity.VoucherApplication;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDVoucherApplicationRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.VoucherApplicationResponse;
import com.shoes.webshoes.service.VoucherApplicationService;


@RestController
@RequestMapping("/api/v1/voucher-application")
public class VoucherApplicationController  {
    @Autowired
    public VoucherApplicationService voucherApplicationService;

    @GetMapping("")
//	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<VoucherApplicationResponse>>> getAll(
			@RequestParam(name = "voucher_id", required = false, defaultValue = "-1") int voucherId,
			@RequestParam(name = "product_id", required = false, defaultValue = "-1") int productId,
			@RequestParam(name = "brand_id", required = false, defaultValue = "-1") int brandId,
			@RequestParam(name = "category_id", required = false, defaultValue = "-1") int categoryId,
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<VoucherApplicationResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<VoucherApplication> listVoucherApplication = voucherApplicationService.spGListVoucherApplication(voucherId, productId, brandId, categoryId, keySearch,
				status, pagination);

		BaseListDataResponse<VoucherApplicationResponse> listData = new BaseListDataResponse<>();

		listData.setList(new VoucherApplicationResponse().mapToList(listVoucherApplication.getResult()));
		listData.setTotalRecord(listVoucherApplication.getTotalRecord());

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @GetMapping("/{id}")
	public ResponseEntity<BaseResponse<VoucherApplicationResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<VoucherApplicationResponse> response = new BaseResponse<>();
		VoucherApplication voucherApplication = voucherApplicationService.findOne(id);

		if (voucherApplication == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_APPLICATION_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
        response.setData(new VoucherApplicationResponse(voucherApplication));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<VoucherApplicationResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
		BaseResponse<VoucherApplicationResponse> response = new BaseResponse<>();
		VoucherApplication voucherApplication = voucherApplicationService.findOne(id);

		if (voucherApplication == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_APPLICATION_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		voucherApplication.setStatus(voucherApplication.getStatus() == 1 ? 0 : 1);

		voucherApplicationService.update(voucherApplication);
        response.setData(new VoucherApplicationResponse(voucherApplication));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<BaseResponse<VoucherApplicationResponse>> create(
			@Valid @RequestBody CRUDVoucherApplicationRequest wrapper) throws Exception {

		BaseResponse<VoucherApplicationResponse> response = new BaseResponse<>();
		// VoucherApplication voucherApplicationCheck = voucherApplicationService.findByName(wrapper.getName());

		// if (voucherApplicationCheck != null) {
		// 	response.setStatus(HttpStatus.BAD_REQUEST);
		// 	response.setMessageError(StringErrorValue.VOUCHER_APPLICATION_IS_EXIST);
		// 	return new ResponseEntity<>(response, HttpStatus.OK);
		// }

		VoucherApplication voucherApplication = new VoucherApplication();
		// voucherApplication.setName(wrapper.getName());
		voucherApplication.setStatus(1);

		voucherApplicationService.create(voucherApplication);
		response.setData(new VoucherApplicationResponse(voucherApplication));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update")
	public ResponseEntity<BaseResponse<VoucherApplicationResponse>> update(@PathVariable("id") int id,
			@Valid @RequestBody CRUDVoucherApplicationRequest wrapper) throws Exception {

		BaseResponse<VoucherApplicationResponse> response = new BaseResponse<>();
		VoucherApplication voucherApplication = voucherApplicationService.findOne(id);

		if (voucherApplication == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_APPLICATION_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		

		// if (!voucherApplication.getName().equals(wrapper.getName())
		// 		&& voucherApplicationService.findByName(wrapper.getName()) != null) {
		// 	response.setStatus(HttpStatus.BAD_REQUEST);
		// 	response.setMessageError(StringErrorValue.VOUCHER_APPLICATION_IS_EXIST);
		// 	return new ResponseEntity<>(response, HttpStatus.OK);

		// }
		// voucherApplication.setName(wrapper.getName());
		voucherApplicationService.update(voucherApplication);

		response.setData(new VoucherApplicationResponse(voucherApplication));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
