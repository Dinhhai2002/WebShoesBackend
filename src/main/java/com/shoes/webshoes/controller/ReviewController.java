package com.shoes.webshoes.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.common.utils.StringErrorValue;
import com.shoes.webshoes.entity.Review;
import com.shoes.webshoes.entity.Users;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDReviewRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.ReviewResponse;
import com.shoes.webshoes.service.ReviewService;
import com.shoes.webshoes.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController extends BaseController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<BaseResponse<BaseListDataResponse<ReviewResponse>>> getAll(
            @RequestParam(name = "user_id", required = false, defaultValue = "-1") int userId,
            @RequestParam(name = "product_id", required = false, defaultValue = "-1") int productId,
            @RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
            @RequestParam(name = "status", required = false, defaultValue = "-1") int status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
        BaseResponse<BaseListDataResponse<ReviewResponse>> response = new BaseResponse<>();
        Pagination pagination = new Pagination(page, limit);
        StoreProcedureListResult<Review> listReview = reviewService.spGListReview(userId, productId, keySearch, status, pagination);

        // Lấy danh sách user IDs từ reviews
        List<Integer> userIds = listReview.getResult().stream()
            .map(Review::getUserId)
            .distinct()
            .collect(Collectors.toList());

        // Lấy thông tin users
        List<Users> users = userService.findByIds(userIds);

        BaseListDataResponse<ReviewResponse> listData = new BaseListDataResponse<>();
        listData.setList(new ReviewResponse().mapToList(listReview.getResult(), users));
        listData.setTotalRecord(listReview.getTotalRecord());

        response.setData(listData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ReviewResponse>> findOneById(@PathVariable("id") int id) throws Exception {
        BaseResponse<ReviewResponse> response = new BaseResponse<>();
        Review review = reviewService.findOne(id);

        if (review == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.REVIEW_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // Lấy thông tin user
        Users user = userService.findOne(review.getUserId());
        response.setData(new ReviewResponse(review, user));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<ReviewResponse>> create(@Valid @RequestBody CRUDReviewRequest wrapper) throws Exception {
        BaseResponse<ReviewResponse> response = new BaseResponse<>();
        Users users = this.getUser();

        // Kiểm tra xem user đã review sản phẩm này chưa
        Review existingReview = reviewService.findByUserIdAndProductId(users.getId(), wrapper.getProductId());
        if (existingReview != null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.REVIEW_IS_EXIST);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Review review = new Review();
        review.setUserId(users.getId());
        review.setProductId(wrapper.getProductId());
        review.setRating(wrapper.getRating());
        review.setComment(wrapper.getComment());
        review.setStatus(1);

        reviewService.create(review);
        response.setData(new ReviewResponse(review));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/change-status")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<BaseResponse<ReviewResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
        BaseResponse<ReviewResponse> response = new BaseResponse<>();
        Review review = reviewService.findOne(id);

        if (review == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.REVIEW_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        review.setStatus(review.getStatus() == 1 ? 0 : 1);
        reviewService.update(review);
        response.setData(new ReviewResponse(review));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
