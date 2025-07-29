package com.shoes.webshoes.request;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class SaveForLaterRequest {
    @JsonProperty("user_id")
    private Integer userId;
    
    @JsonProperty("product_detail_id")
    private Integer productDetailId;
    
    @JsonProperty("cart_detail_id")
    private Integer cartDetailId; // ID của cart detail để xóa sau khi lưu
} 