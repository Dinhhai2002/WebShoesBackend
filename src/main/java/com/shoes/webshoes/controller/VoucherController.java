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
import com.shoes.webshoes.common.utils.Utils;
import com.shoes.webshoes.entity.Voucher;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDVoucherRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.VoucherResponse;
import com.shoes.webshoes.service.VoucherService;


@RestController
@RequestMapping("/api/v1/voucher")
public class VoucherController  {
    @Autowired
    public VoucherService voucherService;

    @GetMapping("")
//	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<VoucherResponse>>> getAll(
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<VoucherResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<Voucher> listVoucher = voucherService.spGListVoucher(keySearch,
				status, pagination);

		BaseListDataResponse<VoucherResponse> listData = new BaseListDataResponse<>();

		listData.setList(new VoucherResponse().mapToList(listVoucher.getResult()));
		listData.setTotalRecord(listVoucher.getTotalRecord());

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @GetMapping("/{id}")
	public ResponseEntity<BaseResponse<VoucherResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<VoucherResponse> response = new BaseResponse<>();
		Voucher voucher = voucherService.findOne(id);

		if (voucher == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
        response.setData(new VoucherResponse(voucher));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<VoucherResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
		BaseResponse<VoucherResponse> response = new BaseResponse<>();
		Voucher voucher = voucherService.findOne(id);

		if (voucher == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		voucher.setStatus(voucher.getStatus() == 1 ? 0 : 1);

		voucherService.update(voucher);
        response.setData(new VoucherResponse(voucher));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<BaseResponse<VoucherResponse>> create(
			@Valid @RequestBody CRUDVoucherRequest wrapper) throws Exception {

		BaseResponse<VoucherResponse> response = new BaseResponse<>();
		Voucher voucherCheck = voucherService.findByName(wrapper.getCode());

		if (voucherCheck != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Voucher voucher = new Voucher();
		voucher.setCode(wrapper.getCode());
		voucher.setDiscountType(wrapper.getDiscountType());
		voucher.setDiscountValue(wrapper.getDiscountValue());
		voucher.setMinOrderValue(wrapper.getMinOrderValue());
		voucher.setMaxDiscount(wrapper.getMaxDiscount());
		voucher.setStartDate(Utils.convertStringToDate(wrapper.getStartDate()));
		voucher.setEndDate(Utils.convertStringToDate(wrapper.getEndDate()));
		voucher.setUsageLimit(wrapper.getUsageLimit());
		voucher.setUsedCount(wrapper.getUsedCount());
		voucher.setStatus(1);

		voucherService.create(voucher);
		response.setData(new VoucherResponse(voucher));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update")
	public ResponseEntity<BaseResponse<VoucherResponse>> update(@PathVariable("id") int id,
			@Valid @RequestBody CRUDVoucherRequest wrapper) throws Exception {

		BaseResponse<VoucherResponse> response = new BaseResponse<>();
		Voucher voucher = voucherService.findOne(id);

		if (voucher == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (!voucher.getCode().equals(wrapper.getCode())
				&& voucherService.findByName(wrapper.getCode()) != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.VOUCHER_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}
		voucher.setCode(wrapper.getCode());
		voucher.setDiscountType(wrapper.getDiscountType());
		voucher.setDiscountValue(wrapper.getDiscountValue());
		voucher.setMinOrderValue(wrapper.getMinOrderValue());
		voucher.setMaxDiscount(wrapper.getMaxDiscount());
		voucher.setStartDate(Utils.convertStringToDate(wrapper.getStartDate()));
		voucher.setEndDate(Utils.convertStringToDate(wrapper.getEndDate()));
		voucher.setUsageLimit(wrapper.getUsageLimit());
		voucher.setUsedCount(wrapper.getUsedCount());
		voucher.setStatus(1);
		voucherService.update(voucher);

		response.setData(new VoucherResponse(voucher));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
