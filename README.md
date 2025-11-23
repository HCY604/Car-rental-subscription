租車系統 — Car Subscription Backend（汽車訂閱服務後端系統）

一個以 Spring Boot 建構的 RESTful API 後端系統，支援訂閱制租車服務，包括 車輛管理、訂閱管理、會員管理 等功能。

本專案採用標準企業級後端架構：

Controller → Service → Repository → Entity

並以 MySQL + Spring Data JPA 實作資料存取，讓整體系統乾淨、模組化、易維護。

系統簡介（System Overview）
使用者端功能

查詢所有可訂閱車輛

建立訂閱（租車）申請

查詢自己的訂閱資訊

使用者登入（JWT Filter 可擴充）

🔧 後台管理功能

新增 / 更新 / 停用 車輛資料

查詢所有訂閱紀錄

管理訂閱流程

CORS 設定（允許前端跨域存取）

系統架構（Project Structure）
car-subscription-backend/
│
├── config/
│   ├── CorsConfig.java                 # CORS 跨域設定
│   └── JwtFilter.java                  # JWT Token 過濾器（可擴充）
│
├── controller/
│   ├── CarController.java              # 車輛查詢 / CRUD
│   └── SubscriptionController.java     # 訂閱建立 / 查詢
│
├── model/
│   ├── Car.java                        # 車輛資料 Entity
│   ├── Subscription.java               # 訂閱資料 Entity
│   └── User.java                       # 使用者 Entity（登入用）
│
├── repository/
│   ├── CarRepository.java              # Car JPA Repository
│   └── SubscriptionRepository.java
│
├── service/
│   ├── CarService.java
│   ├── SubscriptionService.java
│   └── impl/
│        ├── CarServiceImpl.java
│        └── SubscriptionServiceImpl.java
│
└── CarSubscriptionBackendApplication.java   # Spring Boot 啟動入口

資料庫 ERD
┌──────────────┐         ┌──────────────────┐
│     Car      │ 1     n │   Subscription    │
├──────────────┤         ├──────────────────┤
│ id (PK)      │◄────────│ id (PK)           │
│ brand        │         │ userId            │
│ model        │         │ carId (FK)        │
│ price        │         │ startDate         │
│ status       │         │ endDate           │
└──────────────┘         └──────────────────┘


┌──────────────┐
│     User     │
├──────────────┤
│ id (PK)      │
│ username     │
│ password     │
│ role         │
└──────────────┘

API 規格（RESTful API）
車輛（Car）

取得所有車輛

GET /api/cars


依 ID 查詢車輛

GET /api/cars/{id}


新增車輛（後台）

POST /api/cars


更新車輛

PUT /api/cars/{id}


刪除車輛（軟刪除）

DELETE /api/cars/{id}

訂閱（Subscription）

建立訂閱

POST /api/subscriptions


查詢所有訂閱（後台）

GET /api/subscriptions


依使用者查詢訂閱

GET /api/subscriptions/user/{userId}

使用技術（Tech Stack）
技術說明
Java 17 / 21	系統主要開發語言
Spring Boot 4.x	主體框架
Spring Web	RESTful Controller
Spring Data JPA	ORM / 資料存取
Hibernate	JPA Provider
MySQL 8	後端資料庫
HikariCP	高效連線池
Maven	專案建置管理
JWT（可擴充）	登入 / Token 驗證
CORS Config	前後端跨域設定
