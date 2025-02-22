package com.shoes.webshoes.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OTPRequest {
	@NotEmpty(message = "userName không được phép để trống")
	@Length(max = 255, message = "user_name Không được phép lớn hơn 255 kí tự")
	@Pattern(regexp = "^[a-zA-Z 0-9 ]*$", message = "userName không chứa kí tự bất kì kí tự đặc biệt nào!")
	@JsonProperty("user_name")
	private String userName;
	
	@NotEmpty(message = "email không được phép để trống")
	@Email(message = "email chưa đúng định dạng")
	@Length(max = 255, message = "email Không được phép lớn hơn 255 kí tự")
	private String email;

	
	
}
