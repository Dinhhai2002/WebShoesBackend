package com.shoes.webshoes.request;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ChangePasswordRequest {

	@Length(max = 20, min = 8, message = "độ dài mật khẩu tối đa là 20,tối thiểu là 8")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "Mật khẩu cũ phải vừa có kí tự viết hoa viết thường,có số và kí tự đặc biệt!")
	@JsonProperty("old_password")
	private String oldPassword;

	@Length(max = 20, min = 8, message = "độ dài mật khẩu tối đa là 20,tối thiểu là 8")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "Mật khẩu mới phải vừa có kí tự viết hoa viết thường,có số và kí tự đặc biệt!")
	@JsonProperty("new_password")
	private String newPassword;

	@Length(max = 20, min = 8, message = "độ dài mật khẩu tối đa là 20,tối thiểu là 8")
	@JsonProperty("confirm_password")
	private String confirmPassword;

	
}
