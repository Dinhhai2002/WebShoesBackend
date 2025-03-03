package com.shoes.webshoes.dao;

import java.util.List;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.entity.VoucherApplication;

public interface VoucherApplicationDao {
    void create(VoucherApplication voucherApplication);

    VoucherApplication findOne(int id);

    void update(VoucherApplication voucherApplication);

    List<VoucherApplication> getAll();

    StoreProcedureListResult<VoucherApplication> spGListVoucherApplication(int voucherId, int productId, int brandId, int categoryId, String keySearch,int status,Pagination pagination) throws Exception;

    VoucherApplication findByName(String name);
}