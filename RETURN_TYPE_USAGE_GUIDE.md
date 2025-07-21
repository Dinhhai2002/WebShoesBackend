# ReturnType Usage Guide

## Tổng quan
Enum `ReturnType` đã được cập nhật để hỗ trợ đầy đủ các loại trả hàng và đổi hàng chi tiết.

## Các loại ReturnType mới

### 1. Hoàn trả hàng (Return Types)

#### `RETURN_FULL` - Hoàn trả 100% đơn hàng
- **Mô tả**: Khách hàng trả lại toàn bộ đơn hàng và nhận hoàn tiền 100%
- **Sử dụng khi**: Khách hàng không hài lòng với toàn bộ đơn hàng
- **Xử lý tồn kho**: Cộng lại tồn kho cho tất cả sản phẩm trong đơn hàng

#### `RETURN_PARTIAL` - Hoàn trả một phần đơn hàng
- **Mô tả**: Khách hàng trả lại một số sản phẩm trong đơn hàng
- **Sử dụng khi**: Khách hàng chỉ không hài lòng với một số sản phẩm
- **Xử lý tồn kho**: Chỉ cộng lại tồn kho cho các sản phẩm được trả

### 2. Đổi hàng (Exchange Types)

#### `EXCHANGE_FULL` - Đổi 100% đơn hàng
- **Mô tả**: Khách hàng đổi toàn bộ đơn hàng lấy sản phẩm khác
- **Sử dụng khi**: Khách hàng muốn đổi toàn bộ đơn hàng
- **Xử lý**: Tạo ExchangeRequest để admin chọn sản phẩm mới

#### `EXCHANGE_PARTIAL` - Đổi một phần đơn hàng
- **Mô tả**: Khách hàng đổi một số sản phẩm trong đơn hàng
- **Sử dụng khi**: Khách hàng chỉ muốn đổi một số sản phẩm
- **Xử lý**: Tạo ExchangeRequest cho các sản phẩm cần đổi

### 3. Legacy Types (Deprecated)
- `REFUND` - Hoàn tiền (tương đương RETURN_FULL)
- `EXCHANGE` - Đổi hàng (tương đương EXCHANGE_FULL)
- `PARTIAL_REFUND` - Hoàn tiền một phần (tương đương RETURN_PARTIAL)

## Helper Methods

### `isReturnType()`
```java
// Kiểm tra có phải loại hoàn trả hàng không
if (returnType.isReturnType()) {
    // Xử lý logic hoàn trả hàng
}
```

### `isExchangeType()`
```java
// Kiểm tra có phải loại đổi hàng không
if (returnType.isExchangeType()) {
    // Xử lý logic đổi hàng
}
```

### `isFullType()`
```java
// Kiểm tra có phải hoàn trả/đổi 100% không
if (returnType.isFullType()) {
    // Xử lý logic cho toàn bộ đơn hàng
}
```

### `isPartialType()`
```java
// Kiểm tra có phải hoàn trả/đổi một phần không
if (returnType.isPartialType()) {
    // Xử lý logic cho một phần đơn hàng
}
```

## Ví dụ sử dụng

### 1. Tạo Return Request với RETURN_FULL
```json
{
    "orderId": 123,
    "userId": 456,
    "returnReason": "Không hài lòng với toàn bộ đơn hàng",
    "returnType": "RETURN_FULL",
    "details": [
        {
            "productDetailId": 789,
            "quantity": 2,
            "returnReason": "Chất lượng không như mong đợi",
            "conditionDescription": "Sản phẩm còn nguyên vẹn",
            "images": ["image1.jpg", "image2.jpg"]
        },
        {
            "productDetailId": 790,
            "quantity": 1,
            "returnReason": "Size không vừa",
            "conditionDescription": "Sản phẩm còn nguyên vẹn",
            "images": ["image3.jpg"]
        }
    ]
}
```

### 2. Tạo Return Request với RETURN_PARTIAL
```json
{
    "orderId": 123,
    "userId": 456,
    "returnReason": "Chỉ không hài lòng với một sản phẩm",
    "returnType": "RETURN_PARTIAL",
    "details": [
        {
            "productDetailId": 789,
            "quantity": 1,
            "returnReason": "Size không vừa",
            "conditionDescription": "Sản phẩm còn nguyên vẹn",
            "images": ["image1.jpg"]
        }
    ]
}
```

### 3. Tạo Return Request với EXCHANGE_FULL
```json
{
    "orderId": 123,
    "userId": 456,
    "returnReason": "Muốn đổi toàn bộ đơn hàng",
    "returnType": "EXCHANGE_FULL",
    "details": [
        {
            "productDetailId": 789,
            "quantity": 2,
            "returnReason": "Muốn đổi sang sản phẩm khác",
            "conditionDescription": "Sản phẩm còn nguyên vẹn",
            "images": ["image1.jpg", "image2.jpg"]
        }
    ]
}
```

### 4. Tạo Return Request với EXCHANGE_PARTIAL
```json
{
    "orderId": 123,
    "userId": 456,
    "returnReason": "Chỉ muốn đổi một sản phẩm",
    "returnType": "EXCHANGE_PARTIAL",
    "details": [
        {
            "productDetailId": 789,
            "quantity": 1,
            "returnReason": "Muốn đổi sang size khác",
            "conditionDescription": "Sản phẩm còn nguyên vẹn",
            "images": ["image1.jpg"]
        }
    ]
}
```

## Business Logic Updates

### ReturnRequestServiceImpl
- Sử dụng `isReturnType()` để xác định loại hoàn trả hàng
- Xử lý tồn kho cho tất cả loại hoàn trả hàng

### ExchangeRequestServiceImpl
- Sử dụng `isExchangeType()` để xác định loại đổi hàng
- Chỉ cho phép tạo ExchangeRequest cho các loại đổi hàng

## Migration từ Legacy Types

### Từ REFUND sang RETURN_FULL
```java
// Cũ
returnType = ReturnType.REFUND;

// Mới
returnType = ReturnType.RETURN_FULL;
```

### Từ EXCHANGE sang EXCHANGE_FULL
```java
// Cũ
returnType = ReturnType.EXCHANGE;

// Mới
returnType = ReturnType.EXCHANGE_FULL;
```

### Từ PARTIAL_REFUND sang RETURN_PARTIAL
```java
// Cũ
returnType = ReturnType.PARTIAL_REFUND;

// Mới
returnType = ReturnType.RETURN_PARTIAL;
```

## Lưu ý quan trọng

1. **Backward Compatibility**: Các legacy types vẫn được hỗ trợ để đảm bảo tương thích ngược
2. **Validation**: Cần validate số lượng sản phẩm trong details phù hợp với loại return
3. **Business Logic**: Logic xử lý tồn kho và exchange request đã được cập nhật
4. **Documentation**: Cập nhật API documentation để phản ánh các loại mới 