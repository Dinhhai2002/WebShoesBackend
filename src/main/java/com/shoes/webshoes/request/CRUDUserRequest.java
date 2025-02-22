package com.shoes.webshoes.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CRUDUserRequest {

	@NotEmpty(message = "userName không được phép để trống")
	@Length(max = 255, message = "user_name Không được phép lớn hơn 255 kí tự")
//	@Pattern(regexp = "^[a-zA-Z 0-9 ]*$", message = "userName không chứa kí tự bất kì kí tự đặc biệt nào!")
	@JsonProperty("user_name")
	private String userName;

	@NotEmpty(message = "full_name không được phép để trống")
	@Length(max = 255, message = "fullName Không được phép lớn hơn 255 kí tự")
//	@Pattern(regexp = "^[a-zA-Z ]*$", message = "fullName không chứa kí tự bất kì kí tự đặc biệt nào!")
	@JsonProperty("full_name")
	private String fullName;

	@NotEmpty(message = "email không được phép để trống")
	@Email(message = "email chưa đúng định dạng")
	@Length(max = 255, message = "email Không được phép lớn hơn 255 kí tự")
	private String email;

	@Min(value = 0, message = "gender nhỏ nhất 0.")
	@Max(value = 1, message = "gender lớn nhất 1.")
	private int gender;

	@NotEmpty(message = "phone không được phép để trống")
	@Pattern(regexp = "(^$|[0-9]{10})", message = " chỉ được phép nhập số và tối đa 10 số.")
	private String phone;

//	@NotEmpty(message = "password không được phép để trống")
	@Length(max = 20, min = 8, message = "độ dài mật khẩu tối đa là 20,tối thiểu là 8")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "password phải vừa có kí tự viết hoa viết thường,có số và kí tự đặc biệt!")
	private String password;

	@NotEmpty(message = "birthday không được phép để trống")
	@Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "Ngày sinh phải theo định dạng 'dd/mm/yyyy'")
	private String birthday;

	@Min(value = 1, message = "Vui lòng nhập Tỉnh/Thành phố của nhà hàng")
	@JsonProperty("city_id")
	private int cityId;

	@Schema(description = "Quận/Huyện")
	@Min(value = 1, message = "Vui lòng nhập Quận/Huyện của nhà hàng")
	@JsonProperty("district_id")
	private int districtId;

	@Schema(description = "Phường/Xã")
	@Min(value = 1, message = "Vui lòng nhập Phường/Xã của nhà hàng")
	@JsonProperty("ward_id")
	private int wardId;

	@NotEmpty(message = "fullAddress không được phép để trống")
	@Length(max = 255, message = "Không được phép lớn hơn 255 kí tự")
	@JsonProperty("full_address")
	private String fullAddress;

}
