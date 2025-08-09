package com.kozitskiy.dorm4.sys.repositories;

import com.kozitskiy.dorm4.sys.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
