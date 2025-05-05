package com.shoes.webshoes.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoes.webshoes.entity.Product;
import com.shoes.webshoes.entity.ProductDetail;

import lombok.Data;

@Data
public class ProductDetailResponse {
    private int id;

	private String name;

	@JsonProperty("product_id")
	private int productId;

	@JsonProperty("color_id")
	private int colorId;

	private String color;
	
	@JsonProperty("size_id")
	private int sizeId;

	private String size;

	@JsonProperty("material_id")
	private int materialId;

	private String material;

	@JsonProperty("brand_id")
	private int brandId;
	private String brand;

	@JsonProperty("category_id")
	private int categoryId;
	private String category;

	private int stock;

	private BigDecimal price;

	@JsonProperty("image_url")
	private String imageUrl; 

	private int status;

    @JsonProperty("product")
    private ProductResponse product;

    @JsonProperty("sku")
    private String sku;

    @JsonProperty("barcode")
    private String barcode;

	public ProductDetailResponse() {

	}

	public ProductDetailResponse(ProductDetail entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.productId = entity.getProductId();
		this.colorId = entity.getColorId();
		this.color = entity.getColor();
		this.size = entity.getSize();
		this.sizeId = entity.getSizeId();
		this.materialId = entity.getMaterialId();
		this.material = entity.getMaterial();
		this.brandId = entity.getBrandId();
		this.brand = entity.getBrand();
		this.categoryId = entity.getCategoryId();
		this.category = entity.getCategory();
		this.stock = entity.getStock();
		this.price = entity.getPrice();
		this.imageUrl = entity.getImageUrl();
		this.status = entity.getStatus();
		this.sku = entity.getSku();
		this.barcode = entity.getBarcode();
	}

	public ProductDetailResponse(ProductDetail entity, Product product) {
        this(entity);
        this.product = new ProductResponse(product);
    }

	public List<ProductDetailResponse> mapToList(List<ProductDetail> entities) {
		return entities.stream().map(x -> new ProductDetailResponse(x)).collect(Collectors.toList());
	}
}
