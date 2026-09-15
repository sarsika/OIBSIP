package com.library.repository;

import com.library.model.IssueRecord;
import com.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRecordRepository extends JpaRepository<IssueRecord, Long> {

    // all books currently issued (not yet returned) - used in admin panel
    List<IssueRecord> findByReturnedFalse();

    // used for a member's "My Books" page
    List<IssueRecord> findByUserAndReturnedFalse(User user);
}
