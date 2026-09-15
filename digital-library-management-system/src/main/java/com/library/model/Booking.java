package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;

// Used when a user wants a book that is already issued to someone else.
// They "reserve" it and wait for it to come back.
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate bookingDate;

    public Booking() {
    }

    public Booking(Book book, User user, LocalDate bookingDate) {
        this.book = book;
        this.user = user;
        this.bookingDate = bookingDate;
    }

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }
}
