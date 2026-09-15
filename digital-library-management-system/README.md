# Digital Library Management System

A web-based library system with two roles — **Admin** and **User** — built with
Java Spring Boot, MySQL, and plain HTML/CSS/JS.

## Features

**Admin**
- Login with full access
- Add / edit / delete books
- View all issued books and due dates
- View and manage registered members
- Mark fines as paid
- View contact messages from users

**User**
- Register and login
- Browse and search the book catalogue
- Issue a book (14-day loan period)
- Return a book (fine auto-calculated at ₹5/day if late)
- Advance booking for books that are currently unavailable
- Contact form to reach the admin

## Tech Stack

- Java 17 + Spring Boot 3
- Spring Data JPA (Hibernate)
- MySQL
- HTML, CSS, JavaScript (vanilla, no framework — kept simple)

## Project Structure

```
digital-library-management-system/
├── pom.xml
├── schema.sql
├── src/main/java/com/library/
│   ├── LibraryApplication.java
│   ├── model/          → Book, User, IssueRecord, Booking, ContactMessage
│   ├── repository/     → Spring Data JPA interfaces
│   └── controller/     → AuthController, AdminController, UserController
└── src/main/resources/
    ├── application.properties
    └── static/          → index.html, style.css, script.js (frontend)
```

## How to Run

1. Install MySQL and create the database:
   ```sql
   CREATE DATABASE library_db;
   ```

2. Open `src/main/resources/application.properties` and set your MySQL
   username/password.

3. (Optional) Run `schema.sql` to create tables and one admin login manually,
   or just let Spring Boot auto-create them on first run and insert an admin
   row yourself.

4. Run the app:
   ```bash
   mvn spring-boot:run
   ```

5. Open your browser at `http://localhost:8080`

**Default admin login** (if you ran schema.sql):
- Email: `admin@library.com`
- Password: `admin123`

## Notes

- Fine rule: ₹5 per day late, calculated when the book is returned.
- Loan period: 14 days from the issue date.
- Passwords are stored as plain text for simplicity — in a real production
  app these should be hashed (e.g. with BCrypt) and auth should use
  Spring Security / JWT.
