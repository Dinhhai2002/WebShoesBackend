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
import com.shoes.webshoes.common.utils.Utils;
import com.shoes.webshoes.entity.Image;
import com.shoes.webshoes.entity.Users;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDUserRequest;
import com.shoes.webshoes.request.ChangePasswordRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.ImageResponse;
import com.shoes.webshoes.response.UserResponse;
import com.shoes.webshoes.service.IFirebaseImageService;
import com.shoes.webshoes.service.ImageService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseController {

	@Autowired
	public IFirebaseImageService iFirebaseImageService;

	@Autowired
	public ImageService imageService;;

	@GetMapping("")
	public ResponseEntity<BaseResponse<BaseListDataResponse<UserResponse>>> getAllUser(
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "role", required = false, defaultValue = "-1") int role,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "20") int limit) throws Exception {

		BaseResponse<BaseListDataResponse<UserResponse>> response = new BaseResponse<>();

		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<Users> listUser = userService.spGUsers(keySearch, status, role, pagination);

		BaseListDataResponse<UserResponse> listData = new BaseListDataResponse<>();

		listData.setList(new UserResponse().mapToList(listUser.getResult()));
		listData.setTotalRecord(listUser.getTotalRecord());

		response.setData(listData);

		response.setData(listData);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PostMapping("/update")
	public ResponseEntity<BaseResponse<UserResponse>> update(@RequestBody CRUDUserRequest wrapper) throws Exception {

		BaseResponse<UserResponse> response = new BaseResponse<>();

		Users user = this.getUser();

		if (!user.getEmail().equals(wrapper.getEmail())) {
			Users findUsersByEmail = userService.findUsersByEmail(wrapper.getEmail(), 0);
			if (findUsersByEmail != null) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError(StringErrorValue.MAIL_USER_IS_EXIST);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}

		if (!user.getPhone().equals(wrapper.getPhone())) {
			Users findUsersByPhone = userService.findUsersByPhone(wrapper.getPhone());
			if (findUsersByPhone != null) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError(StringErrorValue.PHONE_USER_IS_EXIST);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}

//		khi đổi username thì gọi phương thức login để update lại accessToken
//		if (!user.getUserName().equals(wrapper.getUserName())) {
//			String password = new String(Base64.getDecoder().decode(user.getPassword()));
//			String token = HttpService.login(wrapper.getUserName(), password);
//			user.setAccessToken(token);
//		}

//		user.setUserName(wrapper.getUserName());
		user.setFullName(wrapper.getFullName());
		user.setEmail(wrapper.getEmail());
//		user.setGender(wrapper.getGender());
		user.setPhone(wrapper.getPhone());
//		user.setBirthday(this.formatDate(wrapper.getBirthday()));
//		user.setCityId(wrapper.getCityId());
//		user.setDistrictId(wrapper.getDistrictId());
//		user.setWardId(wrapper.getWardId());
		user.setFullAddress(wrapper.getFullAddress());

		userService.update(user);

		response.setData(new UserResponse(user));
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("/detail")
	public ResponseEntity<BaseResponse<UserResponse>> findOne() throws Exception {

		BaseResponse<UserResponse> response = new BaseResponse<>();
		Users user = this.getUser();


		response.setData(new UserResponse(user));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/change-password")
	public ResponseEntity<BaseResponse<UserResponse>> chagePassword(@Valid @RequestBody ChangePasswordRequest wrapper)
			throws Exception {

		BaseResponse<UserResponse> response = new BaseResponse<>();

		Users users = this.getUser();
		String password = Utils.decodeBase64(users.getPassword());

		if (!wrapper.getOldPassword().equals(password)) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PASSWORD_IS_NOT_CORRECT);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (!wrapper.getNewPassword().equals(wrapper.getConfirmPassword())) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.ERROR_CONFIRM_PASSWORD_AND_CONFIRM);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		/*
		 * users.setPassword(BCrypt.hashpw(wrapper.getNewPassword(),
		 * BCrypt.gensalt(12)));
		 */
		users.setPassword(Utils.encodeBase64(wrapper.getNewPassword()));
		userService.update(users);
		response.setData(new UserResponse(users));
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PostMapping("/upload-avatar")
	public ResponseEntity<BaseResponse<ImageResponse>> create(@RequestParam(name = "file") MultipartFile file)
			throws Exception {

		BaseResponse<ImageResponse> response = new BaseResponse<>();
		Users user = this.getUser();

		String fileName = iFirebaseImageService.save(file);

		String imageUrl = iFirebaseImageService.getImageUrl(fileName);

		Image image = new Image();
		image.setUserId(user.getId());
		image.setUrl(imageUrl);

		imageService.create(image);
		user.setAvatarId(image.getId());
		user.setAvatarUrl(image.getUrl());

		userService.update(user);

		response.setData(new ImageResponse(image));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<UserResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
		BaseResponse<UserResponse> response = new BaseResponse<>();
		Users currentUser = this.getUser();
		Users user = userService.findOne(id);

		if (user == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.USER_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (user.getId() == currentUser.getId()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.USER_NOT_LOCK);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}	

		user.setIsActive(user.getIsActive() == 1 ? 0 : 1);

		userService.update(user);
		response.setData(new UserResponse(user));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
