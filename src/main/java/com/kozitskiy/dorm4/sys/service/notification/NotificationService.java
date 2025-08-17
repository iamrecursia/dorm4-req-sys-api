package com.kozitskiy.dorm4.sys.service.notification;

import com.kozitskiy.dorm4.sys.dto.notification.NotificationCreateDto;
import com.kozitskiy.dorm4.sys.dto.notification.NotificationResponseDto;
import com.kozitskiy.dorm4.sys.entities.Notification;

import java.util.List;

public interface NotificationService {
    NotificationResponseDto createNotificationForUser(NotificationCreateDto notification);
    void deleteNotification(Long id);
    Notification updateNotification(Long id, NotificationCreateDto notification);
    List<NotificationResponseDto> findAllNotifications();
    NotificationResponseDto findNotificationById(Long id);
}
