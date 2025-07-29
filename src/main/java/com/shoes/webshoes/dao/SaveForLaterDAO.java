package com.shoes.webshoes.dao;

import com.shoes.webshoes.entity.SaveForLater;
import java.util.List;

public interface SaveForLaterDAO {
    SaveForLater create(SaveForLater saveForLater);
    SaveForLater findById(Integer id);
    List<SaveForLater> findByUserId(Integer userId);
    SaveForLater findByUserIdAndProductDetailId(Integer userId, Integer productDetailId);
    void delete(Integer id);
    void deleteByUserIdAndProductDetailId(Integer userId, Integer productDetailId);
} 