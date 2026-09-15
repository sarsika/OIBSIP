package com.library.controller;

import com.library.model.Book;
import com.library.model.IssueRecord;
import com.library.model.User;
import com.library.repository.BookRepository;
import com.library.repository.IssueRecordRepository;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Everything only an admin should be able to do.
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private IssueRecordRepository issueRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.library.repository.ContactMessageRepository contactMessageRepository;

    // ---------- BOOK MANAGEMENT ----------

    @PostMapping("/books")
    public Book addBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PutMapping("/books/{id}")
    public Book editBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        Book book = bookRepository.findById(id).orElseThrow();
        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setIsbn(updatedBook.getIsbn());
        book.setCategory(updatedBook.getCategory());
        book.setQuantity(updatedBook.getQuantity());
        return bookRepository.save(book);
    }

    @DeleteMapping("/books/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "Book deleted";
    }

    // ---------- VIEW ISSUED BOOKS ----------

    @GetMapping("/issued-books")
    public List<IssueRecord> viewIssuedBooks() {
        return issueRecordRepository.findByReturnedFalse();
    }

    // ---------- VIEW MEMBERS ----------

    @GetMapping("/members")
    public List<User> viewMembers() {
        return userRepository.findAll();
    }

    // ---------- FINE MANAGEMENT ----------

    @PutMapping("/fines/{issueId}/mark-paid")
    public String markFinePaid(@PathVariable Long issueId) {
        IssueRecord record = issueRecordRepository.findById(issueId).orElseThrow();
        record.setFine(0);
        issueRecordRepository.save(record);
        return "Fine marked as paid";
    }

    // ---------- VIEW CONTACT MESSAGES FROM USERS ----------

    @GetMapping("/messages")
    public List<com.library.model.ContactMessage> viewMessages() {
        return contactMessageRepository.findAll();
    }
}
