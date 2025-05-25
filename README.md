# Brokerage App - Java Spring Boot Backend

This project is a stock order management backend for a brokerage firm.

## Technologies
- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Security (Basic Auth)
- H2 in-memory DB
- Swagger (OpenAPI)

## Access

Swagger UI: http://localhost:8080/swagger-ui.html

H2 Console: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:brokeragedb

Username: sa, Password: (empty)

## Authentication
Use Basic Auth headers:

Username: admin

Password: admin123

## Endpoints
POST /api/orders – Create order

GET /api/orders?customerId=1&from=...&to=... – List orders

## How to Run

```bash
./mvnw spring-boot:run

