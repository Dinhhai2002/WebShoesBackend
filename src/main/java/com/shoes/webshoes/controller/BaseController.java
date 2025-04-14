/**
* 
*/
package com.shoes.webshoes.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.shoes.webshoes.common.enums.DiscountTypeEnum;
import com.shoes.webshoes.entity.Cities;
import com.shoes.webshoes.entity.Users;
import com.shoes.webshoes.entity.Voucher;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.CityResponse;
import com.shoes.webshoes.response.DistrictResponse;
import com.shoes.webshoes.response.WardsResponse;
import com.shoes.webshoes.security.JwtTokenUtil;
import com.shoes.webshoes.service.CityService;
import com.shoes.webshoes.service.DistrictService;
import com.shoes.webshoes.service.UserService;
import com.shoes.webshoes.service.WardsService;
import com.shoes.webshoes.service.impl.JwtUserDetailsService;
import com.shoes.webshoes.service.impl.SendEmail;

@RestController
public class BaseController {
	@Autowired
	public JwtTokenUtil jwtTokenUtil;

	@Autowired
	public JwtUserDetailsService userDetailsService;

	@Autowired
	public SendEmail sendEmail;

	 @Autowired
	 public CityService cityService;

	 @Autowired
	 public DistrictService districtService;

	 @Autowired
	 public WardsService wardsService;


	@Autowired
	public UserService userService;

	// @Autowired
	// public IFirebaseImageService iFirebaseImageService;


	@Autowired
	public com.shoes.webshoes.security.ApplicationProperties applicationProperties;

	// time 3 phút
	public static final long TIME_OTP_EXPIRED = 1000 * 60 * 3;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public final ResponseEntity<BaseResponse> handleUserNotFoundException(MethodArgumentNotValidException ex,
			WebRequest request) {

		BaseResponse response = new BaseResponse();
		response.setStatus(HttpStatus.BAD_REQUEST);
		response.setMessageError("Dữ liệu không hợp lệ");

		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

		response.setData(errors);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<BaseResponse> handleMissingParams(MissingServletRequestParameterException ex) {
		// Actual exception handling
		BaseResponse response = new BaseResponse();
		response.setStatus(HttpStatus.BAD_REQUEST);
		response.setMessageError(String.format("%s is required!", ex.getParameterName()));
		response.setData(null);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public String formatDate(String inputDate) throws ParseException {
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date inputDate1 = inputDateFormat.parse(inputDate);

		SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return outputDateFormat.format(inputDate1);

	}

	
	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	}

	public String getRequestHeaderAccessToken() {
		String authorizeHeader = this.getRequest().getHeader("Authorization");
		return authorizeHeader.replace("Bearer ", "");
	}

	@SuppressWarnings("unused")
	public Users getUser() throws Exception {

		String username = null;
		String jwtToken = null;

		jwtToken = this.getRequestHeaderAccessToken();
		username = jwtTokenUtil.getUsernameFromToken(jwtToken);

		Users user = userService.findUsersByUsersName(username);

		if (user.getIsLogin() == 0 && user.getAccessToken() == "")
			throw new Exception("Tài khoản chưa đăng nhập");
		if (user != null)
			return user;
		else
			throw new Exception("Thất bại");
    }
	public static <T> List<T> getListWithExceptionHandler(Callable<List<T>> callable) {
		try {
			return callable.call();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getOneWithExceptionHandler(Callable<T> callable) {
		try {
			return callable.call();
		} catch (Exception e) {
			e.printStackTrace();
			return (T) new Object();
		}
	}

	public long caculateOtpExpired(Date otpDate) {
		Date currentDate = new Date();
		return currentDate.getTime() - (otpDate.getTime() + TIME_OTP_EXPIRED);

	}

	public static BigDecimal calculateTotalAmountApplyVoucher(BigDecimal amount, Voucher voucher) {
		BigDecimal amountVoucher = BigDecimal.ZERO;
		if(voucher.getDiscountType() == DiscountTypeEnum.PERCENT.getValue()) {
			amountVoucher = amount.multiply(voucher.getDiscountValue().divide(BigDecimal.valueOf(100)));
		} else if(voucher.getDiscountType() == DiscountTypeEnum.CASH.getValue()) {
			amountVoucher = voucher.getDiscountValue();
		}
		if(amountVoucher.compareTo(voucher.getMaxDiscount()) > 0) {
			amountVoucher = voucher.getMaxDiscount();
		}
		return amountVoucher;
	}
	
	@GetMapping("/get-all-city")
	public ResponseEntity<BaseResponse<List<CityResponse>>> findAllCity() throws Exception {

		BaseResponse<List<CityResponse>> response = new BaseResponse<>();

		List<Cities> cities = cityService.getAll();

		response.setData(new CityResponse().mapToList(cities));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}/get-district-by-city")
	public ResponseEntity<BaseResponse<List<DistrictResponse>>> findDistrictByCityId(@PathVariable("id") int id)
			throws Exception {

		BaseResponse<List<DistrictResponse>> response = new BaseResponse<>();

		response.setData(new DistrictResponse().mapToList(districtService.findByCityId(id)));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}/get-ward-by-district")
	public ResponseEntity<BaseResponse<List<WardsResponse>>> findWardByDistrictId(@PathVariable("id") int id)
			throws Exception {

		BaseResponse<List<WardsResponse>> response = new BaseResponse<>();

		response.setData(new WardsResponse().mapToList(wardsService.findByDistrictId(id)));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
