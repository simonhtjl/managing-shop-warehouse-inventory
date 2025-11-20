# managing-shop-warehouse-inventory
Managing shop warehouse inventory dikembangkan dengan arsitektur microservices, di mana setiap fungsi bisnis seperti authentication, manajemen produk, proses transaksi, dan pelaporan dibagi menjadi service-service yang independen dan terpisah. Sistem ini menggunakan Spring Boot 3.x dengan Java 17 sebagai foundation teknologi, dimana API Gateway berfungsi sebagai pintu masuk tunggal yang mengatur routing request dan melakukan validasi JWT token untuk keamanan. Setiap microservice seperti Auth Service, Product Service, Sale Service, dan Report Service berjalan secara mandiri dengan port masing-masing dan berkomunikasi melalui REST API.
Untuk penyimpanan data, sistem memanfaatkan H2 In-Memory Database yang memberikan kecepatan tinggi dalam development karena data disimpan di memory. Setiap service memiliki database H2 terpisah yang terisolasi, memastikan data tidak tercampur antara modul yang berbeda. Konfigurasi H2 yang zero-configuration memudahkan setup tanpa perlu instalasi database server terpisah, sementara fitur auto-create table memungkinkan schema database otomatis terbentuk dari entity classes. Autentikasi sistem menggunakan JSON Web Token (JWT) yang bersifat stateless, dimana token yang di-generate setelah login berisi informasi user dan role yang kemudian divalidasi oleh API Gateway sebelum request diteruskan ke service tujuan. Arsitektur ini memungkinkan skalabilitas yang baik, maintenance yang lebih mudah, dan development yang cepat dengan teknologi modern yang robust.
|===================================================================|
# Architecture
  Client
      ↓
  API Gateway (Port 8080)
      ↓
  ┌─────────────────────────────────┐
  │  Auth Service     (Port 8081)   │ - Authentication
  │  Product Service  (Port 8082)   │ - Manajemen Produk  
  │  Sale Service     (Port 8083)   │ - Transaksi Penjualan
  │  Report Service   (Port 8084)   │ - Laporan & Analytics
  └─────────────────────────────────┘
|===================================================================|

# Download dan running di lokal
  cd auth-service
  mvn spring-boot:run
  Auth Service: http://localhost:8081
  
  cd product-service
  mvn spring-boot:run
  Product Service: http://localhost:8082
  
  cd sale-service
  mvn spring-boot:run
  Sale Service: http://localhost:8083
  
  cd report-service
  mvn spring-boot:run
  Report Service: http://localhost:8084
  
  cd api-gateway
  mvn spring-boot:run
  API Gateway: http://localhost:8080
|===================================================================|

# Database H2 (in memory)
  Auth Service
  URL: http://localhost:8081/h2-console
  JDBC URL: jdbc:h2:mem:authdb
  Username: sa
  Password: (kosong)

  Product Service
  URL: http://localhost:8082/h2-console  
  JDBC URL: jdbc:h2:mem:productdb
  Username: sa
  Password: (kosong)

  Sale Service
  URL: http://localhost:8083/h2-console
  JDBC URL: jdbc:h2:mem:saledb  
  Username: sa
  Password: (kosong)

  Report Sevice
  URL: http://localhost:8084/h2-console
  JDBC URL: jdbc:h2:mem:reportdb
  Username: sa  
|===================================================================|

# Login admin default
  Username: admin
  Password: admin
  Role: ADMIN

# Login to get token
  curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'

|===================================================================|

