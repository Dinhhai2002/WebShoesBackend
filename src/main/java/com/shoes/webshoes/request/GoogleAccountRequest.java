package com.shoes.webshoes.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GoogleAccountRequest {

	@NotEmpty(message = "email không được phép để trống")
	@Email(message = "email chưa đúng định dạng")
	@Length(max = 255, message = "email Không được phép lớn hơn 255 kí tự")
	private String email;

	@JsonProperty("image_url")
	@NotEmpty(message = "Đường dẫn hình ảnh không được phép để trống")
	private String imageUrl;

	@NotEmpty(message = "full_name không được phép để trống")
	@Length(max = 255, message = "fullName Không được phép lớn hơn 255 kí tự")
	private String fullname;

	
}
