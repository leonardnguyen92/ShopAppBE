CREATE DATABASE shopapp
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE shopapp;

/* =========================
   ROLES
========================= */
CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) UNIQUE NOT NULL,
    description VARCHAR(100)
) COMMENT = 'BẢNG VAI TRÒ TÀI KHOẢN';


/* =========================
   USERS
========================= */
CREATE TABLE users (
   id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(30) NOT NULL UNIQUE,
    address VARCHAR(255) DEFAULT '',
    password VARCHAR(100) NOT NULL,
    date_of_birth DATE,

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    -- ACTIVE, INACTIVE, BANNED

    role_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_users_role
        FOREIGN KEY (role_id) REFERENCES roles(id)
) COMMENT = 'BẢNG THÔNG TIN TÀI KHOẢN';


/* =========================
   TOKENS
========================= */
CREATE TABLE tokens (
    id INT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(50) NOT NULL,
    expiration_date DATETIME,
    revoked BOOLEAN DEFAULT FALSE,
    expired BOOLEAN DEFAULT FALSE,
    user_id INT NOT NULL,

    CONSTRAINT fk_tokens_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,

    INDEX idx_tokens_user (user_id)
) COMMENT = 'BẢNG TOKEN XÁC THỰC';


/* =========================
   SOCIAL ACCOUNTS
========================= */
CREATE TABLE social_accounts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    provider VARCHAR(25) NOT NULL,
    provider_id VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    name VARCHAR(100) NOT NULL,
    user_id INT NOT NULL,

    CONSTRAINT fk_social_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,

    UNIQUE KEY uq_provider (provider, provider_id)
) COMMENT = 'BẢNG TÀI KHOẢN MẠNG XÃ HỘI';


/* =========================
   CATEGORIES
========================= */
CREATE TABLE categories (
     id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
    -- ACTIVE, INACTIVE
) COMMENT = 'BẢNG DANH MỤC SẢN PHẨM';


/* =========================
   PRODUCTS
========================= */
CREATE TABLE products (
     id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(350) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    thumbnail VARCHAR(300),
    description LONGTEXT,

    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    -- ACTIVE, INACTIVE, DELETED_BY_ADMIN, DELETED_BY_CATEGORY, OUT_OF_STOCK

    category_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_products_category
        FOREIGN KEY (category_id) REFERENCES categories(id)
        ON DELETE RESTRICT,

    INDEX idx_products_category (category_id)
) COMMENT = 'BẢNG CHI TIẾT SẢN PHẨM';


/* =========================
   PRODUCT IMAGES
========================= */
CREATE TABLE product_images (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    image_url VARCHAR(300) NOT NULL,

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    -- ACTIVE, DELETED

    CONSTRAINT fk_product_images_product
        FOREIGN KEY (product_id) REFERENCES products(id)
        ON DELETE CASCADE,

    INDEX idx_product_images_product (product_id)
) COMMENT = 'BẢNG ẢNH SẢN PHẨM';


/* =========================
   ORDERS
========================= */
CREATE TABLE orders (
  id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,

    fullname VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone_number VARCHAR(30) NOT NULL,
    address VARCHAR(255) NOT NULL,
    note VARCHAR(255),

    order_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    -- PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED

    shipping_method VARCHAR(100),
    shipping_address VARCHAR(255),
    shipping_date DATE,
    tracking_number VARCHAR(100),

    payment_method VARCHAR(100),
    payment_date DATETIME,

    total_money DECIMAL(10,2) NOT NULL CHECK (total_money >= 0),
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id) REFERENCES users(id),

    INDEX idx_orders_user (user_id)
) COMMENT = 'BẢNG ĐƠN HÀNG';


/* =========================
   ORDER DETAILS
========================= */
CREATE TABLE order_details (
   id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,

    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    quantity INT NOT NULL CHECK (quantity > 0),
    total_money DECIMAL(10,2) NOT NULL CHECK (total_money >= 0),
    color VARCHAR(25) DEFAULT '',

    CONSTRAINT fk_order_details_order
        FOREIGN KEY (order_id) REFERENCES orders(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_order_details_product
        FOREIGN KEY (product_id) REFERENCES products(id)
        ON DELETE RESTRICT,

    UNIQUE KEY uq_order_product (order_id, product_id)
) COMMENT = 'BẢNG CHI TIẾT ĐƠN HÀNG';
