package com.shoes.webshoes.common.enums;

public enum PaymentMethodEnum {
    COD(1),
    VNPAY(2),
    STORE(3);  // Bán hàng tại quầy

    private int value;

    private PaymentMethodEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PaymentMethodEnum valueOf(int value) {
        switch (value) {
            case 1:
                return COD;
            case 2:
                return VNPAY;
            case 3:
                return STORE;
            default:
                return COD;
        }
    }

    // COD - Thanh toán khi nhận hàng
    // VNPAY - Thanh toán qua VNPAY
    // STORE - Thanh toán tại quầy
}