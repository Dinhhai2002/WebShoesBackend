package com.shoes.webshoes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.VoucherDao;
import com.shoes.webshoes.entity.Voucher;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.service.VoucherService;

@Service("VoucherService")
@Transactional(rollbackFor = Error.class)
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    private VoucherDao voucherDao;

    @Override
    public void create(Voucher voucher) {
        voucherDao.create(voucher);
    }

    @Override
    public Voucher findOne(int id) {
        return voucherDao.findOne(id);
    }

    @Override
    public void update(Voucher voucher) {
        voucherDao.update(voucher);
    }

    @Override
    public List<Voucher> getAll() {
        return voucherDao.getAll();
    }

    @Override
    public Voucher findByName(String name) {
        return voucherDao.findByName(name);
    }

    @Override
	public StoreProcedureListResult<Voucher> spGListVoucher(String keySearch, int status,
			Pagination pagination) throws Exception {
		return voucherDao.spGListVoucher(keySearch, status, pagination);
	}
}
