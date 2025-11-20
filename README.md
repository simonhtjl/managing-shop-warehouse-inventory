# managing-shop-warehouse-inventory
Managing shop warehouse inventory dikembangkan dengan arsitektur microservices, di mana setiap fungsi bisnis seperti authentication, manajemen produk, proses transaksi, dan pelaporan dibagi menjadi service-service yang independen dan terpisah. Sistem ini menggunakan Spring Boot 3.x dengan Java 17 sebagai foundation teknologi, dimana API Gateway berfungsi sebagai pintu masuk tunggal yang mengatur routing request dan melakukan validasi JWT token untuk keamanan. Setiap microservice seperti Auth Service, Product Service, Sale Service, dan Report Service berjalan secara mandiri dengan port masing-masing dan berkomunikasi melalui REST API.
Untuk penyimpanan data, sistem memanfaatkan H2 In-Memory Database yang memberikan kecepatan tinggi dalam development karena data disimpan di memory. Setiap service memiliki database H2 terpisah yang terisolasi, memastikan data tidak tercampur antara modul yang berbeda. Konfigurasi H2 yang zero-configuration memudahkan setup tanpa perlu instalasi database server terpisah, sementara fitur auto-create table memungkinkan schema database otomatis terbentuk dari entity classes. Autentikasi sistem menggunakan JSON Web Token (JWT) yang bersifat stateless, dimana token yang di-generate setelah login berisi informasi user dan role yang kemudian divalidasi oleh API Gateway sebelum request diteruskan ke service tujuan. Arsitektur ini memungkinkan skalabilitas yang baik, maintenance yang lebih mudah, dan development yang cepat dengan teknologi modern yang robust.
|===================================================================|
# architecture
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

# setup and running
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

#login admin default
  Username: admin
  Password: admin
  Role: ADMIN
|===================================================================|

#login to get token
  curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'

#  get all product
  curl -X GET "http://localhost:8080/api/products" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

#find prodcut by name
  curl -X GET "http://localhost:8080/api/products" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

#find by scan barcode
  curl -X GET "http://localhost:8080/api/products/barcode/8999999100011" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

#make transaction
  curl -X POST http://localhost:8080/api/sales \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer YOUR_TOKEN_HERE" \
    -d '{
      "cashierName": "admin",
      "cashReceived": 50000,
      "items": [
      {
          "productId": 1,
          "quantity": 2
        },
        {
          "productId": 2, 
          "quantity": 1
        }
      ]
    }'

#daily report
  curl -X GET "http://localhost:8080/api/reports/daily" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

#best seller
curl -X GET "http://localhost:8080/api/reports/top-products?limit=5" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
|===================================================================|

#Database H2 (in memory)
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
  Password: (kosong)
|===================================================================|

#End point list
  POST /auth/login - Login user
  GET /api/products - Ambil semua produk
  GET /api/products/{id} - Ambil produk by ID
  GET /api/products/barcode/{barcode} - Scan barcode
  GET /api/products/search?keyword={} - Cari produk
  POST /api/products - Tambah produk baru
  PUT /api/products/{id} - Update produk
  PUT /api/products/{id}/stock - Update stok
  DELETE /api/products/{id} - Hapus produk
  POST /api/sales - Buat transaksi penjualan
  GET /api/sales - Lihat semua transaksi
  GET /api/sales/{id} - Lihat detail transaksi
  GET /api/sales/today - Transaksi hari ini
  GET /api/sales/by-date - Transaksi by range tanggal
  GET /api/reports/daily - Laporan harian
  GET /api/reports/weekly - Laporan mingguan
  GET /api/reports/top-products - Produk terlaris
|===================================================================|
  
