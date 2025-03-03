package com.shoes.webshoes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.VoucherApplicationDao;
import com.shoes.webshoes.entity.VoucherApplication;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.service.VoucherApplicationService;

@Service("VoucherApplicationService")
@Transactional(rollbackFor = Error.class)
public class VoucherApplicationServiceImpl implements VoucherApplicationService {
    @Autowired
    private VoucherApplicationDao voucherApplicationDao;

    @Override
    public void create(VoucherApplication voucherApplication) {
        voucherApplicationDao.create(voucherApplication);
    }

    @Override
    public VoucherApplication findOne(int id) {
        return voucherApplicationDao.findOne(id);
    }

    @Override
    public void update(VoucherApplication voucherApplication) {
        voucherApplicationDao.update(voucherApplication);
    }

    @Override
    public List<VoucherApplication> getAll() {
        return voucherApplicationDao.getAll();
    }

    @Override
    public VoucherApplication findByName(String name) {
        return voucherApplicationDao.findByName(name);
    }

    @Override
    public StoreProcedureListResult<VoucherApplication> spGListVoucherApplication(int voucherId, int productId,
            int brandId, int categoryId, String keySearch, int status, Pagination pagination) throws Exception {
        return voucherApplicationDao.spGListVoucherApplication(voucherId, productId, brandId, categoryId, keySearch, status, pagination);
    }
}
