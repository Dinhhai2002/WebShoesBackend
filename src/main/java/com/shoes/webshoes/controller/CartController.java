package com.shoes.webshoes.controller;

import java.util.List;

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
import com.shoes.webshoes.entity.Cart;
import com.shoes.webshoes.entity.Users;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDCartRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.CartResponse;
import com.shoes.webshoes.service.CartService;


@RestController
@RequestMapping("/api/v1/cart")
public class CartController extends BaseController {
    @Autowired
    public CartService cartService;

    @GetMapping("")
//	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<CartResponse>>> getAll(
			@RequestParam(name = "user_id", required = false, defaultValue = "-1") int userId,
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<CartResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<Cart> listCart = cartService.spGListCart(userId, keySearch,
				status, pagination);

		BaseListDataResponse<CartResponse> listData = new BaseListDataResponse<>();

		listData.setList(new CartResponse().mapToList(listCart.getResult()));
		listData.setTotalRecord(listCart.getTotalRecord());

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @GetMapping("/{id}")
	public ResponseEntity<BaseResponse<CartResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<CartResponse> response = new BaseResponse<>();
		Cart cart = cartService.findOne(id);

		if (cart == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CART_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
        response.setData(new CartResponse(cart));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<CartResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
		BaseResponse<CartResponse> response = new BaseResponse<>();
		Cart cart = cartService.findOne(id);

		if (cart == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CART_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		cart.setStatus(cart.getStatus() == 1 ? 0 : 1);

		cartService.update(cart);
        response.setData(new CartResponse(cart));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<BaseResponse<CartResponse>> create(
			@Valid @RequestBody CRUDCartRequest wrapper) throws Exception {

		BaseResponse<CartResponse> response = new BaseResponse<>();
		Users users = this.getUser();
		List<Cart> listCart = cartService.spGListCart(users.getId(), "",
		1, new Pagination(0, 20)).getResult();

		if(!listCart.isEmpty()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CART_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Cart cart = new Cart();
		cart.setUserId(users.getId());
		cart.setStatus(1);

		cartService.create(cart);
		response.setData(new CartResponse(cart));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update")
	public ResponseEntity<BaseResponse<CartResponse>> update(@PathVariable("id") int id,
			@Valid @RequestBody CRUDCartRequest wrapper) throws Exception {
		Users users = this.getUser();
		BaseResponse<CartResponse> response = new BaseResponse<>();
		Cart cart = cartService.findOne(id);

		if (cart == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CART_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		cart.setUserId(users.getId());
		cartService.update(cart);

		response.setData(new CartResponse(cart));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
