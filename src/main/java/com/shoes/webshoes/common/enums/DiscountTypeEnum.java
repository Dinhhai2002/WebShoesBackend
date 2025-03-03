package com.shoes.webshoes.common.enums;

public enum DiscountTypeEnum {
	PERCENT(1), CASH(2);

	private int value;

	private DiscountTypeEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static DiscountTypeEnum valueOf(int value) {
		switch (value) {
		case 1:
			return PERCENT;
		case 2:
			return CASH;
		default:
			return PERCENT;
		}
	}

}
