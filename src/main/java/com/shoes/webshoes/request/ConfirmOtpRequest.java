package com.shoes.webshoes.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ConfirmOtpRequest {
	@NotEmpty(message = "user_name không được phép để trống")
	@Length(max = 255, message = "Không được phép lớn hơn 255 kí tự")
	@JsonProperty("user_name")
	private String userName;

	@NotEmpty(message = "email không được phép để trống")
	@Length(max = 255, message = "Không được phép lớn hơn 255 kí tự")
	@Email(message = "email chưa đúng định dạng")
	private String email;

	@Min(value = 0, message = "giá trị nhỏ nhất 0.")
	private int otp;

	@Min(value = 0, message = "type nhỏ nhất 0.")
	@Max(value = 1, message = "type lớn nhất 1.")
	private int type;

	
}
