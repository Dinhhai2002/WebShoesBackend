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
import com.shoes.webshoes.entity.Category;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDCategoryRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.CategoryResponse;
import com.shoes.webshoes.service.CategoryService;


@RestController
@RequestMapping("/api/v1/category")
public class CategoryController  {
    @Autowired
    public CategoryService categoryService;

    @GetMapping("")
//	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<CategoryResponse>>> getAll(
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<CategoryResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<Category> listCategory = categoryService.spGListCategory(keySearch,
				status, pagination);

		BaseListDataResponse<CategoryResponse> listData = new BaseListDataResponse<>();

		listData.setList(new CategoryResponse().mapToList(listCategory.getResult()));
		listData.setTotalRecord(listCategory.getTotalRecord());

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @GetMapping("/{id}")
	public ResponseEntity<BaseResponse<CategoryResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<CategoryResponse> response = new BaseResponse<>();
		Category Category = categoryService.findOne(id);

		if (Category == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CATEGORY_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
        response.setData(new CategoryResponse(Category));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<CategoryResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
		BaseResponse<CategoryResponse> response = new BaseResponse<>();
		Category Category = categoryService.findOne(id);

		if (Category == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CATEGORY_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Category.setStatus(Category.getStatus() == 1 ? 0 : 1);

		categoryService.update(Category);
        response.setData(new CategoryResponse(Category));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<BaseResponse<CategoryResponse>> create(
			@Valid @RequestBody CRUDCategoryRequest wrapper) throws Exception {

		BaseResponse<CategoryResponse> response = new BaseResponse<>();
		Category CategoryCheck = categoryService.findByName(wrapper.getName());

		if (CategoryCheck != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CATEGORY_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Category Category = new Category();
		Category.setName(wrapper.getName());
		Category.setStatus(1);

		categoryService.create(Category);
		response.setData(new CategoryResponse(Category));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update")
	public ResponseEntity<BaseResponse<CategoryResponse>> update(@PathVariable("id") int id,
			@Valid @RequestBody CRUDCategoryRequest wrapper) throws Exception {

		BaseResponse<CategoryResponse> response = new BaseResponse<>();
		Category Category = categoryService.findOne(id);

		if (Category == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CATEGORY_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// check name đã tồn tại hay chưa
		if (!Category.getName().equals(wrapper.getName())
				&& categoryService.findByName(wrapper.getName()) != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CATEGORY_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}
		Category.setName(wrapper.getName());
		categoryService.update(Category);

		response.setData(new CategoryResponse(Category));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
