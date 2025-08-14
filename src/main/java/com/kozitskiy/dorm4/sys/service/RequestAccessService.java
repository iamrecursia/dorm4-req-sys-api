package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.entities.enums.UserType;
import com.kozitskiy.dorm4.sys.exceptions.UserNotFoundException;
import com.kozitskiy.dorm4.sys.repositories.UserRepository;
import com.kozitskiy.dorm4.sys.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestAccessService {
    private final SecurityService securityService;
    private final UserRepository userRepository;

    public void validateRequestCreationRights(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!securityService.isCurrentUserAdmin() && !user.getUserType().equals(UserType.STUDENT))
        {
            throw new AccessDeniedException("You do not have permission to create requests");
        }
        if (!user.getUserType().equals(UserType.STUDENT))// Check if admin create request only for student
        {
            throw new AccessDeniedException("Only students can create requests");
        }
    }
}
