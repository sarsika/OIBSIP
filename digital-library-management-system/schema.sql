-- Run this once: CREATE DATABASE library_db;
-- Spring Boot (hibernate.ddl-auto=update) will auto-create these tables for you
-- when you run the app. This file is just here so anyone reading the project
-- can see the table structure at a glance.

CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    author VARCHAR(255),
    isbn VARCHAR(50),
    category VARCHAR(100),
    quantity INT,
    available_quantity INT
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(20)
);

CREATE TABLE issue_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT,
    user_id BIGINT,
    issue_date DATE,
    due_date DATE,
    return_date DATE,
    fine DOUBLE,
    returned BOOLEAN,
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT,
    user_id BIGINT,
    booking_date DATE,
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE contact_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    message VARCHAR(1000),
    sent_date DATE
);

-- create one admin account manually so you can log in as admin
INSERT INTO users (name, email, password, role)
VALUES ('Admin', 'admin@library.com', 'admin123', 'ADMIN');