# POSTMAN COLLECTION
{
  "info": {
    "name": "managing-shop-warehouse-inventory",
    "description": "-",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "AUTH ENDPOINTS",
      "item": [
        {
          "name": "Login",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n    \"username\": \"admin\",\n    \"password\": \"admin\"\n}"
            },
            "url": {
              "raw": "http://localhost:8080/auth/login",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["auth", "login"]
            }
          }
        },
        {
          "name": "Validate Token",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "http://localhost:8080/auth/validate",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["auth", "validate"]
            }
          }
        }
      ]
    },
    {
      "name": "PRODUCT ENDPOINTS",
      "item": [
        {
          "name": "Get All Products",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "http://localhost:8080/api/products",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "products"]
            }
          }
        },
        {
          "name": "Get Product by Barcode",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "http://localhost:8080/api/products/barcode/123456789",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "products", "barcode", "123456789"]
            }
          }
        },
        {
          "name": "Search Products",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "http://localhost:8080/api/products/search?keyword=indomie",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "products", "search"],
              "query": [
                {
                  "key": "keyword",
                  "value": "indomie"
                }
              ]
            }
          }
        },
        {
          "name": "Create Product",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              },
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n    \"name\": \"Indomie Goreng\",\n    \"barcode\": \"123456789\",\n    \"price\": 3500,\n    \"stock\": 100,\n    \"category\": \"FOOD\"\n}"
            },
            "url": {
              "raw": "http://localhost:8080/api/products",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "products"]
            }
          }
        },
        {
          "name": "Update Product",
          "request": {
            "method": "PUT",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              },
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n    \"name\": \"Indomie Goreng Special\",\n    \"price\": 4000,\n    \"stock\": 80\n}"
            },
            "url": {
              "raw": "http://localhost:8080/api/products/1",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "products", "1"]
            }
          }
        },
        {
          "name": "Update Stock",
          "request": {
            "method": "PUT",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "http://localhost:8080/api/products/1/stock?quantity=50",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "products", "1", "stock"],
              "query": [
                {
                  "key": "quantity",
                  "value": "50"
                }
              ]
            }
          }
        }
      ]
    },
    {
      "name": "SALES ENDPOINTS",
      "item": [
        {
          "name": "Create Sale",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              },
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n    \"customerName\": \"John Doe\",\n    \"items\": [\n        {\n            \"productId\": 1,\n            \"quantity\": 2,\n            \"unitPrice\": 3500\n        }\n    ],\n    \"paymentMethod\": \"CASH\",\n    \"totalAmount\": 7000\n}"
            },
            "url": {
              "raw": "http://localhost:8080/api/sales",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "sales"]
            }
          }
        },
        {
          "name": "Get All Sales",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "http://localhost:8080/api/sales",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "sales"]
            }
          }
        },
        {
          "name": "Get Sale by ID",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "http://localhost:8080/api/sales/1",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "sales", "1"]
            }
          }
        },
        {
          "name": "Get Today Sales",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "http://localhost:8080/api/sales/today",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "sales", "today"]
            }
          }
        },
        {
          "name": "Get Sales by Date Range",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "http://localhost:8080/api/sales/by-date?startDate=2025-11-01&endDate=2025-11-20",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "sales", "by-date"],
              "query": [
                {
                  "key": "startDate",
                  "value": "2025-11-01"
                },
                {
                  "key": "endDate",
                  "value": "2025-11-20"
                }
              ]
            }
          }
        }
      ]
    },
    {
      "name": "REPORT ENDPOINTS",
      "item": [
        {
          "name": "Get Daily Report (Today)",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "http://localhost:8080/api/reports/daily",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "reports", "daily"]
            }
          }
        },
        {
          "name": "Get Daily Report by Date",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "http://localhost:8080/api/reports/daily/2025-11-20",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "reports", "daily", "2025-11-20"]
            }
          }
        },
        {
          "name": "Get Weekly Report",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "http://localhost:8080/api/reports/weekly",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "reports", "weekly"]
            }
          }
        },
        {
          "name": "Get Top Products",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "http://localhost:8080/api/reports/top-products?limit=10",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "reports", "top-products"],
              "query": [
                {
                  "key": "limit",
                  "value": "10"
                }
              ]
            }
          }
        }
      ]
    }
  ],
  "variable": [
    {
      "key": "token",
      "value": "your-jwt-token-here",
      "type": "string"
    }
  ],
  "event": [
    {
      "listen": "prerequest",
      "script": {
        "type": "text/javascript",
        "exec": [
          "console.log('Managing-shop-warehouse API Collection');"
        ]
      }
    },
    {
      "listen": "test",
      "script": {
        "type": "text/javascript",
        "exec": [
          "pm.test(\"Status code is 200\", function () {",
          "    pm.response.to.have.status(200);",
          "});"
        ]
      }
    }
  ]
}
  
