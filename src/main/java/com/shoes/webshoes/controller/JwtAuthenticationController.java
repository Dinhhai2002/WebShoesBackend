package com.shoes.webshoes.controller;

import java.util.Date;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoes.webshoes.common.enums.OtpEnum;
import com.shoes.webshoes.common.utils.HttpService;
import com.shoes.webshoes.common.utils.StringErrorValue;
import com.shoes.webshoes.common.utils.Utils;
import com.shoes.webshoes.entity.UserRegister;
import com.shoes.webshoes.entity.Users;
import com.shoes.webshoes.request.CRUDUserRequest;
import com.shoes.webshoes.request.ConfirmOtpRequest;
import com.shoes.webshoes.request.GoogleAccountRequest;
import com.shoes.webshoes.request.JwtRequest;
import com.shoes.webshoes.request.OTPRegisterUserRequest;
import com.shoes.webshoes.request.OTPRequest;
import com.shoes.webshoes.request.ResetPasswordRequest;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.JwtResponse;
import com.shoes.webshoes.response.UserResponse;
import com.shoes.webshoes.service.UserRegisterService;


@RestController
@RequestMapping("/api/v1/authentication")
public class JwtAuthenticationController extends BaseController {
    @Autowired
	public UserRegisterService userRegisterService;
    
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<JwtResponse>> createAuthenticationToken(@RequestBody JwtRequest wrapper)
            throws Exception {
        BaseResponse<JwtResponse> response = new BaseResponse<>();
        Users user = userService.findUsersByUsersNameAndPassword(wrapper.getUsername(),
                Utils.encodeBase64(wrapper.getPassword()));

        if (user == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.LOGIN_FAIL);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(wrapper.getUsername());

        if (user.getIsActive() == 0) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.USER_IS_LOCKED);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        String token = jwtTokenUtil.generateToken(userDetails);
        user.setAccessToken(token);
        userService.update(user);
        response.setData(new JwtResponse(token));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<UserResponse>> spUCreateUser(@Valid @RequestBody CRUDUserRequest wrapper)
            throws Exception {
        BaseResponse<UserResponse> response = new BaseResponse<>();
        // BCrypt.hashpw(wrapper.getPassword(), BCrypt.gensalt(12))

        response.setData(new UserResponse(userService.spUCreateUsers(wrapper.getUserName(), wrapper.getFullName(),
                wrapper.getEmail(), wrapper.getPhone(), Utils.encodeBase64(wrapper.getPassword()), wrapper.getGender(),
                this.formatDate(wrapper.getBirthday()), wrapper.getWardId(), wrapper.getDistrictId(),
                wrapper.getCityId(), wrapper.getFullAddress())));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/reset-password")
    public ResponseEntity<BaseResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest wrapper)
            throws Exception {

        BaseResponse response = new BaseResponse<>();

        Users user = userService.findUsersByUsersName(wrapper.getUserName());

        if (user == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.USER_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (!wrapper.getNewPassword().equals(wrapper.getConfirmPassword())) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.ERROR_CONFIRM_PASSWORD_AND_CONFIRM);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (user.getIsConfirmOtp() == 0) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.OTP_IS_NOT_CONFIRM);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        user.setPassword(Utils.encodeBase64(wrapper.getNewPassword()));
        user.setIsConfirmOtp(0);

