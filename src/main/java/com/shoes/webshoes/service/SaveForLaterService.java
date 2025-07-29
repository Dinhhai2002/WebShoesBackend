package com.shoes.webshoes.service;

import com.shoes.webshoes.entity.SaveForLater;
import com.shoes.webshoes.request.SaveForLaterRequest;
import java.util.List;

public interface SaveForLaterService {
    SaveForLater addToSaveForLater(SaveForLaterRequest request);
    List<SaveForLater> getSaveForLaterByUserId(Integer userId);
    void removeFromSaveForLater(Integer userId, Integer productDetailId);
    void moveToCart(Integer userId, Integer productDetailId);
} 