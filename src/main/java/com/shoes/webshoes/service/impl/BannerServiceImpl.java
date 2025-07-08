package com.shoes.webshoes.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.BannerDao;
import com.shoes.webshoes.entity.Banner;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.service.BannerService;

@Service("BannerService")
@Transactional(rollbackFor = Error.class)
public class BannerServiceImpl implements BannerService {
    @Autowired
    private BannerDao bannerDao;

    @Override
    public void create(Banner banner) {
        bannerDao.create(banner);
    }

    @Override
    public Banner findOne(int id) {
        return bannerDao.findOne(id);
    }

    @Override
    public void update(Banner banner) {
        bannerDao.update(banner);
    }

    @Override
    public List<Banner> getAll() {
        return bannerDao.getAll();
    }

    @Override
    public StoreProcedureListResult<Banner> spGListBanner(
            String keySearch,
            int status,
            Pagination pagination) throws Exception {
        return bannerDao.spGListBanner(keySearch, status, pagination);
    }
}
