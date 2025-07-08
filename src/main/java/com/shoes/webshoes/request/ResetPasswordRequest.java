package com.shoes.webshoes.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ResetPasswordRequest {
	
	@NotEmpty(message = "user_name không được phép để trống")
	@JsonProperty("user_name")
	private String userName;
	
	@NotEmpty(message = "new_password không được phép để trống")
	@Length(max = 20, min = 8, message = "độ dài mật khẩu tối đa là 20,tối thiểu là 8")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "password phải vừa có kí tự viết hoa viết thường,có số và kí tự đặc biệt!")
	@JsonProperty("new_password")
	private String newPassword;

	@NotEmpty(message = "confirm_password không được phép để trống")
	@Length(max = 20, message = "Không được phép lớn hơn 20 kí tự")
	@JsonProperty("confirm_password")
	private String confirmPassword;
	
	
	
	
}
