package com.kozitskiy.dorm4.sys.repositories;

import com.kozitskiy.dorm4.sys.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
