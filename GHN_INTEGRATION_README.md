# GHN API Integration Guide

## Tổng quan
Dự án đã được tích hợp 5 API của GHN:
1. **Available Services API** - Lấy danh sách dịch vụ vận chuyển có sẵn
2. **Calculate Fee API** - Tính phí vận chuyển
3. **Provinces API** - Lấy danh sách tỉnh/thành phố
4. **Districts API** - Lấy danh sách quận/huyện
5. **Wards API** - Lấy danh sách phường/xã

## Cấu hình

### 1. Cập nhật application.properties
Thêm thông tin GHN vào file `src/main/resources/application.properties`:

```properties
# GHN API Configuration
ghn.api.token=YOUR_GHN_TOKEN_HERE
ghn.api.shop-id=YOUR_SHOP_ID_HERE
```

**Lưu ý:** 
- Thay `YOUR_GHN_TOKEN_HERE` bằng token thực từ GHN
- Thay `YOUR_SHOP_ID_HERE` bằng shop ID thực từ GHN

## API Endpoints

### 1. Lấy danh sách dịch vụ vận chuyển có sẵn

**Endpoint:** `POST /api/v1/authentication/ghn/available-services`

**Request Body:**
```json
{
    "shop_id": 123456,
    "from_district": 1442,
    "to_district": 1443
}
```

**Response:**
```json
{
    "code": 200,
    "message": "Success",
    "data": [
        {
            "service_id": 53320,
            "short_name": "Giao hàng nhanh",
            "service_type_id": 1
        }
    ]
}
```

### 2. Tính phí vận chuyển

**Endpoint:** `POST /api/v1/authentication/ghn/calculate-fee`

**Request Body:**
```json
{
    "service_id": 53320,
    "insurance_value": 100000,
    "from_district_id": 1442,
    "to_district_id": 1443,
    "to_ward_code": 20109,
    "height": 10,
    "length": 20,
    "weight": 500,
    "width": 15
}
```

**Response:**
```json
{
    "code": 200,
    "message": "Success",
    "data": {
        "total": 30000,
        "service_fee": 25000,
        "insurance_fee": 5000,
        "pick_station_fee": 0,
        "coupon_value": 0,
        "r2s_fee": 0,
        "document_return": 0,
        "double_check": 0,
        "cod_fee": 0,
        "pick_remote_areas_fee": 0,
        "deliver_remote_areas_fee": 0,
        "cod_failed_fee": 0
    }
}
```

### 3. Lấy danh sách tỉnh/thành phố

**Endpoint:** `GET /api/v1/authentication/ghn/provinces`

**Response:**
```json
{
    "code": 200,
    "message": "Success",
    "data": [
        {
            "provinceId": 201,
            "provinceName": "Hà Nội",
            "code": "4"
        },
        {
            "provinceId": 202,
            "provinceName": "Hồ Chí Minh",
            "code": "8"
        }
    ]
}
```

### 4. Lấy danh sách quận/huyện

**Endpoint:** `GET /api/v1/authentication/ghn/districts?province_id=202`

**Response:**
```json
{
    "code": 200,
    "message": "Success",
    "data": [
        {
            "districtId": 1442,
            "provinceId": 202,
            "districtName": "Quận 1",
            "code": "0201",
            "type": 1,
            "supportType": 0
        },
        {
            "districtId": 1443,
            "provinceId": 202,
            "districtName": "Quận 2",
            "code": "0202",
            "type": 1,
            "supportType": 0
        }
    ]
}
```

### 5. Lấy danh sách phường/xã

**Endpoint:** `GET /api/v1/authentication/ghn/wards?district_id=1566`

**Response:**
```json
{
    "code": 200,
    "message": "Success",
    "data": [
        {
            "wardCode": 510101,
            "districtId": 1566,
            "wardName": "Phường Mỹ Bình"
        },
        {
            "wardCode": 510102,
            "districtId": 1566,
            "wardName": "Phường Mỹ Long"
        }
    ]
}
```

## Cấu trúc Code

### Request Models
- `GHNServiceRequest.java` - Request cho API lấy dịch vụ
- `GHNFeeRequest.java` - Request cho API tính phí

