package com.library.repository;

import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

// Spring auto-generates all the basic save/find/delete methods for us.
// We just add extra search methods here when we need them.
public interface BookRepository extends JpaRepository<Book, Long> {

    // used for the search box - finds books where title OR author contains the text
    java.util.List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
}
