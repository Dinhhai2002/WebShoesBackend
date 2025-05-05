package com.shoes.webshoes.dao;

import java.util.List;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.entity.ProductDetail;

public interface ProductDetailDao {
    void create(ProductDetail productDetail);

    ProductDetail findOne(int id);

    void update(ProductDetail productDetail);

    List<ProductDetail> getAll();

    StoreProcedureListResult<ProductDetail> spGListProductDetail(
        int productId, 
        int colorId, 
        int sizeId, 
        int materialId,
        int brandId,
        int categoryId, 
        String keySearch,
        int status,
        Pagination pagination
    ) throws Exception;

    ProductDetail findByName(String name);
    
    List<ProductDetail> findByIds(List<Integer> ids);

    ProductDetail findBySku(String sku);

    ProductDetail findByBarcode(String barcode);
}