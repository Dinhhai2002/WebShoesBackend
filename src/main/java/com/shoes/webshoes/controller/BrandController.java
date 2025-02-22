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
import com.shoes.webshoes.entity.Brand;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDBrandRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.BrandResponse;
import com.shoes.webshoes.service.BrandService;


@RestController
@RequestMapping("/api/v1/brand")
public class BrandController  {
    @Autowired
    public BrandService brandService;

    @GetMapping("")
//	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<BrandResponse>>> getAll(
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<BrandResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<Brand> listBrand = brandService.spGListBrand(keySearch,
				status, pagination);

		BaseListDataResponse<BrandResponse> listData = new BaseListDataResponse<>();

		listData.setList(new BrandResponse().mapToList(listBrand.getResult()));
		listData.setTotalRecord(listBrand.getTotalRecord());

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @GetMapping("/{id}")
	public ResponseEntity<BaseResponse<BrandResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<BrandResponse> response = new BaseResponse<>();
		Brand brand = brandService.findOne(id);

		if (brand == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.BRAND_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
        response.setData(new BrandResponse(brand));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BrandResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
		BaseResponse<BrandResponse> response = new BaseResponse<>();
		Brand brand = brandService.findOne(id);

		if (brand == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.BRAND_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		brand.setStatus(brand.getStatus() == 1 ? 0 : 1);

		brandService.update(brand);
        response.setData(new BrandResponse(brand));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<BaseResponse<BrandResponse>> create(
			@Valid @RequestBody CRUDBrandRequest wrapper) throws Exception {

		BaseResponse<BrandResponse> response = new BaseResponse<>();
		Brand brandCheck = brandService.findByName(wrapper.getName());

		if (brandCheck != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.BRAND_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Brand brand = new Brand();
		brand.setName(wrapper.getName());
		brand.setStatus(1);

		brandService.create(brand);
		response.setData(new BrandResponse(brand));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update")
	public ResponseEntity<BaseResponse<BrandResponse>> update(@PathVariable("id") int id,
			@Valid @RequestBody CRUDBrandRequest wrapper) throws Exception {

		BaseResponse<BrandResponse> response = new BaseResponse<>();
		Brand brand = brandService.findOne(id);

		if (brand == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.BRAND_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (!brand.getName().equals(wrapper.getName())
				&& brandService.findByName(wrapper.getName()) != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.BRAND_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}
		brand.setName(wrapper.getName());
		brandService.update(brand);

		response.setData(new BrandResponse(brand));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
