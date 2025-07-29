package com.shoes.webshoes.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shoes.webshoes.dao.SaveForLaterDAO;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.entity.SaveForLater;
import com.shoes.webshoes.entity.Cart;
import com.shoes.webshoes.entity.CartDetail;
import com.shoes.webshoes.request.SaveForLaterRequest;
import com.shoes.webshoes.service.SaveForLaterService;
import com.shoes.webshoes.service.CartDetailService;
import com.shoes.webshoes.service.CartService;

@Service("SaveForLaterService")
@Transactional(rollbackFor = Error.class)
public class SaveForLaterServiceImpl implements SaveForLaterService {
    
    @Autowired
    private SaveForLaterDAO saveForLaterDao;
    
    @Autowired
    private CartDetailService cartDetailService;
    
    @Autowired
    private CartService cartService;
    
    @Override
    public SaveForLater addToSaveForLater(SaveForLaterRequest request) {
        // Kiểm tra xem sản phẩm đã có trong save for later chưa
        SaveForLater existed = saveForLaterDao.findByUserIdAndProductDetailId(
            request.getUserId(), request.getProductDetailId());
            
        if (existed != null) {
            throw new RuntimeException("Sản phẩm đã có trong danh sách lưu mua sau!");
        }
        
        // Tạo mới save for later
        SaveForLater saveForLater = new SaveForLater();
        saveForLater.setUserId(request.getUserId());
        saveForLater.setProductDetailId(request.getProductDetailId());
        saveForLater.setCreatedAt(new Date());
        saveForLater.setUpdatedAt(new Date());
        
        SaveForLater saved = saveForLaterDao.create(saveForLater);
        
        // Xóa sản phẩm khỏi giỏ hàng nếu có cartDetailId
//        if (request.getCartDetailId() != null) {
//            cartDetailService.delete(request.getCartDetailId());
//        }
        
        return saved;
    }

    @Override
    public List<SaveForLater> getSaveForLaterByUserId(Integer userId) {
        return saveForLaterDao.findByUserId(userId);
    }

    @Override
    public void removeFromSaveForLater(Integer userId, Integer productDetailId) {
        saveForLaterDao.deleteByUserIdAndProductDetailId(userId, productDetailId);
    }

    @Override
    public void moveToCart(Integer userId, Integer productDetailId) {
        // Lấy thông tin save for later
        SaveForLater saveForLater = saveForLaterDao.findByUserIdAndProductDetailId(userId, productDetailId);
        if (saveForLater == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm trong danh sách lưu mua sau!");
        }
        
        List<Cart> listCart = new ArrayList<>();
		try {
			listCart = cartService.spGListCart(userId, "",
					1, new Pagination(0, 20)).getResult();
		} catch (Exception e) {
		}
        
        Cart cart = listCart.stream().findFirst().orElse(null);
        if(cart == null) {
            throw new RuntimeException("Giỏ hàng trống!");
        }
        // Thêm vào giỏ hàng
        CartDetail cartDetail = new CartDetail();
        cartDetail.setCartId(cart.getId());
        cartDetail.setProductDetailId(productDetailId);
        cartDetail.setQuantity(1); // Mặc định số lượng là 1
        cartDetailService.create(cartDetail);
        
        // Xóa khỏi save for later
        saveForLaterDao.delete(saveForLater.getId());
    }
} 