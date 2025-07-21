# Price Field Update Summary

## Tổng quan
Đã cập nhật hệ thống để hỗ trợ trường `price` trong `ReturnRequestDetail` entity.

## Các thay đổi đã thực hiện

### 1. Entity
✅ **ReturnRequestDetail.java**
- Thêm trường `price` (Double) - đã có sẵn

### 2. Request Model
✅ **ReturnRequestDetailRequest.java**
- Thêm trường `price` với annotation `@JsonProperty("price")`

### 3. Response Model
✅ **ReturnRequestDetailResponse.java**
- Thêm trường `price` trong response
- Cập nhật constructor để map từ entity

### 4. Service Layer
✅ **ReturnRequestDetailServiceImpl.java**
- Cập nhật `createReturnRequestDetail()` để xử lý `price`
- Cập nhật `updateReturnRequestDetail()` để xử lý `price`

✅ **ReturnRequestServiceImpl.java**
- Cập nhật `createReturnRequest()` để lưu `price` khi tạo details

### 5. Documentation
✅ **PRODUCT_ID_UPDATE_SUMMARY.md**
- Cập nhật examples để bao gồm `price`

✅ **README_RETURN_REQUEST_APIS.md**
- Cập nhật request/response examples để bao gồm `price`

## Cấu trúc dữ liệu mới

### ReturnRequestDetail
```json
{
    "id": 1,
    "return_request_id": 1,
    "product_id": 100,
    "product_detail_id": 789,
    "price": 150000.0,           // ← Mới thêm
    "quantity": 1,
    "return_reason": "Size không vừa",
    "condition_description": "Sản phẩm còn nguyên vẹn",
    "images": ["image1.jpg"]
}
```

## Ví dụ sử dụng

### 1. Tạo Return Request với Price
```json
{
    "orderId": 123,
    "userId": 456,
    "returnReason": "Sản phẩm không đúng mô tả",
    "returnType": "RETURN_FULL",
    "details": [
        {
            "productId": 100,
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

### 2. Response với Price
```json
{
    "code": 200,
    "message": "Success",
    "data": {
        "id": 1,
        "order_id": 123,
        "user_id": 456,
        "return_reason": "Sản phẩm không đúng mô tả",
        "return_type": "RETURN_FULL",
        "status": "PENDING",
        "admin_notes": null,
        "created_at": "2024-01-15T10:30:00",
        "updated_at": "2024-01-15T10:30:00",
        "details": [
            {
                "id": 1,
                "return_request_id": 1,
                "product_id": 100,
                "product_detail_id": 789,
                "price": 150000.0,      // ← Mới thêm
                "quantity": 1,
                "return_reason": "Size không vừa",
                "condition_description": "Sản phẩm còn nguyên vẹn",
                "images": ["image1.jpg"]
            }
        ]
    }
}
```

## Lợi ích của việc thêm Price

### 1. Tracking giá trị
- Lưu trữ giá gốc của sản phẩm tại thời điểm tạo yêu cầu trả hàng
- Tránh ảnh hưởng khi giá sản phẩm thay đổi sau này

### 2. Tính toán hoàn tiền
- Dễ dàng tính toán số tiền cần hoàn trả
- Hỗ trợ tính toán phí ship, phí xử lý

### 3. Báo cáo và thống kê
- Thống kê giá trị hàng trả theo thời gian
- Phân tích xu hướng giá trị trả hàng

### 4. Business Logic
- Validate giá trị trả hàng
- Tính toán chênh lệch giá khi đổi hàng

## Database Migration

Nếu cần migrate database hiện tại, thực hiện các bước sau:

```sql
-- Thêm cột price vào return_request_details (nếu chưa có)
ALTER TABLE return_request_details ADD COLUMN price DECIMAL(10,2);

-- Cập nhật giá trị mặc định nếu cần
UPDATE return_request_details SET price = 0 WHERE price IS NULL;
```

## Testing

### Test cases cần thêm:
1. Tạo return request với price hợp lệ
2. Tạo return request với price = null
3. Tạo return request với price = 0
4. Update return request detail với price mới
5. Validate price không âm (nếu cần)

## Kết luận

✅ **Hoàn thành**: Tất cả các thay đổi đã được thực hiện
✅ **Backward compatible**: Vẫn hỗ trợ dữ liệu cũ (price = null)
✅ **Documentation**: Đã cập nhật đầy đủ
✅ **Service Layer**: Đã cập nhật logic xử lý

Hệ thống giờ đây hỗ trợ đầy đủ trường `price` cho return request details! 