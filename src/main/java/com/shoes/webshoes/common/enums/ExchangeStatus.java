package com.shoes.webshoes.common.enums;

public enum ExchangeStatus {
    PENDING("Chờ xử lý"),
    APPROVED("Đã duyệt"),
    REJECTED("Đã từ chối"),
    PROCESSING("Đang xử lý"),
    COMPLETED("Hoàn thành"),
    CANCELLED("Đã hủy");
    
    private final String description;
    
    ExchangeStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 