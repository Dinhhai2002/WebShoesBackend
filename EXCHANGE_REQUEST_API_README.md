# Exchange Request API Documentation

## Tổng quan
API quản lý yêu cầu đổi hàng cho ứng dụng thương mại điện tử. Hệ thống đổi hàng hoạt động độc lập với hệ thống trả hàng nhưng có liên kết thông qua return_request_id.

## Database Schema
```sql
-- Bảng exchange_requests
CREATE TABLE exchange_requests (
    id INT PRIMARY KEY AUTO_INCREMENT,
    return_request_id INT NOT NULL,
    exchange_reason TEXT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'PROCESSING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    admin_notes TEXT,
    price_difference DECIMAL(10,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (return_request_id) REFERENCES return_requests(id) ON DELETE CASCADE
);

-- Bảng exchange_request_details
CREATE TABLE exchange_request_details (
    id INT PRIMARY KEY AUTO_INCREMENT,
    exchange_request_id INT NOT NULL,
    old_product_detail_id INT NOT NULL,
    new_product_detail_id INT NOT NULL,
    quantity INT NOT NULL,
    exchange_reason TEXT,
    condition_description TEXT,
    images JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (exchange_request_id) REFERENCES exchange_requests(id) ON DELETE CASCADE,
    FOREIGN KEY (old_product_detail_id) REFERENCES product_details(id),
    FOREIGN KEY (new_product_detail_id) REFERENCES product_details(id)
);
```

## API Endpoints

### 1. Tạo yêu cầu đổi hàng
**Endpoint:** `POST /api/v1/exchange-requests`

**Request Body:**
```json
{
    "returnRequestId": 123,
    "exchangeReason": "Sản phẩm không đúng size",
    "details": [
        {
            "oldProductId": 100,
            "oldProductDetailId": 789,
            "newProductId": null,
            "newProductDetailId": null,
            "quantity": 1,
            "exchangeReason": "Size không vừa",
            "conditionDescription": "Sản phẩm còn nguyên vẹn",
            "images": ["image1.jpg", "image2.jpg"]
        }
    ]
}
```

**Response:**
```json
{
    "code": 200,
    "message": "Success",
    "data": {
        "id": 1,
        "return_request_id": 123,
        "exchange_reason": "Sản phẩm không đúng size",
        "status": "PENDING",
        "admin_notes": null,
        "price_difference": 0,
        "created_at": "2024-01-15T10:30:00",
        "updated_at": "2024-01-15T10:30:00",
        "details": [
            {
                "id": 1,
                "exchange_request_id": 1,
                "old_product_id": 100,
                "old_product_detail_id": 789,
                "new_product_id": null,
                "new_product_detail_id": null,
                "quantity": 1,
                "exchange_reason": "Size không vừa",
                "condition_description": "Sản phẩm còn nguyên vẹn",
                "images": ["image1.jpg", "image2.jpg"]
            }
        ]
    }
}
```

### 2. Lấy yêu cầu đổi hàng theo ID
**Endpoint:** `GET /api/v1/exchange-requests/{id}`

**Response:** Tương tự như response của tạo yêu cầu

### 3. Lấy yêu cầu đổi hàng theo return request ID
**Endpoint:** `GET /api/v1/exchange-requests/return-request/{returnRequestId}`

**Response:** Tương tự như response của tạo yêu cầu

### 4. Admin: Lấy tất cả yêu cầu đổi hàng
**Endpoint:** `GET /api/v1/exchange-requests/admin`

**Response:**
```json
{
    "code": 200,
    "message": "Success",
    "data": [
        {
            "id": 1,
            "return_request_id": 123,
            "exchange_reason": "Sản phẩm không đúng size",
            "status": "PENDING",
            "admin_notes": null,
            "price_difference": 0,
            "created_at": "2024-01-15T10:30:00",
            "updated_at": "2024-01-15T10:30:00",
            "details": [...]
        }
    ]
}
```

### 5. Admin: Duyệt yêu cầu đổi hàng
**Endpoint:** `POST /api/v1/exchange-requests/{id}/approve`

**Request Body:**
```json
{
    "adminNotes": "Đã kiểm tra và chấp nhận yêu cầu đổi hàng",
    "priceDifference": 50000,
    "exchangeProducts": [
        {
            "oldProductId": 100,
            "oldProductDetailId": 789,
            "newProductId": 101,
            "newProductDetailId": 790,
            "quantity": 1,
            "exchangeReason": "Đổi sang size lớn hơn"
        }
    ]
}
```

**Response:** Tương tự như response của tạo yêu cầu với status = "APPROVED"

### 6. Admin: Từ chối yêu cầu đổi hàng
**Endpoint:** `POST /api/v1/exchange-requests/{id}/reject`

**Request Body:**
```json
{
    "adminNotes": "Sản phẩm đã được sử dụng, không thể đổi hàng"
}
```

