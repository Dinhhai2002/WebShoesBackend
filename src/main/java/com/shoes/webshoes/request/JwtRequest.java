package com.shoes.webshoes.request;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class JwtRequest implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;

	@JsonProperty("user_name")
	@NotEmpty(message = "userName không được phép để trống")
	@Length(max = 255, message = "user_name Không được phép lớn hơn 255 kí tự")
//	@Pattern(regexp = "^[a-zA-Z 0-9 ]*$", message = "userName không chứa kí tự bất kì kí tự đặc biệt nào!")
	private String username;

	@Length(max = 20, min = 8, message = "độ dài mật khẩu tối đa là 20,tối thiểu là 8")
//	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "password phải vừa có kí tự viết hoa viết thường,có số và kí tự đặc biệt!")
	private String password;

}