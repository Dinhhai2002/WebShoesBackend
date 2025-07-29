package com.shoes.webshoes.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.ProductDetail;
import com.shoes.webshoes.entity.SaveForLater;
import lombok.Data;
import java.util.Date;

@Data
public class SaveForLaterResponse {
    private Integer id;
    
    @JsonProperty("user_id")
    private Integer userId;
    
    @JsonProperty("product_detail_id")
    private Integer productDetailId;
    
    @JsonProperty("product_detail")
    private ProductDetailResponse productDetail;
    
    @JsonProperty("created_at")
    private Date createdAt;
    
    @JsonProperty("updated_at")
    private Date updatedAt;
    
    public SaveForLaterResponse() {}
    
    public SaveForLaterResponse(SaveForLater entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.productDetailId = entity.getProductDetailId();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

     public SaveForLaterResponse(SaveForLater entity, ProductDetail productDetail) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.productDetailId = entity.getProductDetailId();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.productDetail = new ProductDetailResponse(productDetail);
     }
} 