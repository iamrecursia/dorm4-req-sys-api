package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.dto.CreateNotificationDto;
import com.kozitskiy.dorm4.sys.dto.NotificationResponseDto;
import com.kozitskiy.dorm4.sys.entities.Notification;

import java.util.List;

public interface NotificationService {
    Notification createNotification(CreateNotificationDto notification);
    void deleteNotification(Long id);
    Notification updateNotification(Long id, CreateNotificationDto notification);
    List<Notification> getNotifications();
    NotificationResponseDto findNotificationById(Long id);
}
