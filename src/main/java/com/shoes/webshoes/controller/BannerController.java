package com.shoes.webshoes.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.common.utils.StringErrorValue;
import com.shoes.webshoes.entity.Banner;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDBannerRequest;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.BannerResponse;
import com.shoes.webshoes.service.BannerService;
import com.shoes.webshoes.service.impl.FirebaseImageService;

@RestController
@RequestMapping("/api/v1/banner")
public class BannerController {
    @Autowired
    private BannerService bannerService;
    
    @Autowired
   	public FirebaseImageService iFirebaseImageService;

    @GetMapping("")
    public ResponseEntity<BaseResponse<BaseListDataResponse<BannerResponse>>> getAll(
            @RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
            @RequestParam(name = "status", required = false, defaultValue = "-1") int status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
        BaseResponse<BaseListDataResponse<BannerResponse>> response = new BaseResponse<>();
        Pagination pagination = new Pagination(page, limit);
        StoreProcedureListResult<Banner> listBanner = bannerService.spGListBanner(keySearch, status, pagination);

        BaseListDataResponse<BannerResponse> listData = new BaseListDataResponse<>();
        listData.setList(new BannerResponse().mapToList(listBanner.getResult()));
        listData.setTotalRecord(listBanner.getTotalRecord());

        response.setData(listData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<BannerResponse>> findOneById(@PathVariable("id") int id) throws Exception {
        BaseResponse<BannerResponse> response = new BaseResponse<>();
        Banner banner = bannerService.findOne(id);

        if (banner == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.BANNER_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.setData(new BannerResponse(banner));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<BaseResponse<BannerResponse>> create(
    		@RequestParam(name = "file") MultipartFile file) throws Exception {
        BaseResponse<BannerResponse> response = new BaseResponse<>();
    	String fileName = iFirebaseImageService.save(file);

		String imageUrl = iFirebaseImageService.getImageUrl(fileName);
        Banner banner = new Banner();
        banner.setUrl(imageUrl);
        banner.setStatus(1);
        banner.setIsDeleted(0);

        bannerService.create(banner);
        response.setData(new BannerResponse(banner));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<BaseResponse<BannerResponse>> update(
            @PathVariable("id") int id,
            @Valid @RequestBody CRUDBannerRequest wrapper) throws Exception {
        BaseResponse<BannerResponse> response = new BaseResponse<>();
        Banner banner = bannerService.findOne(id);

        if (banner == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.BANNER_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        banner.setUrl(wrapper.getUrl());

        bannerService.update(banner);
        response.setData(new BannerResponse(banner));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/change-status")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<BaseResponse<BannerResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
        BaseResponse<BannerResponse> response = new BaseResponse<>();
        Banner banner = bannerService.findOne(id);

        if (banner == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.BANNER_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        banner.setStatus(banner.getStatus() == 1 ? 0 : 1);
        bannerService.update(banner);
        response.setData(new BannerResponse(banner));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
