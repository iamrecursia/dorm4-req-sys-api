package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    public User createUser(User user);
    public User findUserByUsername(String username);
    public List<User> findAllUsers();

}
