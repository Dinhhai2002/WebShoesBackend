# Return Request APIs Documentation

## Overview
This document describes the Return Request APIs for the e-commerce application, including the new filtering API that allows searching and filtering return requests with multiple parameters.

## Database Schema

### 1. return_requests Table
```sql
CREATE TABLE return_requests (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    user_id INT NOT NULL,
    return_reason TEXT,
    return_type ENUM('RETURN_FULL', 'RETURN_PARTIAL', 'EXCHANGE_FULL', 'EXCHANGE_PARTIAL', 'REFUND', 'EXCHANGE', 'PARTIAL_REFUND') NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'PROCESSING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    admin_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 2. return_request_details Table
```sql
CREATE TABLE return_request_details (
    id INT PRIMARY KEY AUTO_INCREMENT,
    return_request_id INT NOT NULL,
    product_detail_id INT NOT NULL,
    quantity INT NOT NULL,
    return_reason TEXT,
    condition_description TEXT,
    images JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 3. return_request_history Table
```sql
CREATE TABLE return_request_history (
    id INT PRIMARY KEY AUTO_INCREMENT,
    return_request_id INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    notes TEXT,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## ReturnType Enum

### New Return Types (Recommended)
- `RETURN_FULL` - Hoàn trả 100% đơn hàng
- `RETURN_PARTIAL` - Hoàn trả một phần đơn hàng
- `EXCHANGE_FULL` - Đổi 100% đơn hàng
- `EXCHANGE_PARTIAL` - Đổi một phần đơn hàng

### Legacy Return Types (Deprecated but supported)
- `REFUND` - Hoàn tiền (equivalent to RETURN_FULL)
- `EXCHANGE` - Đổi hàng (equivalent to EXCHANGE_FULL)
- `PARTIAL_REFUND` - Hoàn tiền một phần (equivalent to RETURN_PARTIAL)

### Helper Methods
```java
returnType.isReturnType()    // Check if it's a return type
returnType.isExchangeType()  // Check if it's an exchange type
returnType.isFullType()      // Check if it's full return/exchange
returnType.isPartialType()   // Check if it's partial return/exchange
```

## Stored Procedure Implementation

### sp_g_list_return_request

The filtering functionality is implemented using a MySQL stored procedure that provides efficient querying with multiple filter parameters.

```sql
DELIMITER $$

CREATE PROCEDURE sp_g_list_return_request(
    IN p_user_id INT,
    IN p_key_search VARCHAR(255),
    IN p_status VARCHAR(50),
    IN p_from_date VARCHAR(20),
    IN p_to_date VARCHAR(20),
    IN p_page INT,
    IN p_limit INT,
    OUT p_total_record INT
)
BEGIN
    DECLARE v_offset INT DEFAULT 0;
    
    -- Calculate offset
    SET v_offset = (p_page - 1) * p_limit;
    
    -- Build dynamic query
    SET @sql = CONCAT('
        SELECT SQL_CALC_FOUND_ROWS 
            rr.*,
            u.full_name as user_name,
            u.phone as user_phone
        FROM return_requests rr
        LEFT JOIN users u ON rr.user_id = u.id
        WHERE 1=1
    ');
    
    -- Add user filter
    IF p_user_id IS NOT NULL AND p_user_id > 0 THEN
        SET @sql = CONCAT(@sql, ' AND rr.user_id = ', p_user_id);
    END IF;
    
    -- Add search filter
    IF p_key_search IS NOT NULL AND p_key_search != '' THEN
        SET @sql = CONCAT(@sql, ' AND (rr.return_reason LIKE ''%', p_key_search, '%'' OR u.full_name LIKE ''%', p_key_search, '%'' OR u.phone LIKE ''%', p_key_search, '%'')');
    END IF;
    
    -- Add status filter
    IF p_status IS NOT NULL AND p_status != '' THEN
        SET @sql = CONCAT(@sql, ' AND rr.status = ''', p_status, '''');
    END IF;
    
    -- Add date range filter
    IF p_from_date IS NOT NULL AND p_from_date != '' THEN
        SET @sql = CONCAT(@sql, ' AND DATE(rr.created_at) >= ''', p_from_date, '''');
    END IF;
    
    IF p_to_date IS NOT NULL AND p_to_date != '' THEN
        SET @sql = CONCAT(@sql, ' AND DATE(rr.created_at) <= ''', p_to_date, '''');
    END IF;
    
    -- Add ordering and pagination
    SET @sql = CONCAT(@sql, ' ORDER BY rr.created_at DESC LIMIT ', v_offset, ', ', p_limit);
    
    -- Execute query
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
    
    -- Get total records
    SELECT FOUND_ROWS() INTO p_total_record;
END$$

DELIMITER ;
```

## API Endpoints

### 1. Create Return Request
**POST** `/api/v1/return-requests`

**Request Body:**
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
            "price": 150000.0,
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
                "price": 150000.0,
                "quantity": 1,
                "return_reason": "Size không vừa",
                "condition_description": "Sản phẩm còn nguyên vẹn",
                "images": ["image1.jpg", "image2.jpg"]
            }
        ]
    }
}
```

### 2. Get Return Request by ID
**GET** `/api/v1/return-requests/{id}`

### 3. Get Return Requests by User ID
**GET** `/api/v1/return-requests/user/{userId}`

### 4. Get Return Requests by Order ID
**GET** `/api/v1/return-requests/order/{orderId}`

### 5. Get All Return Requests (Admin)
**GET** `/api/v1/return-requests/admin`

### 6. Filter Return Requests
**GET** `/api/v1/return-requests?user_id=1&key_search=lỗi&status=PENDING&from_date=2024-01-01&to_date=2024-12-31&page=1&limit=20`

**Query Parameters:**
- `user_id` (optional): Filter by user ID
- `key_search` (optional): Search in return reason, user name, or phone
- `status` (optional): Filter by status
- `from_date` (optional): Start date (YYYY-MM-DD)
- `to_date` (optional): End date (YYYY-MM-DD)
- `page` (optional): Page number (default: 1)
- `limit` (optional): Items per page (default: 10)

### 7. Admin: Approve Return Request
**POST** `/api/v1/return-requests/{id}/approve`

Admin duyệt yêu cầu trả hàng.

#### Request Body:
```json
{
    "adminNotes": "Đã kiểm tra và đồng ý trả hàng"
}
```

### 8. Admin: Reject Return Request
**POST** `/api/v1/return-requests/{id}/reject`

Admin từ chối yêu cầu trả hàng.

#### Request Body:
```json
{
    "adminNotes": "Sản phẩm không đủ điều kiện trả hàng"
}
```

### 9. Admin: Process Return Request
**POST** `/api/v1/return-requests/{id}/process`

Bắt đầu xử lý trả hàng.

### 10. Admin: Complete Return Request
**POST** `/api/v1/return-requests/{id}/complete`

Hoàn thành trả hàng.

### 11. Cancel Return Request
**POST** `/api/v1/return-requests/{id}/cancel`

Hủy yêu cầu trả hàng.

### 12. Delete Return Request
**POST** `/api/v1/return-requests/{id}/deleted`

Xóa yêu cầu trả hàng.

## Status Values

### Return Status:
- `PENDING`: Chờ xử lý
- `APPROVED`: Đã duyệt
- `REJECTED`: Đã từ chối
- `PROCESSING`: Đang xử lý
- `COMPLETED`: Hoàn thành
- `CANCELLED`: Đã hủy

### Return Type:
- `RETURN_FULL`: Hoàn trả 100% đơn hàng
- `RETURN_PARTIAL`: Hoàn trả một phần đơn hàng
- `EXCHANGE_FULL`: Đổi 100% đơn hàng
- `EXCHANGE_PARTIAL`: Đổi một phần đơn hàng
- `REFUND`: Hoàn tiền (legacy)
- `EXCHANGE`: Đổi hàng (legacy)
- `PARTIAL_REFUND`: Hoàn tiền một phần (legacy)

## Usage Examples

### 1. Lấy tất cả yêu cầu trả hàng:
```
GET /api/v1/return-requests
```

### 2. Lọc theo người dùng:
```
GET /api/v1/return-requests?user_id=1
```

### 3. Tìm kiếm theo từ khóa:
```
GET /api/v1/return-requests?key_search=lỗi
```

### 4. Lọc theo trạng thái:
```
GET /api/v1/return-requests?status=PENDING
```

### 5. Lọc theo khoảng thời gian:
```
GET /api/v1/return-requests?from_date=2024-01-01&to_date=2024-12-31
```

### 6. Kết hợp nhiều bộ lọc:
```
GET /api/v1/return-requests?user_id=1&status=PENDING&from_date=2024-01-01&page=1&limit=20
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

## Business Logic

### Return Type Processing
- **RETURN_FULL/RETURN_PARTIAL**: Xử lý hoàn trả hàng, cộng lại tồn kho
- **EXCHANGE_FULL/EXCHANGE_PARTIAL**: Tạo ExchangeRequest để xử lý đổi hàng
- **Legacy types**: Tương thích ngược với logic cũ

### Stock Management
- Khi approve return request: tự động cộng lại tồn kho
- Khi approve exchange request: cộng lại tồn kho sản phẩm cũ, trừ tồn kho sản phẩm mới

## Testing

### Test tạo yêu cầu trả hàng:
```bash
curl -X POST http://localhost:80/api/v1/return-requests \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": 123,
    "userId": 456,
    "returnReason": "Sản phẩm không đúng mô tả",
    "returnType": "RETURN_FULL",
    "details": [
        {
            "productId": 100,
            "productDetailId": 789,
            "price": 150000.0,
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

## Related Documentation

- [Exchange Request APIs](./EXCHANGE_REQUEST_API_README.md)
- [ReturnType Usage Guide](./RETURN_TYPE_USAGE_GUIDE.md)
- [Exchange Functionality Summary](./EXCHANGE_FUNCTIONALITY_SUMMARY.md) 