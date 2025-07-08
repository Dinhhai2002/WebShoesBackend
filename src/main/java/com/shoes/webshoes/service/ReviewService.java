package com.shoes.webshoes.service;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.entity.Review;
import com.shoes.webshoes.model.StoreProcedureListResult;

public interface ReviewService {
    void create(Review review);
    Review findOne(int id);
    void update(Review review);
    void delete(int id);
    Review findByUserIdAndProductId(int userId, int productId);
    StoreProcedureListResult<Review> spGListReview(int userId, int productId, String keySearch, int status, Pagination pagination) throws Exception;
}
