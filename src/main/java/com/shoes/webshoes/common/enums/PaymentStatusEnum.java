package com.shoes.webshoes.common.enums;

public enum PaymentStatusEnum {
	PENDING(1), 
    PROCESSING(2),
    PAID(3),
    FAILED(4);

	private int value;

	private PaymentStatusEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static PaymentStatusEnum valueOf(int value) {
		switch (value) {
		case 1:
			return PENDING;
		case 2:
			return PROCESSING;
        case 3:
            return PAID;
        case 4:
            return FAILED;
		default:
			return PENDING;
		}
	}
    // Chưa thanh toán – Đơn hàng đã được tạo nhưng chưa thực hiện thanh toán.
    // Đang chờ thanh toán – Đã chọn phương thức thanh toán online nhưng chưa hoàn tất giao dịch.
    // Đã thanh toán – Thanh toán đã được xác nhận thành công.
    // Thanh toán thất bại – Thanh toán không thành công (có thể do lỗi hệ thống, thẻ bị từ chối, v.v.).

}
