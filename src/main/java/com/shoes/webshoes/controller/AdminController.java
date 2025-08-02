package com.shoes.webshoes.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.shoes.webshoes.entity.Order;
import com.shoes.webshoes.entity.ReturnRequest;
import com.shoes.webshoes.common.enums.ReturnStatus;
import com.shoes.webshoes.common.enums.StatusOrderEnum;
import com.shoes.webshoes.response.BaseResponse;
import com.shoes.webshoes.response.WebsiteStatisticalResponse;
import com.shoes.webshoes.service.OrderService;
import com.shoes.webshoes.service.ProductDetailService;
import com.shoes.webshoes.service.ReturnRequestService;
import com.shoes.webshoes.service.StatisticalService;
import com.shoes.webshoes.service.UserService;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController extends BaseController {
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductDetailService productDetailService;
    
    @Autowired
    private ReturnRequestService returnRequestService;
    
    @Autowired
    private StatisticalService statisticalService;

    private boolean isValidOrderForRevenue(Order order, LocalDate date, String timeType) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate orderDate = order.getCreatedAt().toInstant().atZone(defaultZoneId).toLocalDate();

        // Kiểm tra đơn hàng đã giao thành công
        boolean isDelivered = order.getStatus() == StatusOrderEnum.DELIVERED.getValue();

        // Kiểm tra thời gian theo loại (ngày/tháng/năm)
        boolean isValidTime;
        switch (timeType) {
            case "day":
                isValidTime = orderDate.equals(date);
                break;
            case "month":
                isValidTime = YearMonth.from(orderDate).equals(YearMonth.from(date));
                break;
            case "year":
                isValidTime = orderDate.getYear() == date.getYear();
                break;
            case "all":
                isValidTime = true; // Cho tổng doanh thu
                break;
            default:
                isValidTime = false;
                break;
        }

        // Kiểm tra không có yêu cầu trả hàng hợp lệ
        List<ReturnRequest> returnRequests = returnRequestService.getReturnRequestsByOrderId(order.getId());
        boolean hasNoValidReturnRequest = returnRequests.isEmpty() || 
            returnRequests.stream().allMatch(request -> 
                request.getStatus() == ReturnStatus.REJECTED || 
                request.getStatus() == ReturnStatus.CANCELLED);

        return isDelivered && isValidTime && hasNoValidReturnRequest;
    }

    private BigDecimal calculateRevenue(List<Order> orders, LocalDate date, String timeType) {
        return orders.stream()
            .filter(order -> isValidOrderForRevenue(order, date, timeType))
            .map(Order::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @GetMapping("/statistical-overview")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<BaseResponse<WebsiteStatisticalResponse>> getStatisticalOverview() {
        List<Order> allOrders = orderService.getAll();
        LocalDate today = LocalDate.now();

        // Tổng doanh thu từ đơn hàng
//        BigDecimal totalRevenue =  calculateRevenue(allOrders, today, "all");
        BigDecimal totalRevenue = BigDecimal.ZERO;

        // Doanh thu ngày hiện tại
//        BigDecimal dailyRevenue = calculateRevenue(allOrders, today, "day");
        BigDecimal dailyRevenue = BigDecimal.ZERO;

        // Doanh thu tháng hiện tại
//        BigDecimal monthlyRevenue = calculateRevenue(allOrders, today, "month");
        BigDecimal monthlyRevenue = BigDecimal.ZERO;

        // Doanh thu năm hiện tại
//        BigDecimal yearlyRevenue = calculateRevenue(allOrders, today, "year");
        BigDecimal yearlyRevenue = BigDecimal.ZERO;

        // Tổng số đơn hàng
        int totalOrders = allOrders.size();

        // Tổng số người dùng
        int totalUsers = 0;
		try {
			totalUsers = userService.getAll().size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Tổng số sản phẩm
        int totalProducts = productDetailService.getAll().size();

        // Tạo response
        WebsiteStatisticalResponse data = new WebsiteStatisticalResponse(
            totalUsers, totalRevenue, totalProducts, totalOrders,
            dailyRevenue, monthlyRevenue, yearlyRevenue
        );

        BaseResponse<WebsiteStatisticalResponse> response = new BaseResponse<>();
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
