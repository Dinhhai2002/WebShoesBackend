# Cancel Order Request APIs Documentation

## Overview
API quản lý yêu cầu hủy đơn hàng cho hệ thống e-commerce.

## Database Schema

### 1. cancel_order_requests Table
```sql
CREATE TABLE cancel_order_requests (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    user_id INT NOT NULL,
    cancel_reason TEXT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    admin_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### 2. cancel_order_history Table
```sql
CREATE TABLE cancel_order_history (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cancel_request_id INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    notes TEXT,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cancel_request_id) REFERENCES cancel_order_requests(id) ON DELETE CASCADE
);
```

## API Endpoints

### 1. Create Cancel Request
**POST** `/api/v1/cancel-orders`

Tạo yêu cầu hủy đơn hàng mới.

#### Request Body:
```json
{
    "order_id": 123,
    "user_id": 456,
    "cancel_reason": "Đổi ý không muốn mua nữa"
}
```

#### Response:
```json
{
    "code": 200,
    "message": "Tạo yêu cầu hủy đơn hàng thành công",
    "data": {
        "id": 1,
        "order_id": 123,
        "user_id": 456,
        "cancel_reason": "Đổi ý không muốn mua nữa",
        "status": "PENDING",
        "admin_notes": null,
        "created_at": "2024-01-15T10:30:00",
        "updated_at": "2024-01-15T10:30:00"
    }
}
```

### 2. Approve Cancel Request
**POST** `/api/v1/cancel-orders/{id}/approve`

Admin duyệt yêu cầu hủy đơn hàng.

#### Request Body:
```json
{
    "admin_notes": "Đã kiểm tra và đồng ý hủy đơn hàng"
}
```

### 3. Reject Cancel Request
**POST** `/api/v1/cancel-orders/{id}/reject`

Admin từ chối yêu cầu hủy đơn hàng.

#### Request Body:
```json
{
    "admin_notes": "Đơn hàng đã được xử lý, không thể hủy"
}
```

### 4. Get Cancel Request by ID
**GET** `/api/v1/cancel-orders/{id}`

### 5. Get Cancel Request by Order ID
**GET** `/api/v1/cancel-orders/order/{orderId}`

### 6. Get Cancel Requests by User ID
**GET** `/api/v1/cancel-orders/user/{userId}`

### 7. Get Cancel Requests by Status
**GET** `/api/v1/cancel-orders/status/{status}`

### 8. Get All Cancel Requests
**GET** `/api/v1/cancel-orders`

### 9. Delete Cancel Request
**DELETE** `/api/v1/cancel-orders/{id}`

### 10. Search Cancel Requests
**GET** `/api/v1/cancel-orders/search`

#### Query Parameters:
- `user_id` (optional): Filter by user ID
- `key_search` (optional): Search in cancel reason, user name, phone, or order code
- `status` (optional): Filter by status
- `page` (optional): Page number (default: 1)
- `limit` (optional): Items per page (default: 10)

## Status Values

### Cancel Request Status:
- `PENDING`: Chờ xử lý
- `APPROVED`: Đã duyệt
- `REJECTED`: Đã từ chối

## Business Logic

### Cancel Request Flow
1. Người dùng tạo yêu cầu hủy đơn hàng (PENDING)
2. Admin xem xét yêu cầu
3. Admin có thể:
   - Duyệt yêu cầu (APPROVED) -> Đơn hàng sẽ được hủy
   - Từ chối yêu cầu (REJECTED) -> Đơn hàng vẫn giữ nguyên

### Validation Rules
1. Mỗi đơn hàng chỉ được tạo một yêu cầu hủy
2. Đơn hàng phải tồn tại trong hệ thống
3. Lưu lại lịch sử thay đổi trạng thái

## Testing

### Test tạo yêu cầu hủy:
```bash
curl -X POST http://localhost:80/api/v1/cancel-orders \
  -H "Content-Type: application/json" \
  -d '{
    "order_id": 123,
    "user_id": 456,
    "cancel_reason": "Đổi ý không muốn mua nữa"
}'
```

### Test duyệt yêu cầu:
```bash
curl -X POST http://localhost:80/api/v1/cancel-orders/1/approve \
  -H "Content-Type: application/json" \
  -d '{
    "admin_notes": "Đã kiểm tra và đồng ý hủy đơn hàng"
}'
```

## Error Handling

Tất cả API đều trả về response với format:
```json
{
    "code": 500,
    "message": "Error message",
    "data": null
}
```

## Database Migration

Nếu cần migrate database hiện tại, thực hiện các bước sau:

```sql
-- Tạo bảng cancel_order_requests
CREATE TABLE cancel_order_requests (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    user_id INT NOT NULL,
    cancel_reason TEXT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    admin_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tạo bảng cancel_order_history
CREATE TABLE cancel_order_history (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cancel_request_id INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    notes TEXT,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cancel_request_id) REFERENCES cancel_order_requests(id) ON DELETE CASCADE
);

-- Tạo indexes
CREATE INDEX idx_cancel_order_requests_order_id ON cancel_order_requests(order_id);
CREATE INDEX idx_cancel_order_requests_user_id ON cancel_order_requests(user_id);
CREATE INDEX idx_cancel_order_requests_status ON cancel_order_requests(status);
CREATE INDEX idx_cancel_order_history_cancel_request_id ON cancel_order_history(cancel_request_id);
``` 