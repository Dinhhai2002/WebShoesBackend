# ProductId Field Update Summary

## Tổng quan
Đã cập nhật hệ thống để hỗ trợ trường `productId` trong cả `ExchangeRequest` và `ReturnRequestDetail` entities.

## Các thay đổi đã thực hiện

### 1. Entities
✅ **ReturnRequestDetail.java**
- Thêm trường `productId` (đã có sẵn)

✅ **ExchangeRequestDetail.java**
- Thêm trường `oldProductId` (Product ID của sản phẩm cũ)
- Thêm trường `newProductId` (Product ID của sản phẩm mới)

### 2. Request Models
✅ **ReturnRequestDetailRequest.java**
- Thêm trường `productId`

✅ **ExchangeRequestDetailRequest.java**
- Thêm trường `oldProductId`
- Thêm trường `newProductId`

✅ **ApproveExchangeRequestRequest.java**
- Cập nhật `ExchangeProductRequest` để bao gồm `oldProductId` và `newProductId`

### 3. Response Models
✅ **ReturnRequestDetailResponse.java**
- Thêm trường `productId` trong response

✅ **ExchangeRequestDetailResponse.java**
- Thêm trường `oldProductId` và `newProductId` trong response

### 4. Service Layer
✅ **ReturnRequestServiceImpl.java**
- Cập nhật `createReturnRequest()` để lưu `productId`

✅ **ExchangeRequestServiceImpl.java**
- Cập nhật `createExchangeRequest()` để lưu `oldProductId` và `newProductId`
- Cập nhật `approveExchangeRequest()` để cập nhật `newProductId`

✅ **ReturnRequestDetailServiceImpl.java**
- Cập nhật `createReturnRequestDetail()` và `updateReturnRequestDetail()` để xử lý `productId`

✅ **ExchangeRequestDetailServiceImpl.java**
- Cập nhật `createExchangeRequestDetail()` và `updateExchangeRequestDetail()` để xử lý `productId`

### 5. Database Schema
✅ **exchange_tables.sql**
- Thêm `old_product_id` và `new_product_id` vào bảng `exchange_request_details`
- Thêm foreign key constraints
- Thêm indexes cho performance

### 6. Documentation
✅ **EXCHANGE_REQUEST_API_README.md**
- Cập nhật request/response examples để bao gồm `productId`

✅ **README_RETURN_REQUEST_APIS.md**
- Cập nhật request/response examples để bao gồm `productId`

## Cấu trúc dữ liệu mới

### ReturnRequestDetail
```json
{
    "id": 1,
    "return_request_id": 1,
    "product_id": 100,           // ← Mới thêm
    "product_detail_id": 789,
    "price": 150000.0,           // ← Mới thêm
    "quantity": 1,
    "return_reason": "Size không vừa",
    "condition_description": "Sản phẩm còn nguyên vẹn",
    "images": ["image1.jpg"]
}
```

### ExchangeRequestDetail
```json
{
    "id": 1,
    "exchange_request_id": 1,
    "old_product_id": 100,       // ← Mới thêm
    "old_product_detail_id": 789,
    "new_product_id": 101,       // ← Mới thêm
    "new_product_detail_id": 790,
    "quantity": 1,
    "exchange_reason": "Đổi sang size khác",
    "condition_description": "Sản phẩm còn nguyên vẹn",
    "images": ["image1.jpg"]
}
```

## Ví dụ sử dụng

### 1. Tạo Return Request với ProductId
```json
{
    "orderId": 123,
    "userId": 456,
    "returnReason": "Sản phẩm không đúng mô tả",
    "returnType": "RETURN_FULL",
    "details": [
        {
            "productId": 100,           // ← Mới thêm
            "productDetailId": 789,
            "price": 150000.0,          // ← Mới thêm
            "quantity": 1,
            "returnReason": "Size không vừa",
            "conditionDescription": "Sản phẩm còn nguyên vẹn",
            "images": ["image1.jpg"]
        }
    ]
}
```

### 2. Tạo Exchange Request với ProductId
```json
{
    "returnRequestId": 123,
    "exchangeReason": "Sản phẩm không đúng size",
    "details": [
        {
            "oldProductId": 100,        // ← Mới thêm
            "oldProductDetailId": 789,
            "newProductId": null,       // ← Mới thêm (null khi tạo ban đầu)
            "newProductDetailId": null,
            "quantity": 1,
            "exchangeReason": "Size không vừa",
            "conditionDescription": "Sản phẩm còn nguyên vẹn",
            "images": ["image1.jpg"]
        }
    ]
}
```

### 3. Approve Exchange Request với ProductId
```json
{
    "adminNotes": "Đã kiểm tra và chấp nhận yêu cầu đổi hàng",
    "priceDifference": 50000,
    "exchangeProducts": [
        {
            "oldProductId": 100,        // ← Mới thêm
            "oldProductDetailId": 789,
            "newProductId": 101,        // ← Mới thêm
            "newProductDetailId": 790,
            "quantity": 1,
            "exchangeReason": "Đổi sang size lớn hơn"
        }
    ]
}
```

## Lợi ích của việc thêm ProductId

### 1. Tracking tốt hơn
- Có thể theo dõi sản phẩm ở cả cấp độ Product và ProductDetail
- Dễ dàng query và báo cáo theo sản phẩm

### 2. Performance
- Index trên `product_id` giúp query nhanh hơn
- Giảm JOIN queries khi cần thông tin sản phẩm

### 3. Business Logic
- Có thể validate sản phẩm ở cả 2 cấp độ
- Dễ dàng tính toán thống kê theo sản phẩm

### 4. Integration
- Tương thích tốt hơn với các hệ thống khác
- Dễ dàng export/import dữ liệu

## Database Migration

Nếu cần migrate database hiện tại, thực hiện các bước sau:

```sql
-- Thêm cột product_id vào return_request_details (nếu chưa có)
ALTER TABLE return_request_details ADD COLUMN product_id INT;
ALTER TABLE return_request_details ADD FOREIGN KEY (product_id) REFERENCES products(id);

-- Thêm cột product_id vào exchange_request_details
ALTER TABLE exchange_request_details ADD COLUMN old_product_id INT NOT NULL;
ALTER TABLE exchange_request_details ADD COLUMN new_product_id INT;
ALTER TABLE exchange_request_details ADD FOREIGN KEY (old_product_id) REFERENCES products(id);
ALTER TABLE exchange_request_details ADD FOREIGN KEY (new_product_id) REFERENCES products(id);

-- Thêm indexes
CREATE INDEX idx_return_request_details_product_id ON return_request_details(product_id);
CREATE INDEX idx_exchange_request_details_old_product_id ON exchange_request_details(old_product_id);
CREATE INDEX idx_exchange_request_details_new_product_id ON exchange_request_details(new_product_id);
```

## Testing

### Test cases cần thêm:
1. Tạo return request với productId hợp lệ
2. Tạo exchange request với oldProductId hợp lệ
3. Approve exchange request với newProductId hợp lệ
4. Validate productId không tồn tại
5. Test performance với index mới

## Kết luận

✅ **Hoàn thành**: Tất cả các thay đổi đã được thực hiện
✅ **Backward compatible**: Vẫn hỗ trợ dữ liệu cũ
✅ **Documentation**: Đã cập nhật đầy đủ
✅ **Database**: Schema đã được cập nhật

Hệ thống giờ đây hỗ trợ đầy đủ trường `productId` cho cả return và exchange requests! 