package com.kozitskiy.dorm4.sys.service.notification;

import com.kozitskiy.dorm4.sys.dto.notification.NotificationCreateDto;
import com.kozitskiy.dorm4.sys.dto.notification.NotificationResponseDto;
import com.kozitskiy.dorm4.sys.dto.user.UserResponseDto;
import com.kozitskiy.dorm4.sys.entities.Notification;
import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.exceptions.NotificationNotFoundException;
import com.kozitskiy.dorm4.sys.exceptions.UserNotFoundException;
import com.kozitskiy.dorm4.sys.repositories.NotificationRepository;
import com.kozitskiy.dorm4.sys.repositories.UserRepository;
import com.kozitskiy.dorm4.sys.security.service.SecurityService;
import com.kozitskiy.dorm4.sys.service.request.RequestAccessService;
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
    public NotificationResponseDto createNotificationForUser(NotificationCreateDto dto) {

        if (dto.message() == null || dto.message().isEmpty()) {
            throw new IllegalStateException("Message cannot be empty");
        }

        if (dto.recipientUserId() == null) {
            throw new IllegalStateException("Recipient userId cannot be empty");
        }

        // Проверка прав текущего пользователя
        Long currentUserId = securityService.getCurrentUserId();
        requestAccessService.validateRequestCreationRights(currentUserId);

        // Получаем получателя
        User recipient = userRepository.findById(dto.recipientUserId()).orElseThrow(
                () -> new UserNotFoundException("Recipient not found")
        );

        Notification notification = Notification.builder()
                .message(dto.message())
                .user(recipient)
                .isRead(false)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        recipient.getNotifications().add(savedNotification);

        return NotificationResponseDto.builder()
                .message(savedNotification.getMessage())
                .userResponseDto(UserResponseDto.builder()
                        .id(savedNotification.getUser().getId())
                        .username(savedNotification.getUser().getUsername())
                        .userType(savedNotification.getUser().getUserType())
                        .build())
                .build();
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public Notification updateNotification(Long id, NotificationCreateDto notification) {
        Notification notificationToUpdate = notificationRepository.findById(id).orElseThrow(
                () -> new NotificationNotFoundException("Notification was not found")
        );
        notificationToUpdate.setMessage(notification.message());

        return notificationRepository.save(notificationToUpdate);
    }

    @Override
    @Transactional
    public List<NotificationResponseDto> findAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(notification -> NotificationResponseDto
                        .builder()
                        .message(notification.getMessage())
                        .userResponseDto(UserResponseDto.builder()
                                .id(notification.getUser().getId())
                                .username(notification.getUser().getUsername())
                                .userType(notification.getUser().getUserType())
                                .build())
                        .build())
                .toList();

    }

    @Override
    public NotificationResponseDto findNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(
                () -> new NotificationNotFoundException("Notification was not found")
        );

        UserResponseDto userDto = UserResponseDto.builder()
                .id(notification.getUser().getId())
                .username(notification.getUser().getUsername())
                .userType(notification.getUser().getUserType())
                .build();

        return NotificationResponseDto.builder()
                .message(notification.getMessage())
                .userResponseDto(userDto)
                .build();
    }
}