**Response:** Tương tự như response của tạo yêu cầu với status = "REJECTED"

### 7. Admin: Bắt đầu xử lý đổi hàng
**Endpoint:** `POST /api/v1/exchange-requests/{id}/process`

**Response:** Tương tự như response của tạo yêu cầu với status = "PROCESSING"

### 8. Admin: Hoàn thành đổi hàng
**Endpoint:** `POST /api/v1/exchange-requests/{id}/complete`

**Response:** Tương tự như response của tạo yêu cầu với status = "COMPLETED"

### 9. Hủy yêu cầu đổi hàng
**Endpoint:** `POST /api/v1/exchange-requests/{id}/cancel`

**Response:**
```json
{
    "code": 200,
    "message": "Success",
    "data": null
}
```

### 10. Xóa yêu cầu đổi hàng
**Endpoint:** `POST /api/v1/exchange-requests/{id}/deleted`

**Response:**
```json
{
    "code": 200,
    "message": "Success",
    "data": null
}
```

## Trạng thái yêu cầu đổi hàng

### ExchangeStatus Enum:
- `PENDING`: Chờ duyệt
- `APPROVED`: Đã duyệt
- `REJECTED`: Từ chối
- `PROCESSING`: Đang xử lý
- `COMPLETED`: Hoàn thành
- `CANCELLED`: Đã hủy

## Quy trình xử lý

### 1. Khách hàng tạo yêu cầu đổi hàng
- Gọi API `POST /api/v1/exchange-requests`
- Trạng thái: `PENDING`
- Chỉ có thông tin sản phẩm cũ, chưa có sản phẩm mới

### 2. Admin review và chọn sản phẩm mới
- Gọi API `POST /api/v1/exchange-requests/{id}/approve`
- Admin chọn sản phẩm mới phù hợp
- Hệ thống tự động:
  - Cộng lại tồn kho sản phẩm cũ
  - Trừ tồn kho sản phẩm mới
  - Tính chênh lệch giá
- Trạng thái: `APPROVED`

### 3. Xử lý đổi hàng
- Gọi API `POST /api/v1/exchange-requests/{id}/process`
- Trạng thái: `PROCESSING`

### 4. Hoàn thành
- Gọi API `POST /api/v1/exchange-requests/{id}/complete`
- Trạng thái: `COMPLETED`

## Business Logic

### 1. Validation khi tạo yêu cầu
- Kiểm tra return request có tồn tại không
- Kiểm tra return request có phải là EXCHANGE không
- Kiểm tra đã có exchange request cho return request này chưa

### 2. Validation khi duyệt yêu cầu
- Kiểm tra sản phẩm mới có tồn tại không
- Kiểm tra tồn kho sản phẩm mới có đủ không
- Tính toán chênh lệch giá

### 3. Xử lý tồn kho
- Khi duyệt: cộng lại tồn kho sản phẩm cũ, trừ tồn kho sản phẩm mới
- Khi từ chối: không thay đổi tồn kho

## Error Handling

Tất cả API đều trả về response với format:
```json
{
    "code": 500,
    "message": "Error message",
    "data": null
}
```

## Testing

### Test tạo yêu cầu đổi hàng:
```bash
curl -X POST http://localhost:80/api/v1/exchange-requests \
  -H "Content-Type: application/json" \
  -d '{
    "returnRequestId": 123,
    "exchangeReason": "Sản phẩm không đúng size",
    "details": [
        {
            "oldProductDetailId": 789,
            "newProductDetailId": null,
            "quantity": 1,
            "exchangeReason": "Size không vừa",
            "conditionDescription": "Sản phẩm còn nguyên vẹn",
            "images": ["image1.jpg", "image2.jpg"]
        }
    ]
}'
```

### Test duyệt yêu cầu:
```bash
curl -X POST http://localhost:80/api/v1/exchange-requests/1/approve \
  -H "Content-Type: application/json" \
  -d '{
    "adminNotes": "Đã kiểm tra và chấp nhận yêu cầu đổi hàng",
    "priceDifference": 50000,
    "exchangeProducts": [
        {
            "oldProductId": 100,
            "oldProductDetailId": 789,
            "newProductId": 101,
            "newProductDetailId": 790,
            "quantity": 1,
            "exchangeReason": "Đổi sang size lớn hơn"
        }
    ]
}'
```

## Lưu ý

1. **Validation**: Cần validate dữ liệu đầu vào trước khi lưu
2. **Authorization**: Cần kiểm tra quyền truy cập cho các API admin
3. **Business Logic**: Cần implement logic nghiệp vụ trong service layer
4. **Notification**: Cần gửi thông báo khi trạng thái thay đổi
5. **History**: Cần lưu lịch sử thay đổi trạng thái
6. **Stock Management**: Tự động cập nhật tồn kho khi duyệt yêu cầu
7. **Price Difference**: Tính toán và lưu chênh lệch giá giữa sản phẩm cũ và mới 