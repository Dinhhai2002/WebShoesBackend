package com.shoes.webshoes.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shoes.webshoes.common.utils.BarcodeUtil;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.common.utils.StringErrorValue;
import com.shoes.webshoes.entity.Brand;
import com.shoes.webshoes.entity.Category;
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
import com.shoes.webshoes.service.BrandService;
import com.shoes.webshoes.service.CategoryService;
import com.shoes.webshoes.service.ColorService;
import com.shoes.webshoes.service.MaterialsService;
import com.shoes.webshoes.service.ProductDetailService;
import com.shoes.webshoes.service.ProductService;
import com.shoes.webshoes.service.SizeService;
import com.shoes.webshoes.service.impl.FirebaseImageService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/product-detail")
public class ProductDetailController {
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
		StoreProcedureListResult<ProductDetail> listProductDetail = productDetailService.spGListProductDetail(productId,
				colorId, sizeId, materialId, brandId, categoryId, keySearch, status, pagination);

		BaseListDataResponse<ProductDetailResponse> listData = new BaseListDataResponse<>();
		listData.setList(new ProductDetailResponse().mapToList(listProductDetail.getResult()));
		listData.setTotalRecord(listProductDetail.getTotalRecord());

		response.setData(listData);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BaseResponse<ProductDetailResponse>> findOneById(@PathVariable("id") int id)
			throws Exception {
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
	public ResponseEntity<BaseResponse<ProductDetailResponse>> changeStatus(@PathVariable("id") int id)
			throws Exception {
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
//		ProductDetail productDetailCheck = productDetailService.findByName(wrapper.getName());
//
//		if (productDetailCheck != null) {
//			response.setStatus(HttpStatus.BAD_REQUEST);
//			response.setMessageError(StringErrorValue.PRODUCT_DETAIL_IS_EXIST);
//			return new ResponseEntity<>(response, HttpStatus.OK);
//		}

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

		Brand brand = brandService.findOne(product.getBrandId());
		if (brand == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.BRAND_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Category category = categoryService.findOne(product.getCategoryId());
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
		// Kiểm tra SKU đã tồn tại chưa
		String sku = String.format("S-%d-%d-%d-%d", wrapper.getProductId(), wrapper.getColorId(), wrapper.getSizeId(),
				wrapper.getMaterialId());
		ProductDetail existedBySku = productDetailService.findBySku(sku);
		if (existedBySku != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_DETAIL_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		productDetail.setSku(sku);

		// Sinh mã barcode chuẩn EAN-13
		String barcode = generateEAN13Barcode(wrapper.getProductId(), wrapper.getColorId(), wrapper.getSizeId(),
				wrapper.getMaterialId());
		ProductDetail existedByBarcode = productDetailService.findByBarcode(barcode);
		if (existedByBarcode != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(
					"Sản phẩm đã tồn tại với barcode: " + barcode + ", tên: " + existedByBarcode.getName());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		productDetail.setBarcode(barcode);

		productDetailService.create(productDetail);
		response.setData(new ProductDetailResponse(productDetail));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create-multiple")
	public ResponseEntity<BaseResponse<List<ProductDetailResponse>>> createMultiple(
			@Valid @RequestBody List<CRUDProductDetailRequest> productDetailsRequest) throws Exception {

		BaseResponse<List<ProductDetailResponse>> response = new BaseResponse<>();
		List<ProductDetailResponse> productDetailResponses = new ArrayList<>();
		// Kiểm tra các thông tin liên quan
		Product product = productService.findOne(productDetailsRequest.get(0).getProductId());
		if (product == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Brand brand = brandService.findOne(product.getBrandId());
		if (brand == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.BRAND_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Category category = categoryService.findOne(product.getCategoryId());
		if (category == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CATEGORY_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		// Tối ưu: gom tất cả ID cần lấy
		Set<Integer> colorIds = new HashSet<>();
		Set<Integer> sizeIds = new HashSet<>();
		Set<Integer> materialIds = new HashSet<>();
		for (CRUDProductDetailRequest wrapper : productDetailsRequest) {
			colorIds.add(wrapper.getColorId());
			sizeIds.add(wrapper.getSizeId());
			materialIds.add(wrapper.getMaterialId());
		}
		// Lấy 1 lần tất cả entities liên quan
		List<Color> colorList = colorService.findByIds(new ArrayList<>(colorIds));
		List<Size> sizeList = sizeService.findByIds(new ArrayList<>(sizeIds));
		List<Materials> materialList = materialsService.findByIds(new ArrayList<>(materialIds));
		// Map để tra cứu nhanh
		Map<Integer, Color> colorMap = colorList.stream().collect(Collectors.toMap(Color::getId, c -> c));
		Map<Integer, Size> sizeMap = sizeList.stream().collect(Collectors.toMap(Size::getId, s -> s));
		Map<Integer, Materials> materialMap = materialList.stream().collect(Collectors.toMap(Materials::getId, m -> m));
		// Lặp qua từng sản phẩm trong danh sách
		for (CRUDProductDetailRequest wrapper : productDetailsRequest) {
			Color color = colorMap.get(wrapper.getColorId());
			if (color == null) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError(StringErrorValue.COLOR_NOT_FOUND);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			Size size = sizeMap.get(wrapper.getSizeId());
			if (size == null) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError(StringErrorValue.SIZE_NOT_FOUND);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			Materials materials = materialMap.get(wrapper.getMaterialId());
			if (materials == null) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError(StringErrorValue.MATERIALS_NOT_FOUND);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			// Tạo mới sản phẩm con
			ProductDetail productDetail = new ProductDetail();
			productDetail.setName(wrapper.getName());
			productDetail.setProductId(wrapper.getProductId());
			productDetail.setColorId(color.getId());
			productDetail.setColor(color.getName());
			productDetail.setSizeId(size.getId());
			productDetail.setSize(size.getName());
			productDetail.setMaterialId(materials.getId());
			productDetail.setMaterial(materials.getName());
			productDetail.setPrice(product.getPrice());
			productDetail.setStock(wrapper.getStock());
			productDetail.setStatus(1); // Sản phẩm có sẵn
			productDetail.setBrandId(brand.getId());
			productDetail.setBrand(brand.getName());
			productDetail.setCategoryId(category.getId());
			productDetail.setCategory(category.getName());
			productDetail.setImageUrl(product.getImageUrl());
			// Kiểm tra SKU đã tồn tại chưa
			String sku = String.format("S-%d-%d-%d-%d", wrapper.getProductId(), wrapper.getColorId(),
					wrapper.getSizeId(), wrapper.getMaterialId());
			ProductDetail existedBySku = productDetailService.findBySku(sku);
			if (existedBySku != null) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError("Sản phẩm đã tồn tại với SKU: " + sku + ", tên: " + existedBySku.getName()
						+ ", màu: " + existedBySku.getColor() + ", size: " + existedBySku.getSize() + ", chất liệu: "
						+ existedBySku.getMaterial());
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			productDetail.setSku(sku);

			// Sinh mã barcode chuẩn EAN-13
			String barcode = generateEAN13Barcode(wrapper.getProductId(), wrapper.getColorId(), wrapper.getSizeId(),
					wrapper.getMaterialId());
			ProductDetail existedByBarcode = productDetailService.findByBarcode(barcode);
			if (existedByBarcode != null) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError(
						"Sản phẩm đã tồn tại với barcode: " + barcode + ", tên: " + existedByBarcode.getName());
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			productDetail.setBarcode(barcode);

			productDetailService.create(productDetail);
			productDetailResponses.add(new ProductDetailResponse(productDetail));
		}

		// Trả về danh sách các sản phẩm con đã được tạo
		response.setData(productDetailResponses);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Sinh mã barcode chuẩn EAN-13
	private String generateEAN13Barcode(int productId, int colorId, int sizeId, int materialId) {
		// EAN-13: 13 số, ví dụ: 893 + productId(5 số) + colorId(2) + sizeId(2) +
		// materialId(1) + checksum(1)
		String prefix = "89";
		String body = String.format("%05d%02d%02d%01d", productId, colorId, sizeId, materialId);
		String partial = prefix + body; // 12 số
		int checksum = calcEAN13Checksum(partial);
		return partial + checksum;
	}

	// Tính checksum cho EAN-13
	private int calcEAN13Checksum(String code) {
		int sum = 0;
		for (int i = 0; i < code.length(); i++) {
			int digit = code.charAt(i) - '0';
			sum += (i % 2 == 0) ? digit : digit * 3;
		}
		return (10 - (sum % 10)) % 10;
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

		// Cập nhật brand
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

		// Cập nhật category
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

		// Cập nhật color
		if (wrapper.getColorId() > 0) {
			Color color = colorService.findOne(wrapper.getColorId());
			if (color == null) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError(StringErrorValue.COLOR_NOT_FOUND);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			productDetail.setColorId(color.getId());
			productDetail.setColor(color.getName());
		}

		// Cập nhật size
		if (wrapper.getSizeId() > 0) {
			Size size = sizeService.findOne(wrapper.getSizeId());
			if (size == null) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError(StringErrorValue.SIZE_NOT_FOUND);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			productDetail.setSizeId(size.getId());
			productDetail.setSize(size.getName());
		}

		// Cập nhật material
		if (wrapper.getMaterialId() > 0) {
			Materials material = materialsService.findOne(wrapper.getMaterialId());
			if (material == null) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError(StringErrorValue.MATERIALS_NOT_FOUND);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			productDetail.setMaterialId(material.getId());
			productDetail.setMaterial(material.getName());
		}

		// Các trường còn lại
		productDetail.setName(wrapper.getName());
		productDetail.setPrice(wrapper.getPrice());
		productDetail.setStock(wrapper.getStock());

		productDetailService.update(productDetail);

		response.setData(new ProductDetailResponse(productDetail));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PostMapping("/{id}/image")
	public ResponseEntity<BaseResponse> uploadBanner(@RequestParam(name = "file") MultipartFile file,
			@PathVariable("id") int id) throws Exception {
		BaseResponse response = new BaseResponse();
		ProductDetail productDetail = productDetailService.findOne(id);

		if (productDetail == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_DETAIL_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		String fileName = iFirebaseImageService.save(file);

		String imageUrl = iFirebaseImageService.getImageUrl(fileName);

		productDetail.setImageUrl(imageUrl);
		productDetailService.update(productDetail);

		response.setData(new ProductDetailResponse(productDetail));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update-image")
	public ResponseEntity<BaseResponse> updateImageColorProduct(@PathVariable("id") int id) throws Exception {
		BaseResponse response = new BaseResponse();
		ProductDetail productDetail = productDetailService.findOne(id);

		if (productDetail == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_DETAIL_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		List<ProductDetail> productDetails = productDetailService.spGListProductDetail(productDetail.getProductId(),
				productDetail.getColorId(), -1, -1, -1, -1, "", 1, new Pagination(0, 20)).getResult();
		productDetails.stream().forEach(p -> {
			p.setImageUrl(productDetail.getImageUrl());
			productDetailService.update(p);
		});

		response.setData(new ProductDetailResponse(productDetail));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/barcode/{barcode}")
	public ResponseEntity<BaseResponse<ProductDetailResponse>> getByBarcode(@PathVariable("barcode") String barcode) {
		BaseResponse<ProductDetailResponse> response = new BaseResponse<>();
		ProductDetail productDetail = productDetailService.findByBarcode(barcode);
		if (productDetail == null) {
			response.setStatus(HttpStatus.NOT_FOUND);
			response.setMessageError("Không tìm thấy sản phẩm với barcode: " + barcode);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		response.setData(new ProductDetailResponse(productDetail));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/barcode-image/{barcode}")
	public ResponseEntity<byte[]> getBarcodeImage(@PathVariable("barcode") String barcode) throws Exception {
		// Sinh hình ảnh barcode EAN-13
		byte[] image = BarcodeUtil.generateEAN13BarcodeImage(barcode);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(image, headers, HttpStatus.OK);
	}
}
