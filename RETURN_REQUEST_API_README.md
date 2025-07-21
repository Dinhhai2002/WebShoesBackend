# Return Request API Documentation

## Tổng quan
API quản lý yêu cầu trả hàng/đổi hàng cho ứng dụng thương mại điện tử.

## Database Schema
```sql
-- Bảng yêu cầu trả hàng
CREATE TABLE return_requests (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    user_id INT NOT NULL,
    return_reason VARCHAR(500),
    return_type ENUM('REFUND', 'EXCHANGE', 'PARTIAL_REFUND'),
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'PROCESSING', 'COMPLETED', 'CANCELLED'),
    admin_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng chi tiết trả hàng
CREATE TABLE return_request_details (
    id INT PRIMARY KEY AUTO_INCREMENT,
    return_request_id INT NOT NULL,
    product_detail_id INT NOT NULL,
    quantity INT NOT NULL,
    return_reason VARCHAR(500),
    condition_description TEXT,
    images TEXT -- JSON array of image URLs
);

-- Bảng lịch sử trả hàng
CREATE TABLE return_request_history (
    id INT PRIMARY KEY AUTO_INCREMENT,
    return_request_id INT NOT NULL,
    status VARCHAR(50),
    notes TEXT,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## API Endpoints

### 1. Tạo yêu cầu trả hàng
**Endpoint:** `POST /api/v1/return-requests`

**Request Body:**
```json
{
    "orderId": 123,
    "userId": 456,
    "returnReason": "Sản phẩm không đúng mô tả",
    "returnType": "REFUND",
    "details": [
        {
            "productDetailId": 789,
            "quantity": 1,
            "returnReason": "Size không vừa",
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
        "orderId": 123,
        "userId": 456,
        "returnReason": "Sản phẩm không đúng mô tả",
        "returnType": "REFUND",
        "status": "PENDING",
        "adminNotes": null,
        "createdAt": "2024-01-15T10:30:00",
        "updatedAt": "2024-01-15T10:30:00",
        "details": []
    }
}
```

### 2. Lấy yêu cầu trả hàng theo ID
**Endpoint:** `GET /api/v1/return-requests/{id}`

**Response:**
```json
{
    "code": 200,
    "message": "Success",
    "data": {
        "id": 1,
        "orderId": 123,
        "userId": 456,
        "returnReason": "Sản phẩm không đúng mô tả",
        "returnType": "REFUND",
        "status": "PENDING",
        "adminNotes": null,
        "createdAt": "2024-01-15T10:30:00",
        "updatedAt": "2024-01-15T10:30:00",
        "details": []
    }
}
```

### 3. Lấy danh sách yêu cầu trả hàng của user
**Endpoint:** `GET /api/v1/return-requests/user/{userId}`

**Response:**
```json
{
    "code": 200,
    "message": "Success",
    "data": [
        {
            "id": 1,
            "orderId": 123,
            "userId": 456,
            "returnReason": "Sản phẩm không đúng mô tả",
            "returnType": "REFUND",
            "status": "PENDING",
            "adminNotes": null,
            "createdAt": "2024-01-15T10:30:00",
            "updatedAt": "2024-01-15T10:30:00",
            "details": []
        }
    ]
}
```

### 4. Lấy danh sách yêu cầu trả hàng theo đơn hàng
**Endpoint:** `GET /api/v1/return-requests/order/{orderId}`

### 5. Admin: Lấy tất cả yêu cầu trả hàng
**Endpoint:** `GET /api/v1/return-requests/admin?status=PENDING`

**Query Parameters:**
- `status` (optional): Lọc theo trạng thái (PENDING, APPROVED, REJECTED, PROCESSING, COMPLETED, CANCELLED)

### 6. Admin: Duyệt yêu cầu trả hàng
**Endpoint:** `PUT /api/v1/return-requests/{id}/approve`

**Request Body:**
```json
{
    "adminNotes": "Đã kiểm tra và đồng ý trả hàng"
}
```

### 7. Admin: Từ chối yêu cầu trả hàng
**Endpoint:** `PUT /api/v1/return-requests/{id}/reject`

**Request Body:**
```json
{
    "adminNotes": "Sản phẩm đã được sử dụng, không thể trả hàng"
}
```

### 8. Admin: Bắt đầu xử lý trả hàng
**Endpoint:** `PUT /api/v1/return-requests/{id}/process`

### 9. Admin: Hoàn thành trả hàng
**Endpoint:** `PUT /api/v1/return-requests/{id}/complete`

### 10. Hủy yêu cầu trả hàng
**Endpoint:** `PUT /api/v1/return-requests/{id}/cancel`

### 11. Xóa yêu cầu trả hàng
**Endpoint:** `DELETE /api/v1/return-requests/{id}`

## Trạng thái yêu cầu trả hàng

### ReturnStatus Enum:
- `PENDING`: Chờ duyệt
- `APPROVED`: Đã duyệt
- `REJECTED`: Từ chối
- `PROCESSING`: Đang xử lý
- `COMPLETED`: Hoàn thành
- `CANCELLED`: Đã hủy

### ReturnType Enum:
- `REFUND`: Hoàn tiền
- `EXCHANGE`: Đổi hàng
- `PARTIAL_REFUND`: Hoàn tiền một phần

## Quy trình xử lý

### 1. Khách hàng tạo yêu cầu
- Gọi API `POST /api/v1/return-requests`
- Trạng thái: `PENDING`

### 2. Admin review
- Gọi API `PUT /api/v1/return-requests/{id}/approve` hoặc `PUT /api/v1/return-requests/{id}/reject`
- Trạng thái: `APPROVED` hoặc `REJECTED`

### 3. Xử lý trả hàng
- Gọi API `PUT /api/v1/return-requests/{id}/process`
- Trạng thái: `PROCESSING`

### 4. Hoàn thành
- Gọi API `PUT /api/v1/return-requests/{id}/complete`
- Trạng thái: `COMPLETED`

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

### Test tạo yêu cầu trả hàng:
```bash
curl -X POST http://localhost:80/api/v1/return-requests \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": 123,
    "userId": 456,
    "returnReason": "Sản phẩm không đúng mô tả",
    "returnType": "REFUND",
    "details": [
        {
            "productDetailId": 789,
            "quantity": 1,
            "returnReason": "Size không vừa",
            "conditionDescription": "Sản phẩm còn nguyên vẹn",
            "images": ["image1.jpg", "image2.jpg"]
        }
    ]
}'
```

### Test duyệt yêu cầu:
```bash
curl -X PUT http://localhost:80/api/v1/return-requests/1/approve \
  -H "Content-Type: application/json" \
  -d '{
    "adminNotes": "Đã kiểm tra và đồng ý trả hàng"
}'
```

## Lưu ý

1. **Validation**: Cần validate dữ liệu đầu vào trước khi lưu
2. **Authorization**: Cần kiểm tra quyền truy cập cho các API admin
3. **Business Logic**: Cần implement logic nghiệp vụ trong service layer
4. **Notification**: Cần gửi thông báo khi trạng thái thay đổi
5. **History**: Cần lưu lịch sử thay đổi trạng thái 