# BTH4 - Order Management Service

Spring Boot 4.1.1 / Java 21 / MySQL. Service độc lập tại cổng 8082.

## Cài đặt
1. Tạo database: `CREATE DATABASE order_db;`
2. Sửa tài khoản/mật khẩu MySQL trong `src/main/resources/application.properties`.
3. Chạy Authentication Service BTH2 tại 8080 và Product Service BTH3 tại 8081.
4. Chạy `mvn spring-boot:run`.

## Tích hợp
- Xác thực: POST `http://localhost:8080/validate`, header Authorization: Bearer <token>.
- Sản phẩm: GET `http://localhost:8081/products/{id}`, chuyển tiếp Authorization header.
- Tồn kho kiểm tra trước khi tạo/cập nhật chi tiết đơn hàng. Không tự trừ tồn kho vì API BTH3 hiện tại chưa có endpoint cập nhật tồn kho chuyên biệt.

## API
- GET/POST `/orders`
- GET/PUT/DELETE `/orders/{id}`
- GET/POST `/order_items`
- GET/PUT/DELETE `/order_items/{id}`

POST /orders body:
```json
{"customerName":"Nguyen Van A","customerEmail":"a@example.com","items":[{"productId":1,"quantity":2}]}
```
PUT /orders/{id} body: `{"status":"completed"}`
POST /order_items body: `{"orderId":1,"productId":1,"quantity":2}`
PUT /order_items/{id} body: `{"productId":1,"quantity":3}`
