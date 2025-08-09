package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public User createUser(User user) {
       User createdUser = User.builder()
               .username(user.getUsername())
               .password(user.getPassword())
               .build();

        return userRepository.save(createdUser);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
