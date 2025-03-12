package com.shoes.webshoes.service;

import java.util.List;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.entity.CartDetail;

public interface CartDetailService {
	void create(CartDetail cartDetail);

	CartDetail findOne(int id);

	void update(CartDetail cartDetail);

	List<CartDetail> getAll();

	StoreProcedureListResult<CartDetail> spGListCartDetail(int cartId, int productDetailId, String keySearch,
			int status, Pagination pagination) throws Exception;

	CartDetail findByName(String name);
	
	void delete(int id);
}
