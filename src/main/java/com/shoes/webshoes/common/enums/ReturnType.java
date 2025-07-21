package com.shoes.webshoes.common.enums;

public enum ReturnType {
    // Hoàn trả hàng
    RETURN_FULL("Hoàn trả 100% đơn hàng"),
    RETURN_PARTIAL("Hoàn trả một phần đơn hàng"),
    
    // Đổi hàng
    EXCHANGE_FULL("Đổi 100% đơn hàng"),
    EXCHANGE_PARTIAL("Đổi một phần đơn hàng"),
    
    // Legacy support (deprecated)
    REFUND("Hoàn tiền"),
    EXCHANGE("Đổi hàng"),
    PARTIAL_REFUND("Hoàn tiền một phần");
    
    private String description;
    
    ReturnType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Kiểm tra xem có phải là loại hoàn trả hàng không
     */
    public boolean isReturnType() {
        return this == RETURN_FULL || this == RETURN_PARTIAL || this == REFUND || this == PARTIAL_REFUND;
    }
    
    /**
     * Kiểm tra xem có phải là loại đổi hàng không
     */
    public boolean isExchangeType() {
        return this == EXCHANGE_FULL || this == EXCHANGE_PARTIAL || this == EXCHANGE;
    }
    
    /**
     * Kiểm tra xem có phải là hoàn trả/đổi 100% không
     */
    public boolean isFullType() {
        return this == RETURN_FULL || this == EXCHANGE_FULL || this == REFUND || this == EXCHANGE;
    }
    
    /**
     * Kiểm tra xem có phải là hoàn trả/đổi một phần không
     */
    public boolean isPartialType() {
        return this == RETURN_PARTIAL || this == EXCHANGE_PARTIAL || this == PARTIAL_REFUND;
    }
} 