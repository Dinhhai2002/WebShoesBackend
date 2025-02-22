package com.shoes.webshoes.common.enums;

public enum RoleEnum {
	USER(0), ADMIN(1);

	private int value;

	private RoleEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static RoleEnum valueOf(int value) {
		switch (value) {
		case 0:
			return USER;
		case 1:
			return ADMIN;
		default:
			return USER;
		}
	}

	public String getName() {
		switch (this) {
		case USER:
			return "USER";
		case ADMIN:
			return "ADMIN";
		default:
			return "USER";
		}
	}
}
