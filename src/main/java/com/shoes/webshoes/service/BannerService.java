package com.shoes.webshoes.service;

import java.util.List;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.entity.Banner;
import com.shoes.webshoes.model.StoreProcedureListResult;

public interface BannerService {
    void create(Banner banner);
    Banner findOne(int id);
    void update(Banner banner);
    List<Banner> getAll();
    StoreProcedureListResult<Banner> spGListBanner(
        String keySearch,
        int status,
        Pagination pagination
    ) throws Exception;
}
