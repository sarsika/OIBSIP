package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;

// A message a user sends through the "Contact / Query" form.
// Admin can read all these in the admin panel.
@Entity
@Table(name = "contact_messages")
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1000)
    private String message;

    private LocalDate sentDate;

    public ContactMessage() {
    }

    public ContactMessage(String name, String message, LocalDate sentDate) {
        this.name = name;
        this.message = message;
        this.sentDate = sentDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getSentDate() {
        return sentDate;
    }
}
