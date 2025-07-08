package com.shoes.webshoes.dao;

import java.util.List;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.entity.Voucher;

public interface VoucherDao {
    void create(Voucher voucher);

    Voucher findOne(int id);

    void update(Voucher voucher);

    List<Voucher> getAll();

    StoreProcedureListResult<Voucher> spGListVoucher(String keySearch,int status,Pagination pagination) throws Exception;

    Voucher findByName(String name);
}