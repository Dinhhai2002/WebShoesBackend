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
import com.shoes.webshoes.entity.Size;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDSizeRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.SizeResponse;
import com.shoes.webshoes.service.SizeService;


@RestController
@RequestMapping("/api/v1/size")
public class SizeController  {
    @Autowired
    public SizeService sizeService;

    @GetMapping("")
//	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<SizeResponse>>> getAll(
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<SizeResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<Size> listSize = sizeService.spGListSize(keySearch,
				status, pagination);

		BaseListDataResponse<SizeResponse> listData = new BaseListDataResponse<>();

		listData.setList(new SizeResponse().mapToList(listSize.getResult()));
		listData.setTotalRecord(listSize.getTotalRecord());

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @GetMapping("/{id}")
	public ResponseEntity<BaseResponse<SizeResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<SizeResponse> response = new BaseResponse<>();
		Size size = sizeService.findOne(id);

		if (size == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.SIZE_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
        response.setData(new SizeResponse(size));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<SizeResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
		BaseResponse<SizeResponse> response = new BaseResponse<>();
		Size size = sizeService.findOne(id);

		if (size == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.SIZE_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		size.setStatus(size.getStatus() == 1 ? 0 : 1);

		sizeService.update(size);
        response.setData(new SizeResponse(size));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<BaseResponse<SizeResponse>> create(
			@Valid @RequestBody CRUDSizeRequest wrapper) throws Exception {

		BaseResponse<SizeResponse> response = new BaseResponse<>();
		Size sizeCheck = sizeService.findByName(wrapper.getName());

		if (sizeCheck != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.SIZE_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Size size = new Size();
		size.setName(wrapper.getName());
		size.setStatus(1);

		sizeService.create(size);
		response.setData(new SizeResponse(size));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update")
	public ResponseEntity<BaseResponse<SizeResponse>> update(@PathVariable("id") int id,
			@Valid @RequestBody CRUDSizeRequest wrapper) throws Exception {

		BaseResponse<SizeResponse> response = new BaseResponse<>();
		Size size = sizeService.findOne(id);

		if (size == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.SIZE_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (!size.getName().equals(wrapper.getName())
				&& sizeService.findByName(wrapper.getName()) != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.SIZE_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}
		size.setName(wrapper.getName());
		sizeService.update(size);

		response.setData(new SizeResponse(size));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
