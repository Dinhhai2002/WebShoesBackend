package com.shoes.webshoes.common.enums;

public enum OtpEnum {
	REGISTER(0), FORGOTPASSWORD(1);

	private int value;

	private OtpEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static OtpEnum valueOf(int value) {
		switch (value) {
		case 0:
			return REGISTER;
		case 1:
			return FORGOTPASSWORD;

		default:
			return REGISTER;
		}
	}

}
