package com.shoes.webshoes.common.enums;

public enum RoleEnum {
	USER(1), ADMIN(2), STAFF(3);

	private int value;

	private RoleEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static RoleEnum valueOf(int value) {
		switch (value) {
		case 1:
			return USER;
		case 2:
			return ADMIN;
		case 3:
			return STAFF;
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
		case STAFF:
			return "STAFF";
		default:
			return "USER";
		}
	}
}
