package com.kozitskiy.dorm4.sys.repositories;

import com.kozitskiy.dorm4.sys.dto.UserResponseDto;
import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.entities.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<UserResponseDto> findByUsername(String username);
    List<User> findByUserType(UserType userType);
    UserResponseDto getUserById(Long id);
}