### Response Models
- `GHNServiceResponse.java` - Response cho từng dịch vụ
- `GHNServiceListResponse.java` - Wrapper response cho danh sách dịch vụ
- `GHNFeeResponse.java` - Response cho phí vận chuyển
- `GHNFeeDetailResponse.java` - Wrapper response cho phí vận chuyển
- `GHNProvinceResponse.java` - Response cho tỉnh/thành phố
- `GHNProvinceListResponse.java` - Wrapper response cho danh sách tỉnh/thành phố
- `GHNDistrictResponse.java` - Response cho quận/huyện
- `GHNDistrictListResponse.java` - Wrapper response cho danh sách quận/huyện
- `GHNWardResponse.java` - Response cho phường/xã
- `GHNWardListResponse.java` - Wrapper response cho danh sách phường/xã

### Service Layer
- `GHNService.java` - Interface định nghĩa các method
- `GHNServiceImpl.java` - Implementation gọi API GHN

### Controller Layer
- `JwtAuthenticationController.java` - Expose REST endpoints (bao gồm cả GHN APIs)

## Sử dụng trong Frontend

### 1. Lấy danh sách dịch vụ
```javascript
const getAvailableServices = async (fromDistrict, toDistrict) => {
    const response = await fetch('/api/v1/authentication/ghn/available-services', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            from_district: fromDistrict,
            to_district: toDistrict
        })
    });
    return await response.json();
};
```

### 2. Tính phí vận chuyển
```javascript
const calculateShippingFee = async (serviceId, packageInfo) => {
    const response = await fetch('/api/v1/authentication/ghn/calculate-fee', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            service_id: serviceId,
            insurance_value: packageInfo.insuranceValue,
            from_district_id: packageInfo.fromDistrictId,
            to_district_id: packageInfo.toDistrictId,
            to_ward_code: packageInfo.toWardCode,
            height: packageInfo.height,
            length: packageInfo.length,
            weight: packageInfo.weight,
            width: packageInfo.width
        })
    });
    return await response.json();
};
```

### 3. Lấy danh sách tỉnh/thành phố
```javascript
const getProvinces = async () => {
    const response = await fetch('/api/v1/authentication/ghn/provinces');
    return await response.json();
};
```

### 4. Lấy danh sách quận/huyện
```javascript
const getDistricts = async (provinceId) => {
    const response = await fetch(`/api/v1/authentication/ghn/districts?province_id=${provinceId}`);
    return await response.json();
};
```

### 5. Lấy danh sách phường/xã
```javascript
const getWards = async (districtId) => {
    const response = await fetch(`/api/v1/authentication/ghn/wards?district_id=${districtId}`);
    return await response.json();
};
```

## Lưu ý quan trọng

1. **Token GHN**: Cần có token hợp lệ từ GHN để sử dụng API
2. **Shop ID**: Cần có shop ID hợp lệ từ GHN
3. **District/Ward Codes**: Sử dụng mã quận/huyện và phường/xã theo chuẩn của GHN
4. **Error Handling**: API đã có xử lý lỗi cơ bản, trả về HTTP 500 khi có lỗi
5. **CORS**: Controller đã được cấu hình CORS để cho phép frontend gọi API
6. **Master Data APIs**: Các API tỉnh/quận/phường sử dụng GET method và chỉ cần token

## Testing

Để test API, bạn có thể sử dụng Postman hoặc curl:

```bash
# Test available services
curl -X POST http://localhost:80/api/v1/authentication/ghn/available-services \
  -H "Content-Type: application/json" \
  -d '{"from_district": 1442, "to_district": 1443}'

# Test calculate fee
curl -X POST http://localhost:80/api/v1/authentication/ghn/calculate-fee \
  -H "Content-Type: application/json" \
  -d '{"service_id": 53320, "insurance_value": 100000, "from_district_id": 1442, "to_district_id": 1443, "to_ward_code": 20109, "height": 10, "length": 20, "weight": 500, "width": 15}'

# Test provinces
curl -X GET http://localhost:80/api/v1/authentication/ghn/provinces

# Test districts
curl -X GET "http://localhost:80/api/v1/authentication/ghn/districts?province_id=202"

# Test wards
curl -X GET "http://localhost:80/api/v1/authentication/ghn/wards?district_id=1566"
``` 