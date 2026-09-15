package com.library.repository;

import com.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // used for login - find a user by their email
    User findByEmail(String email);
}
