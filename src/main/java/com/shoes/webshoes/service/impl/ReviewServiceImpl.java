package com.shoes.webshoes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.ReviewDao;
import com.shoes.webshoes.entity.Review;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewDao reviewDao;

    @Override
    public void create(Review review) {
        reviewDao.create(review);
    }

    @Override
    public Review findOne(int id) {
        return reviewDao.findOne(id);
    }

    @Override
    public void update(Review review) {
        reviewDao.update(review);
    }

    @Override
    public void delete(int id) {
        reviewDao.delete(id);
    }

    @Override
    public Review findByUserIdAndProductId(int userId, int productId) {
        return reviewDao.findByUserIdAndProductId(userId, productId);
    }

    @Override
    public StoreProcedureListResult<Review> spGListReview(int userId, int productId, String keySearch, int status, Pagination pagination) throws Exception {
        return reviewDao.spGListReview(userId, productId, keySearch, status, pagination);
    }
}
