package com.shoes.webshoes.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.common.utils.StringErrorValue;
import com.shoes.webshoes.entity.Color;
import com.shoes.webshoes.entity.Materials;
import com.shoes.webshoes.entity.Product;
import com.shoes.webshoes.entity.ProductDetail;
import com.shoes.webshoes.entity.Size;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDProductDetailRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.ProductDetailResponse;
import com.shoes.webshoes.service.ColorService;
import com.shoes.webshoes.service.MaterialsService;
import com.shoes.webshoes.service.ProductDetailService;
import com.shoes.webshoes.service.ProductService;
import com.shoes.webshoes.service.SizeService;
import com.shoes.webshoes.service.impl.FirebaseImageService;
import com.shoes.webshoes.service.BrandService;
import com.shoes.webshoes.service.CategoryService;
import com.shoes.webshoes.entity.Brand;
import com.shoes.webshoes.entity.Category;


@RestController
@RequestMapping("/api/v1/product-detail")
public class ProductDetailController  {
    @Autowired
    public ProductDetailService productDetailService;

	@Autowired
    public ProductService productService;

	@Autowired
	public FirebaseImageService iFirebaseImageService;

	@Autowired
	public ColorService colorService;

	@Autowired
	public SizeService sizeService;

	@Autowired
	public MaterialsService materialsService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private CategoryService categoryService;

    @GetMapping("")
//	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<ProductDetailResponse>>> getAll(
			@RequestParam(name = "product_id", required = false, defaultValue = "-1") int productId,
			@RequestParam(name = "color_id", required = false, defaultValue = "-1") int colorId,
			@RequestParam(name = "size_id", required = false, defaultValue = "-1") int sizeId,
			@RequestParam(name = "material_id", required = false, defaultValue = "-1") int materialId,
			@RequestParam(name = "brand_id", required = false, defaultValue = "-1") int brandId,
			@RequestParam(name = "category_id", required = false, defaultValue = "-1") int categoryId,
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<ProductDetailResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<ProductDetail> listProductDetail = productDetailService.spGListProductDetail(
			productId, colorId, sizeId, materialId, brandId, categoryId, keySearch, status, pagination);

		BaseListDataResponse<ProductDetailResponse> listData = new BaseListDataResponse<>();
		listData.setList(new ProductDetailResponse().mapToList(listProductDetail.getResult()));
		listData.setTotalRecord(listProductDetail.getTotalRecord());

		response.setData(listData);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @GetMapping("/{id}")
	public ResponseEntity<BaseResponse<ProductDetailResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<ProductDetailResponse> response = new BaseResponse<>();
		ProductDetail productDetail = productDetailService.findOne(id);

		if (productDetail == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_DETAIL_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
        response.setData(new ProductDetailResponse(productDetail));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<ProductDetailResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
		BaseResponse<ProductDetailResponse> response = new BaseResponse<>();
		ProductDetail productDetail = productDetailService.findOne(id);

		if (productDetail == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_DETAIL_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		productDetail.setStatus(productDetail.getStatus() == 1 ? 0 : 1);

		productDetailService.update(productDetail);
        response.setData(new ProductDetailResponse(productDetail));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<BaseResponse<ProductDetailResponse>> create(
			@Valid @RequestBody CRUDProductDetailRequest wrapper) throws Exception {

		BaseResponse<ProductDetailResponse> response = new BaseResponse<>();
		ProductDetail productDetailCheck = productDetailService.findByName(wrapper.getName());

		if (productDetailCheck != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_DETAIL_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Product product = productService.findOne(wrapper.getProductId());

		if (product == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Color color = colorService.findOne(wrapper.getColorId());

		if (color == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.COLOR_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Size size = sizeService.findOne(wrapper.getSizeId());

		if (size == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.SIZE_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Materials materials = materialsService.findOne(wrapper.getMaterialId());

		if (materials == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.MATERIALS_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Brand brand = brandService.findOne(wrapper.getBrandId());
		if (brand == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.BRAND_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Category category = categoryService.findOne(wrapper.getCategoryId());
		if (category == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CATEGORY_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		ProductDetail productDetail = new ProductDetail();
		productDetail.setName(wrapper.getName());
		productDetail.setProductId(wrapper.getProductId());
		productDetail.setColorId(color.getId());
		productDetail.setColor(color.getName());
		productDetail.setSizeId(size.getId());
		productDetail.setSize(size.getName());
		productDetail.setMaterialId(materials.getId());
		productDetail.setMaterial(materials.getName());
		productDetail.setPrice(wrapper.getPrice());
		productDetail.setStock(wrapper.getStock());
		productDetail.setStatus(1);
		productDetail.setBrandId(brand.getId());
		productDetail.setBrand(brand.getName());
		productDetail.setCategoryId(category.getId());
		productDetail.setCategory(category.getName());

		productDetailService.create(productDetail);
		response.setData(new ProductDetailResponse(productDetail));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update")
	public ResponseEntity<BaseResponse<ProductDetailResponse>> update(@PathVariable("id") int id,
			@Valid @RequestBody CRUDProductDetailRequest wrapper) throws Exception {

		BaseResponse<ProductDetailResponse> response = new BaseResponse<>();
		ProductDetail productDetail = productDetailService.findOne(id);

		if (productDetail == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_DETAIL_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (!productDetail.getName().equals(wrapper.getName())
				&& productDetailService.findByName(wrapper.getName()) != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_DETAIL_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (wrapper.getBrandId() > 0) {
			Brand brand = brandService.findOne(wrapper.getBrandId());
			if (brand == null) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError(StringErrorValue.BRAND_NOT_FOUND);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			productDetail.setBrandId(brand.getId());
			productDetail.setBrand(brand.getName());
		}

		if (wrapper.getCategoryId() > 0) {
			Category category = categoryService.findOne(wrapper.getCategoryId());
			if (category == null) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError(StringErrorValue.CATEGORY_NOT_FOUND);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			productDetail.setCategoryId(category.getId());
			productDetail.setCategory(category.getName());
		}

		productDetail.setName(wrapper.getName());
		productDetail.setPrice(wrapper.getPrice());
		productDetail.setStock(wrapper.getStock());
		productDetailService.update(productDetail);

		response.setData(new ProductDetailResponse(productDetail));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
