CREATE DATABASE IF NOT EXISTS reservams_hotel_db;

USE reservams_hotel_db;

CREATE TABLE hotels (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    address VARCHAR(150) NOT NULL,
    city VARCHAR(80) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE hotel_operators (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hotel_id BIGINT NOT NULL,
    operator_user_id BIGINT NOT NULL,
    assigned_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_hotel_operator_hotel
        FOREIGN KEY (hotel_id)
        REFERENCES hotels(id)
);