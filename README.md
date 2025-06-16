# FashionShop - E-commerce Platform

## 📋 Tổng quan dự án

FashionShop là một nền tảng thương mại điện tử hoàn chỉnh được xây dựng bằng Java Spring Boot, chuyên về thời trang và phụ kiện. Hệ thống hỗ trợ quản lý sản phẩm, đơn hàng, thanh toán và người dùng với kiến trúc RESTful API.

### ✨ Tính năng chính

- 🔐 **Hệ thống xác thực và phân quyền** với JWT
- 👤 **Quản lý người dùng** (Customer, Admin, Staff Warehouse)
- 🛍️ **Catalog sản phẩm** với biến thể (màu sắc, kích thước)
- 🛒 **Giỏ hàng và đặt hàng**
- 💳 **Tích hợp thanh toán VNPay**
- 🎫 **Hệ thống voucher và khuyến mãi**
- 📦 **Quản lý kho hàng và nhà cung cấp**
- 📊 **Cache với Redis**
- 📧 **Gửi email tự động**
- 📁 **Upload và quản lý file**

## 🏗️ Kiến trúc hệ thống

### Technology Stack

- **Backend Framework**: Spring Boot 3.4.1
- **Java Version**: 17
- **Database**: MySQL
- **Cache**: Redis
- **Authentication**: JWT
- **Payment**: VNPay Integration
- **Mail**: Spring Mail
- **Documentation**: OpenAPI 3 (Swagger)
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito


#### 👤 User Management
- **User**: Quản lý thông tin người dùng
- **Role**: Phân quyền (ADMIN, CUSTOMER, STAFF_WAREHOUSE)

#### 🛍️ Product Management
- **Product**: Sản phẩm chính
- **ProductVariant**: Biến thể sản phẩm (màu sắc, kích thước, giá)
- **Category**: Danh mục sản phẩm
- **Brand**: Thương hiệu

#### 🛒 Shopping & Orders
- **Cart**: Giỏ hàng
- **Order**: Đơn hàng
- **OrderDetail**: Chi tiết đơn hàng
- **Payment**: Thanh toán

#### 🎫 Promotions
- **Voucher**: Mã giảm giá
- **BundleDiscount**: Khuyến mãi combo

#### 📦 Inventory Management
- **Supplier**: Nhà cung cấp
- **PurchaseOrder**: Đơn hàng nhập
- **StockIn**: Nhập kho

