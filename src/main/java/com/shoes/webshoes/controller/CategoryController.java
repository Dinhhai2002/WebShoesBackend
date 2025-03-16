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
import org.springframework.web.multipart.MultipartFile;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.common.utils.StringErrorValue;
import com.shoes.webshoes.entity.Category;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDCategoryRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.CategoryResponse;
import com.shoes.webshoes.service.CategoryService;
import com.shoes.webshoes.service.impl.FirebaseImageService;


@RestController
@RequestMapping("/api/v1/category")
public class CategoryController  {
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
	public FirebaseImageService iFirebaseImageService;

    @GetMapping("")
//	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<CategoryResponse>>> getAll(
			@RequestParam(name = "parent_id", required = false, defaultValue = "-1") int parentId,
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<CategoryResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<Category> listCategory = categoryService.spGListCategory(
				parentId, keySearch, status, pagination);

		BaseListDataResponse<CategoryResponse> listData = new BaseListDataResponse<>();

		listData.setList(new CategoryResponse().mapToList(listCategory.getResult()));
		listData.setTotalRecord(listCategory.getTotalRecord());

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @GetMapping("/{id}")
	public ResponseEntity<BaseResponse<CategoryResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<CategoryResponse> response = new BaseResponse<>();
		Category category = categoryService.findOne(id);

		if (category == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CATEGORY_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
        response.setData(new CategoryResponse(category));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<CategoryResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
		BaseResponse<CategoryResponse> response = new BaseResponse<>();
		Category category = categoryService.findOne(id);

		if (category == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CATEGORY_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		category.setStatus(category.getStatus() == 1 ? 0 : 1);

		categoryService.update(category);
        response.setData(new CategoryResponse(category));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<CategoryResponse>> create(
			@Valid @RequestBody CRUDCategoryRequest wrapper) throws Exception {

		BaseResponse<CategoryResponse> response = new BaseResponse<>();
		Category categoryCheck = categoryService.findByName(wrapper.getName());

		if (categoryCheck != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CATEGORY_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Category category = new Category();
		category.setName(wrapper.getName());
		category.setParentId(wrapper.getParentId());
		category.setImageUrl(wrapper.getImageUrl());
		category.setStatus(1);

		categoryService.create(category);
		response.setData(new CategoryResponse(category));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<CategoryResponse>> update(@PathVariable("id") int id,
			@Valid @RequestBody CRUDCategoryRequest wrapper) throws Exception {

		BaseResponse<CategoryResponse> response = new BaseResponse<>();
		Category category = categoryService.findOne(id);

		if (category == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CATEGORY_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// check name đã tồn tại hay chưa
		if (!category.getName().equals(wrapper.getName())
				&& categoryService.findByName(wrapper.getName()) != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CATEGORY_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}
		category.setName(wrapper.getName());
		category.setParentId(wrapper.getParentId());
		category.setImageUrl(wrapper.getImageUrl());
		categoryService.update(category);

		response.setData(new CategoryResponse(category));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PostMapping("/{id}/image")
	public ResponseEntity<BaseResponse> uploadBanner(@RequestParam(name = "file") MultipartFile file,
			@PathVariable("id") int id) throws Exception {
		BaseResponse response = new BaseResponse();
		Category category = categoryService.findOne(id);

		if (category == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CATEGORY_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		String fileName = iFirebaseImageService.save(file);

		String imageUrl = iFirebaseImageService.getImageUrl(fileName);

		category.setImageUrl(imageUrl);
		categoryService.update(category);

		response.setData(new CategoryResponse(category));
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
