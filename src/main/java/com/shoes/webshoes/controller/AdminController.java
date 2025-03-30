package com.shoes.webshoes.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoes.webshoes.entity.Order;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.WebsiteStatisticalResponse;
import com.shoes.webshoes.service.OrderService;
import com.shoes.webshoes.service.ProductDetailService;
import com.shoes.webshoes.service.StatisticalService;
import com.shoes.webshoes.service.UserService;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController extends BaseController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	ProductDetailService productDetailService;
	
	@Autowired
	StatisticalService statisticalService;

	@GetMapping("/statistical-overview")
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
	public ResponseEntity<BaseResponse<WebsiteStatisticalResponse>> getWebsiteStats() throws Exception {
	    BaseResponse<WebsiteStatisticalResponse> response = new BaseResponse<>();

	    int totalUsers = userService.getAll().size();
	    int totalProducts = productDetailService.getAll().size();
	    int totalOrders = orderService.getAll().size();

	    // Tổng doanh thu từ đơn hàng
	    BigDecimal totalRevenue = orderService.getAll().stream()
	        .map(Order::getTotalPrice)
	        .reduce(BigDecimal.ZERO, BigDecimal::add);

	    WebsiteStatisticalResponse data = new WebsiteStatisticalResponse(
	        totalUsers, totalRevenue, totalProducts, totalOrders
	    );
	    response.setData(data);

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/amount")
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
	public ResponseEntity<BaseResponse<List<Object>>> amount(
			@RequestParam(name = "number_week", required = false, defaultValue = "") int numberWeek,
			@RequestParam(name = "from_date", required = false, defaultValue = "-1") String fromDate,
			@RequestParam(name = "to_date", required = false, defaultValue = "-1") String toDate,
			@RequestParam(name = "type", required = false, defaultValue = "1") int type) throws Exception {
		BaseResponse<List<Object>> response = new BaseResponse<>();

		List<Object> list = statisticalService.statisticalAmount(numberWeek, this.formatDate(fromDate),
				this.formatDate(toDate), type);

		response.setData(list);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
