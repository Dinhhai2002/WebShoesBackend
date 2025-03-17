package com.shoes.webshoes.controller;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.shoes.webshoes.common.enums.OtpEnum;
import com.shoes.webshoes.common.utils.HttpService;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.common.utils.StringErrorValue;
import com.shoes.webshoes.common.utils.Utils;
import com.shoes.webshoes.entity.Banner;
import com.shoes.webshoes.entity.Brand;
import com.shoes.webshoes.entity.Cart;
import com.shoes.webshoes.entity.Category;
import com.shoes.webshoes.entity.Product;
import com.shoes.webshoes.entity.UserRegister;
import com.shoes.webshoes.entity.Users;
import com.shoes.webshoes.entity.Size;
import com.shoes.webshoes.entity.Materials;
import com.shoes.webshoes.entity.Color;
import com.shoes.webshoes.entity.ProductDetail;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.request.CRUDUserRequest;
import com.shoes.webshoes.request.ConfirmOtpRequest;
import com.shoes.webshoes.request.GoogleAccountRequest;
import com.shoes.webshoes.request.JwtRequest;
import com.shoes.webshoes.request.OTPRegisterUserRequest;
import com.shoes.webshoes.request.OTPRequest;
import com.shoes.webshoes.request.ResetPasswordRequest;
import com.shoes.webshoes.response.BannerResponse;
import com.shoes.webshoes.response.BaseListDataResponse;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.BrandResponse;
import com.shoes.webshoes.response.CategoryResponse;
import com.shoes.webshoes.response.JwtResponse;
import com.shoes.webshoes.response.ProductResponse;
import com.shoes.webshoes.response.UserResponse;
import com.shoes.webshoes.response.SizeResponse;
import com.shoes.webshoes.response.MaterialsResponse;
import com.shoes.webshoes.response.ColorResponse;
import com.shoes.webshoes.response.ProductDetailResponse;
import com.shoes.webshoes.service.BannerService;
import com.shoes.webshoes.service.BrandService;
import com.shoes.webshoes.service.CartService;
import com.shoes.webshoes.service.CategoryService;
import com.shoes.webshoes.service.ProductService;
import com.shoes.webshoes.service.UserRegisterService;
import com.shoes.webshoes.service.SizeService;
import com.shoes.webshoes.service.MaterialsService;
import com.shoes.webshoes.service.ColorService;
import com.shoes.webshoes.service.ProductDetailService;


@RestController
@RequestMapping("/api/v1/authentication")
public class JwtAuthenticationController extends BaseController {
    @Autowired
	public UserRegisterService userRegisterService;
    
    @Autowired
    public CartService cartService;
    
    @Autowired
    private BannerService bannerService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private BrandService brandService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private SizeService sizeService;
    
    @Autowired
    private MaterialsService materialsService;
    
    @Autowired
    private ColorService colorService;
    
    @Autowired
    private ProductDetailService productDetailService;
    
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<JwtResponse>> createAuthenticationToken(@RequestBody JwtRequest wrapper)
            throws Exception {
        BaseResponse<JwtResponse> response = new BaseResponse<>();
        Users user = userService.findUsersByUsersNameAndPassword(wrapper.getUsername(),
                Utils.encodeBase64(wrapper.getPassword()));

        if (user == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.LOGIN_FAIL);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(wrapper.getUsername());

        if (user.getIsActive() == 0) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.USER_IS_LOCKED);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        String token = jwtTokenUtil.generateToken(userDetails);
        user.setAccessToken(token);
        userService.update(user);
        
        List<Cart> listCart = cartService.spGListCart(user.getId(), "",
				1, new Pagination(0, 20)).getResult();
        if(listCart != null && listCart.isEmpty()) {
        	Cart cart = new Cart();
        	cart.setUserId(user.getId());
        	cart.setStatus(1);
        	cartService.create(cart);
        }
        
