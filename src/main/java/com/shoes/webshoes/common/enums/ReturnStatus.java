package com.shoes.webshoes.common.enums;

public enum ReturnStatus {
    PENDING("Chờ duyệt"),
    APPROVED("Đã duyệt"),
    REJECTED("Từ chối"),
    PROCESSING("Đang xử lý"),
    COMPLETED("Hoàn thành"),
    CANCELLED("Đã hủy");
    
    private String description;
    
    ReturnStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 