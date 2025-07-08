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
import com.shoes.webshoes.entity.Materials;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDMaterialsRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.MaterialsResponse;
import com.shoes.webshoes.service.MaterialsService;


@RestController
@RequestMapping("/api/v1/materials")
public class MaterialsController  {
    @Autowired
    public MaterialsService materialsService;

    @GetMapping("")
//	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<MaterialsResponse>>> getAll(
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<MaterialsResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<Materials> listMaterials = materialsService.spGListMaterials(keySearch,
				status, pagination);

		BaseListDataResponse<MaterialsResponse> listData = new BaseListDataResponse<>();

		listData.setList(new MaterialsResponse().mapToList(listMaterials.getResult()));
		listData.setTotalRecord(listMaterials.getTotalRecord());

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @GetMapping("/{id}")
	public ResponseEntity<BaseResponse<MaterialsResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<MaterialsResponse> response = new BaseResponse<>();
		Materials materials = materialsService.findOne(id);

		if (materials == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.MATERIALS_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
        response.setData(new MaterialsResponse(materials));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<MaterialsResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
		BaseResponse<MaterialsResponse> response = new BaseResponse<>();
		Materials materials = materialsService.findOne(id);

		if (materials == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.MATERIALS_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		materials.setStatus(materials.getStatus() == 1 ? 0 : 1);

		materialsService.update(materials);
        response.setData(new MaterialsResponse(materials));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<BaseResponse<MaterialsResponse>> create(
			@Valid @RequestBody CRUDMaterialsRequest wrapper) throws Exception {

		BaseResponse<MaterialsResponse> response = new BaseResponse<>();
		Materials materialsCheck = materialsService.findByName(wrapper.getName());

		if (materialsCheck != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.MATERIALS_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Materials materials = new Materials();
		materials.setName(wrapper.getName());
		materials.setStatus(1);

		materialsService.create(materials);
		response.setData(new MaterialsResponse(materials));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update")
	public ResponseEntity<BaseResponse<MaterialsResponse>> update(@PathVariable("id") int id,
			@Valid @RequestBody CRUDMaterialsRequest wrapper) throws Exception {

		BaseResponse<MaterialsResponse> response = new BaseResponse<>();
		Materials materials = materialsService.findOne(id);

		if (materials == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.MATERIALS_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (!materials.getName().equals(wrapper.getName())
				&& materialsService.findByName(wrapper.getName()) != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.MATERIALS_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}
		materials.setName(wrapper.getName());
		materialsService.update(materials);

		response.setData(new MaterialsResponse(materials));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