        response.setData(new JwtResponse(token));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<UserResponse>> spUCreateUser(@Valid @RequestBody CRUDUserRequest wrapper)
            throws Exception {
        BaseResponse<UserResponse> response = new BaseResponse<>();
        // BCrypt.hashpw(wrapper.getPassword(), BCrypt.gensalt(12))

        response.setData(new UserResponse(userService.spUCreateUsers(wrapper.getUserName(), wrapper.getFullName(),
                wrapper.getEmail(), wrapper.getPhone(), Utils.encodeBase64(wrapper.getPassword()), wrapper.getGender(),
                this.formatDate(wrapper.getBirthday()), wrapper.getWardId(), wrapper.getDistrictId(),
                wrapper.getCityId(), wrapper.getFullAddress())));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/reset-password")
    public ResponseEntity<BaseResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest wrapper)
            throws Exception {

        BaseResponse response = new BaseResponse<>();

        Users user = userService.findUsersByUsersName(wrapper.getUserName());

        if (user == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.USER_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (!wrapper.getNewPassword().equals(wrapper.getConfirmPassword())) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.ERROR_CONFIRM_PASSWORD_AND_CONFIRM);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (user.getIsConfirmOtp() == 0) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.OTP_IS_NOT_CONFIRM);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        user.setPassword(Utils.encodeBase64(wrapper.getNewPassword()));
        user.setIsConfirmOtp(0);

