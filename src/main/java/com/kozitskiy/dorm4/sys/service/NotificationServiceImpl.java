package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.dto.CreateNotificationDto;
import com.kozitskiy.dorm4.sys.dto.NotificationResponseDto;
import com.kozitskiy.dorm4.sys.dto.UserResponseDto;
import com.kozitskiy.dorm4.sys.entities.Notification;
import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.exceptions.NotificationNotFoundException;
import com.kozitskiy.dorm4.sys.exceptions.UserNotFoundException;
import com.kozitskiy.dorm4.sys.repositories.NotificationRepository;
import com.kozitskiy.dorm4.sys.repositories.UserRepository;
import com.kozitskiy.dorm4.sys.security.service.SecurityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final RequestAccessService requestAccessService;

    @Override
    @Transactional
    public Notification createNotification(CreateNotificationDto dto) {

        if (dto.message() == null || dto.message().isEmpty()) {
            throw new IllegalStateException("Message cannot be empty");
        }

        Long currentUserId = securityService.getCurrentUserId();
        User user = userRepository.findById(currentUserId).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );

        requestAccessService.validateRequestCreationRights(currentUserId);

        Notification notification = Notification.builder()
                .message(dto.message())
                .user(user)
                .isRead(false)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        user.getNotifications().add(notification);

        return savedNotification;

    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public Notification updateNotification(Long id, CreateNotificationDto notification) {
        Notification notificationToUpdate = notificationRepository.findById(id).orElseThrow(
                () -> new NotificationNotFoundException("Notification was not found")
        );
        notificationToUpdate.setMessage(notification.message());
        notificationToUpdate.setIsRead(notification.isRead());

        return notificationRepository.save(notificationToUpdate);
    }

    @Override
    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public NotificationResponseDto findNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(
                () -> new NotificationNotFoundException("Notification was not found")
        );

        Long currentUser = securityService.getCurrentUserId();
        User user = userRepository.findById(currentUser).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );

        UserResponseDto userDto = UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .userType(user.getUserType())
                .build();

        return NotificationResponseDto.builder()
                .message(notification.getMessage())
                .userResponseDto(userDto)
                .build();
    }
}
