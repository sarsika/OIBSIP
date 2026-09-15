package com.library.controller;

import com.library.model.*;
import com.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

// Everything a normal member can do: browse, search, issue, return, book, contact.
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IssueRecordRepository issueRecordRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    private static final int LOAN_DAYS = 14;      // how many days a book can be kept
    private static final double FINE_PER_DAY = 5; // rupees per day late

    // ---------- BROWSE / SEARCH CATALOGUE ----------

    @GetMapping("/books")
    public List<Book> getAllBooks(@RequestParam(required = false) String search) {
        if (search == null || search.isBlank()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(search, search);
    }

    // ---------- ISSUE A BOOK ----------

    @PostMapping("/books/{bookId}/issue/{userId}")
    public Object issueBook(@PathVariable Long bookId, @PathVariable Long userId) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        if (book.getAvailableQuantity() <= 0) {
            return "No copies available right now. Try advance booking instead.";
        }

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);

        LocalDate today = LocalDate.now();
        IssueRecord record = new IssueRecord(book, user, today, today.plusDays(LOAN_DAYS));
        return issueRecordRepository.save(record);
    }

    // ---------- RETURN A BOOK ----------

    @PostMapping("/issues/{issueId}/return")
    public Object returnBook(@PathVariable Long issueId) {
        IssueRecord record = issueRecordRepository.findById(issueId).orElseThrow();

        LocalDate today = LocalDate.now();
        record.setReturnDate(today);
        record.setReturned(true);

        // calculate fine only if returned late
        long lateDays = ChronoUnit.DAYS.between(record.getDueDate(), today);
        if (lateDays > 0) {
            record.setFine(lateDays * FINE_PER_DAY);
        }

        // give the copy back to the shelf
        Book book = record.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepository.save(book);

        return issueRecordRepository.save(record);
    }

    // ---------- MY ISSUED BOOKS ----------

    @GetMapping("/users/{userId}/my-books")
    public List<IssueRecord> myBooks(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return issueRecordRepository.findByUserAndReturnedFalse(user);
    }

    // ---------- ADVANCE BOOKING (for a book that's currently issued to someone else) ----------

    @PostMapping("/books/{bookId}/book/{userId}")
    public Booking advanceBooking(@PathVariable Long bookId, @PathVariable Long userId) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        Booking booking = new Booking(book, user, LocalDate.now());
        return bookingRepository.save(booking);
    }

    @GetMapping("/users/{userId}/bookings")
    public List<Booking> myBookings(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return bookingRepository.findByUser(user);
    }

    // ---------- CONTACT / QUERY FORM ----------

    @PostMapping("/contact")
    public ContactMessage sendMessage(@RequestBody ContactMessage message) {
        return contactMessageRepository.save(
                new ContactMessage(message.getName(), message.getMessage(), LocalDate.now())
        );
    }
}