        userService.update(user);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/otp-register")
    public ResponseEntity<BaseResponse> otpRegister(@Valid @RequestBody OTPRegisterUserRequest wrapper)
            throws Exception {

        BaseResponse<Object> response = new BaseResponse<>();

        if (userService.findUsersByUsersName(wrapper.getUserName()) != null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.NAME_USER_IS_EXIST);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (userService.findUsersByEmail(wrapper.getEmail(), 0) != null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.MAIL_USER_IS_EXIST);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (userService.findUsersByPhone(wrapper.getPhone()) != null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.PHONE_USER_IS_EXIST);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        /*
         * - lấy ra userRegister -> Nếu có thì kiểm tra mã otp còn hạn hay không + Nếu
         * hết thì set status =0 + Ngược lại thì thông báo tài khoản này đăng có người
         * khác xác thực
         */
        UserRegister checkUserRegister = userRegisterService.findUsersRegisterByUsersNameAndEmail(wrapper.getUserName(),
                wrapper.getEmail());
        if (checkUserRegister != null) {

            if (this.caculateOtpExpired(checkUserRegister.getOtpCreatedAt()) > 0) {
                checkUserRegister.setStatus(0);
                userRegisterService.update(checkUserRegister);
            }

            else {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessageError(StringErrorValue.USER_REGISTER_IS_AUTHENTICATING);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }

        Random rand = new Random();
        int otpvalue = rand.nextInt(1255650);
        sendEmail.sendSimpleEmail(wrapper.getEmail(), "Mã OTP",
                "Mã OTP là:" + otpvalue + ". Mã otp này có thời hạn là 3p");

        UserRegister userRegister = new UserRegister();
        userRegister.setUserName(wrapper.getUserName());
        userRegister.setEmail(wrapper.getEmail());
        userRegister.setOtp(otpvalue);
        userRegister.setOtpCreatedAt(new Date());
        userRegister.setStatus(1);

        userRegisterService.create(userRegister);

        response.setData(otpvalue);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/otp")
    public ResponseEntity<BaseResponse> otpForgot(@Valid @RequestBody OTPRequest wrapper) throws Exception {

        BaseResponse<Object> response = new BaseResponse<>();
        Users user = userService.findUsersByUsersNameAndEmail(wrapper.getUserName(), wrapper.getEmail());

        if (user == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.USER_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (user.getIsGoogle() == 1) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.ACCOUNT_GOOLE_IS_NOT_PERMIT);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Random rand = new Random();
        int otpvalue = rand.nextInt(1255650);
        sendEmail.sendSimpleEmail(wrapper.getEmail(), "Mã OTP",
                "Mã OTP là:" + otpvalue + ". Mã otp này có thời hạn là 3p");

        user.setOtp(otpvalue);
        user.setOtpCreatedAt(new Date());

        userService.update(user);

        response.setData(otpvalue);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/confirm-otp")
    public ResponseEntity<BaseResponse> confirmOtp(@Valid @RequestBody ConfirmOtpRequest wrapper) throws Exception {

        BaseResponse<Object> response = new BaseResponse<>();

        /*
         * type = 0 => otp register || type = 1 => otp forgot password
         */

        if (wrapper.getType() == OtpEnum.REGISTER.getValue()) {

            UserRegister userRegister = userRegisterService.findUsersRegisterByUsersNameAndEmail(wrapper.getUserName(),
                    wrapper.getEmail());

            if (userRegister == null) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessageError(StringErrorValue.OTP_IS_NOT_USING);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            if (userRegister.getOtp() != wrapper.getOtp()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessageError(StringErrorValue.OTP_IS_NOT_CORRECT);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            // xử lí thời gian mã OTP.Quy định mã otp có thời hạn trong 3 phút

            if (this.caculateOtpExpired(userRegister.getOtpCreatedAt()) > 0) {
                userRegister.setStatus(0);
                userRegisterService.update(userRegister);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessageError(StringErrorValue.OTP_IS_EXPIRED);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } else {
            Users user = userService.findUsersByUsersNameAndEmail(wrapper.getUserName(), wrapper.getEmail());
            if (user == null) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessageError(StringErrorValue.USER_NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            if (user.getOtp() != wrapper.getOtp()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessageError(StringErrorValue.OTP_IS_NOT_CORRECT);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            // xử lí thời gian mã OTP.Quy định mã otp có thời hạn trong 3 phút
            if (this.caculateOtpExpired(user.getOtpCreatedAt()) > 0) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessageError(StringErrorValue.OTP_IS_EXPIRED);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            user.setIsConfirmOtp(1);
            userService.update(user);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login-google")
    public ResponseEntity<BaseResponse<JwtResponse>> loginGoogle(@Valid @RequestBody GoogleAccountRequest wrapper)
            throws Exception {

        BaseResponse<JwtResponse> response = new BaseResponse<>();
        Users registerUser = new Users();
        String token;
        Users users = userService.findUsersByEmail(wrapper.getEmail(), 1);

        // Nếu chưa có user thì tạo và gọi api login để set Token
        if (users == null) {
            registerUser.setEmail(wrapper.getEmail());
            registerUser.setUserName(wrapper.getEmail());
            registerUser.setAvatarUrl(wrapper.getImageUrl());
            registerUser.setIsGoogle(1);
            registerUser.setPassword(Utils.encodeBase64(applicationProperties.getPasswordAccountGoogle()));
            registerUser.setFullName(wrapper.getFullname());
            registerUser.setIsActive(1);
            userService.create(registerUser);

            token = HttpService.login(wrapper.getEmail(), applicationProperties.getPasswordAccountGoogle(),
                    applicationProperties.getBaseUrl());
            registerUser.setAccessToken(token);
            registerUser.setIsLogin(1);
            response.setData(new JwtResponse(token));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        token = HttpService.login(users.getUserName(), applicationProperties.getPasswordAccountGoogle(),
                applicationProperties.getBaseUrl());
        registerUser.setIsLogin(1);
        response.setData(new JwtResponse(token));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/banners")
    public ResponseEntity<BaseResponse<BaseListDataResponse<BannerResponse>>> getBanners(
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

    @GetMapping("/categories")
    public ResponseEntity<BaseResponse<BaseListDataResponse<CategoryResponse>>> getCategories(
            @RequestParam(name = "parent_id", required = false, defaultValue = "-1") int parentId,
            @RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
            @RequestParam(name = "status", required = false, defaultValue = "-1") int status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
        BaseResponse<BaseListDataResponse<CategoryResponse>> response = new BaseResponse<>();
        Pagination pagination = new Pagination(page, limit);
        StoreProcedureListResult<Category> listCategory = categoryService.spGListCategory(
                parentId, keySearch, status, pagination);

        BaseListDataResponse<CategoryResponse> listData = new BaseListDataResponse<>();
        listData.setList(new CategoryResponse().mapToList(listCategory.getResult()));
        listData.setTotalRecord(listCategory.getTotalRecord());

        response.setData(listData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/brands")
    public ResponseEntity<BaseResponse<BaseListDataResponse<BrandResponse>>> getBrands(
            @RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
            @RequestParam(name = "status", required = false, defaultValue = "-1") int status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
        BaseResponse<BaseListDataResponse<BrandResponse>> response = new BaseResponse<>();
        Pagination pagination = new Pagination(page, limit);
        StoreProcedureListResult<Brand> listBrand = brandService.spGListBrand(keySearch, status, pagination);

        BaseListDataResponse<BrandResponse> listData = new BaseListDataResponse<>();
        listData.setList(new BrandResponse().mapToList(listBrand.getResult()));
        listData.setTotalRecord(listBrand.getTotalRecord());

        response.setData(listData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<BaseResponse<BaseListDataResponse<ProductResponse>>> getProducts(
            @RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
            @RequestParam(name = "status", required = false, defaultValue = "-1") int status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
        BaseResponse<BaseListDataResponse<ProductResponse>> response = new BaseResponse<>();
        Pagination pagination = new Pagination(page, limit);
        StoreProcedureListResult<Product> listProduct = productService.spGListProduct(keySearch, status, pagination);

        BaseListDataResponse<ProductResponse> listData = new BaseListDataResponse<>();
        listData.setList(new ProductResponse().mapToList(listProduct.getResult()));
        listData.setTotalRecord(listProduct.getTotalRecord());

        response.setData(listData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/sizes")
    public ResponseEntity<BaseResponse<BaseListDataResponse<SizeResponse>>> getSizes(
            @RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
            @RequestParam(name = "status", required = false, defaultValue = "-1") int status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
        BaseResponse<BaseListDataResponse<SizeResponse>> response = new BaseResponse<>();
        Pagination pagination = new Pagination(page, limit);
        StoreProcedureListResult<Size> listSize = sizeService.spGListSize(keySearch, status, pagination);

        BaseListDataResponse<SizeResponse> listData = new BaseListDataResponse<>();
        listData.setList(new SizeResponse().mapToList(listSize.getResult()));
        listData.setTotalRecord(listSize.getTotalRecord());

        response.setData(listData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/materials")
    public ResponseEntity<BaseResponse<BaseListDataResponse<MaterialsResponse>>> getMaterials(
            @RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
            @RequestParam(name = "status", required = false, defaultValue = "-1") int status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
        BaseResponse<BaseListDataResponse<MaterialsResponse>> response = new BaseResponse<>();
        Pagination pagination = new Pagination(page, limit);
        StoreProcedureListResult<Materials> listMaterials = materialsService.spGListMaterials(keySearch, status, pagination);

        BaseListDataResponse<MaterialsResponse> listData = new BaseListDataResponse<>();
        listData.setList(new MaterialsResponse().mapToList(listMaterials.getResult()));
        listData.setTotalRecord(listMaterials.getTotalRecord());

        response.setData(listData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/colors")
    public ResponseEntity<BaseResponse<BaseListDataResponse<ColorResponse>>> getColors(
            @RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
            @RequestParam(name = "status", required = false, defaultValue = "-1") int status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
        BaseResponse<BaseListDataResponse<ColorResponse>> response = new BaseResponse<>();
        Pagination pagination = new Pagination(page, limit);
        StoreProcedureListResult<Color> listColor = colorService.spGListColor(keySearch, status, pagination);

        BaseListDataResponse<ColorResponse> listData = new BaseListDataResponse<>();
        listData.setList(new ColorResponse().mapToList(listColor.getResult()));
        listData.setTotalRecord(listColor.getTotalRecord());

        response.setData(listData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/product-details")
    public ResponseEntity<BaseResponse<BaseListDataResponse<ProductDetailResponse>>> getProductDetails(
            @RequestParam(name = "product_id", required = false, defaultValue = "-1") int productId,
            @RequestParam(name = "color_id", required = false, defaultValue = "-1") int colorId,
            @RequestParam(name = "size_id", required = false, defaultValue = "-1") int sizeId,
            @RequestParam(name = "material_id", required = false, defaultValue = "-1") int materialId,
            @RequestParam(name = "brand_id", required = false, defaultValue = "-1") int brandId,
            @RequestParam(name = "category_id", required = false, defaultValue = "-1") int categoryId,
            @RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
            @RequestParam(name = "status", required = false, defaultValue = "-1") int status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
        BaseResponse<BaseListDataResponse<ProductDetailResponse>> response = new BaseResponse<>();
        Pagination pagination = new Pagination(page, limit);
        StoreProcedureListResult<ProductDetail> listProductDetail = productDetailService.spGListProductDetail(
            productId, colorId, sizeId, materialId, brandId, categoryId, keySearch, status, pagination);

        BaseListDataResponse<ProductDetailResponse> listData = new BaseListDataResponse<>();
        listData.setList(new ProductDetailResponse().mapToList(listProductDetail.getResult()));
        listData.setTotalRecord(listProductDetail.getTotalRecord());

        response.setData(listData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<BaseResponse<ProductResponse>> getProductById(@PathVariable("id") int id) throws Exception {
        BaseResponse<ProductResponse> response = new BaseResponse<>();
        Product product = productService.findOne(id);

        if (product == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.PRODUCT_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.setData(new ProductResponse(product));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
