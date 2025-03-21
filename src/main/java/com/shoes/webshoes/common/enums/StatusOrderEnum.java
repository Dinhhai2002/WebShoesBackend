package com.shoes.webshoes.common.enums;

public enum StatusOrderEnum {
	PENDING(1), 
    CONFIRMED(2),
    PROCESSING(3),
    SHIPPED(4),
    DELIVERED(5),
    CANCELLED(6);

	private int value;

	private StatusOrderEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static StatusOrderEnum valueOf(int value) {
		switch (value) {
		case 1:
			return PENDING;
		case 2:
			return CONFIRMED;
        case 3:
            return PROCESSING;
        case 4:
            return SHIPPED;
        case 5:
            return DELIVERED;
        case 6:
            return CANCELLED;
		default:
			return PENDING;
		}
	}

    public static boolean isValidStatus(int status) {
        for (StatusOrderEnum orderStatus : StatusOrderEnum.values()) {
            if (orderStatus.getValue() == status) {
                return true;
            }
        }
        return false;
    }

    // PENDING – Đơn hàng mới được tạo, đang chờ xác nhận.
    // CONFIRMED – Đơn hàng đã được xác nhận bởi hệ thống hoặc người bán.
    // PROCESSING – Đơn hàng đang được chuẩn bị (đóng gói, xuất kho, v.v.).
    // SHIPPED – Đơn hàng đã được gửi đi cho đơn vị vận chuyển.
    // DELIVERED – Đơn hàng đã được giao thành công đến khách hàng.
    // CANCELLED – Đơn hàng bị hủy (do khách hàng hoặc người bán)

}
