# BTH5 - Reporting Service

Reporting Service is an independent Spring Boot service on port `8083` with its own MySQL database (`reporting_db`). It stores order reports in `orders_reports` and per-product/per-order reports in `product_reports`.

## Run

Requirements: Java 21 and MySQL. Create `reporting_db`, then run `mvn spring-boot:run` in this folder. Configure `REPORTING_DB_URL`, `REPORTING_DB_USERNAME`, `REPORTING_DB_PASSWORD`, `AUTH_SERVICE_URL`, `PRODUCT_SERVICE_URL`, and `ORDER_SERVICE_URL` with environment variables when needed. Defaults expect BTH2, BTH3, and BTH4 at ports 8080, 8081, and 8082.

Every API requires the `Authorization` header used by BTH2. Requests are checked through `POST {AUTH_SERVICE_URL}/validate`, then upstream calls forward the same header.

## APIs

| Method | Path | Body | Result |
|---|---|---|---|
| GET | `/reports/products` | — | Product reports, including current inventory fetched from BTH3 |
| GET | `/reports/products/{id}` | — | Product report by report ID |
| POST | `/reports/products` | `{"orderId": 1, "productId": 1}` | Creates or refreshes the report for this product in this order |
| DELETE | `/reports/products/{id}` | — | Deletes a product report |
| GET | `/reports/orders` | — | All order reports |
| GET | `/reports/orders/{id}` | — | Order report by report ID |
| POST | `/reports/orders` | `{"orderId": 1}` | Creates or refreshes an order report |
| DELETE | `/reports/orders/{id}` | — | Deletes the order report and its product reports |

Report creation retrieves the order and its items from BTH4, then product details from BTH3. Revenue is based on BTH4 order totals. Cost is calculated from a product cost field (`cost`, `costPrice`, `cost_price`, `purchasePrice`, or `purchase_price`) if BTH3 returns one. The current BTH3 schema exposes only the selling `price`, so absent a purchase-cost field the report records cost as `0.00`; add purchase cost to BTH3 before treating profit as a real margin. Inventory is live data returned by BTH3 and is not stored in the assignment's report table.

## Example

```http
POST http://localhost:8083/reports/orders
Authorization: <BTH2 token>
Content-Type: application/json

{"orderId": 1}
```
