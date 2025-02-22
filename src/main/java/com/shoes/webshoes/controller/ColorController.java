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
import com.shoes.webshoes.entity.Color;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDColorRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.ColorResponse;
import com.shoes.webshoes.service.ColorService;


@RestController
@RequestMapping("/api/v1/color")
public class ColorController  {
    @Autowired
    public ColorService colorService;

    @GetMapping("")
//	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<ColorResponse>>> getAll(
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<ColorResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<Color> listColor = colorService.spGListColor(keySearch,
				status, pagination);

		BaseListDataResponse<ColorResponse> listData = new BaseListDataResponse<>();

		listData.setList(new ColorResponse().mapToList(listColor.getResult()));
		listData.setTotalRecord(listColor.getTotalRecord());

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @GetMapping("/{id}")
	public ResponseEntity<BaseResponse<ColorResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<ColorResponse> response = new BaseResponse<>();
		Color color = colorService.findOne(id);

		if (color == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.COLOR_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
        response.setData(new ColorResponse(color));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<ColorResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
		BaseResponse<ColorResponse> response = new BaseResponse<>();
		Color color = colorService.findOne(id);

		if (color == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.COLOR_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		color.setStatus(color.getStatus() == 1 ? 0 : 1);

		colorService.update(color);
        response.setData(new ColorResponse(color));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<BaseResponse<ColorResponse>> create(
			@Valid @RequestBody CRUDColorRequest wrapper) throws Exception {

		BaseResponse<ColorResponse> response = new BaseResponse<>();
		Color colorCheck = colorService.findByName(wrapper.getName());

		if (colorCheck != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.COLOR_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Color color = new Color();
		color.setName(wrapper.getName());
		color.setStatus(1);

		colorService.create(color);
		response.setData(new ColorResponse(color));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update")
	public ResponseEntity<BaseResponse<ColorResponse>> update(@PathVariable("id") int id,
			@Valid @RequestBody CRUDColorRequest wrapper) throws Exception {

		BaseResponse<ColorResponse> response = new BaseResponse<>();
		Color color = colorService.findOne(id);

		if (color == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.COLOR_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (!color.getName().equals(wrapper.getName())
				&& colorService.findByName(wrapper.getName()) != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.COLOR_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}
		color.setName(wrapper.getName());
		colorService.update(color);

		response.setData(new ColorResponse(color));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