        userService.update(user);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/otp-register")
    public ResponseEntity<BaseResponse> otpRegister(@Valid @RequestBody OTPRegisterUserRequest wrapper)
            throws Exception {

        BaseResponse<Object> response = new BaseResponse<>();

        if (userService.findUsersByUsersName(wrapper.getUserName()) != null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.NAME_USER_IS_EXIST);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (userService.findUsersByEmail(wrapper.getEmail(), 0) != null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.MAIL_USER_IS_EXIST);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (userService.findUsersByPhone(wrapper.getPhone()) != null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.PHONE_USER_IS_EXIST);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        /*
         * - lấy ra userRegister -> Nếu có thì kiểm tra mã otp còn hạn hay không + Nếu
         * hết thì set status =0 + Ngược lại thì thông báo tài khoản này đăng có người
         * khác xác thực
         */
        UserRegister checkUserRegister = userRegisterService.findUsersRegisterByUsersNameAndEmail(wrapper.getUserName(),
                wrapper.getEmail());
        if (checkUserRegister != null) {

            if (this.caculateOtpExpired(checkUserRegister.getOtpCreatedAt()) > 0) {
                checkUserRegister.setStatus(0);
                userRegisterService.update(checkUserRegister);
            }

            else {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessageError(StringErrorValue.USER_REGISTER_IS_AUTHENTICATING);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }

        Random rand = new Random();
        int otpvalue = rand.nextInt(1255650);
        sendEmail.sendSimpleEmail(wrapper.getEmail(), "Mã OTP",
                "Mã OTP là:" + otpvalue + ". Mã otp này có thời hạn là 3p");

        UserRegister userRegister = new UserRegister();
        userRegister.setUserName(wrapper.getUserName());
        userRegister.setEmail(wrapper.getEmail());
        userRegister.setOtp(otpvalue);
        userRegister.setOtpCreatedAt(new Date());
        userRegister.setStatus(1);

        userRegisterService.create(userRegister);

        response.setData(otpvalue);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/otp")
    public ResponseEntity<BaseResponse> otpForgot(@Valid @RequestBody OTPRequest wrapper) throws Exception {

        BaseResponse<Object> response = new BaseResponse<>();
        Users user = userService.findUsersByUsersNameAndEmail(wrapper.getUserName(), wrapper.getEmail());

        if (user == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.USER_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (user.getIsGoogle() == 1) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.ACCOUNT_GOOLE_IS_NOT_PERMIT);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Random rand = new Random();
        int otpvalue = rand.nextInt(1255650);
        sendEmail.sendSimpleEmail(wrapper.getEmail(), "Mã OTP",
                "Mã OTP là:" + otpvalue + ". Mã otp này có thời hạn là 3p");

        user.setOtp(otpvalue);
        user.setOtpCreatedAt(new Date());

        userService.update(user);

        response.setData(otpvalue);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/confirm-otp")
    public ResponseEntity<BaseResponse> confirmOtp(@Valid @RequestBody ConfirmOtpRequest wrapper) throws Exception {

        BaseResponse<Object> response = new BaseResponse<>();

        /*
         * type = 0 => otp register || type = 1 => otp forgot password
         */

        if (wrapper.getType() == OtpEnum.REGISTER.getValue()) {

            UserRegister userRegister = userRegisterService.findUsersRegisterByUsersNameAndEmail(wrapper.getUserName(),
                    wrapper.getEmail());

            if (userRegister == null) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessageError(StringErrorValue.OTP_IS_NOT_USING);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            if (userRegister.getOtp() != wrapper.getOtp()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessageError(StringErrorValue.OTP_IS_NOT_CORRECT);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            // xử lí thời gian mã OTP.Quy định mã otp có thời hạn trong 3 phút

            if (this.caculateOtpExpired(userRegister.getOtpCreatedAt()) > 0) {
                userRegister.setStatus(0);
                userRegisterService.update(userRegister);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessageError(StringErrorValue.OTP_IS_EXPIRED);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } else {
            Users user = userService.findUsersByUsersNameAndEmail(wrapper.getUserName(), wrapper.getEmail());
            if (user == null) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessageError(StringErrorValue.USER_NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            if (user.getOtp() != wrapper.getOtp()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessageError(StringErrorValue.OTP_IS_NOT_CORRECT);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            // xử lí thời gian mã OTP.Quy định mã otp có thời hạn trong 3 phút
            if (this.caculateOtpExpired(user.getOtpCreatedAt()) > 0) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessageError(StringErrorValue.OTP_IS_EXPIRED);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            user.setIsConfirmOtp(1);
            userService.update(user);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login-google")
    public ResponseEntity<BaseResponse<JwtResponse>> loginGoogle(@Valid @RequestBody GoogleAccountRequest wrapper)
            throws Exception {

        BaseResponse<JwtResponse> response = new BaseResponse<>();
        Users registerUser = new Users();
        String token;
        Users users = userService.findUsersByEmail(wrapper.getEmail(), 1);

        // Nếu chưa có user thì tạo và gọi api login để set Token
        if (users == null) {
            registerUser.setEmail(wrapper.getEmail());
            registerUser.setUserName(wrapper.getEmail());
            registerUser.setAvatarUrl(wrapper.getImageUrl());
            registerUser.setIsGoogle(1);
            registerUser.setPassword(Utils.encodeBase64(applicationProperties.getPasswordAccountGoogle()));
            registerUser.setFullName(wrapper.getFullname());
            registerUser.setIsActive(1);
            userService.create(registerUser);

            token = HttpService.login(wrapper.getEmail(), applicationProperties.getPasswordAccountGoogle(),
                    applicationProperties.getBaseUrl());
            registerUser.setAccessToken(token);
            registerUser.setIsLogin(1);
            response.setData(new JwtResponse(token));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        token = HttpService.login(users.getUserName(), applicationProperties.getPasswordAccountGoogle(),
                applicationProperties.getBaseUrl());
        registerUser.setIsLogin(1);
        response.setData(new JwtResponse(token));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
