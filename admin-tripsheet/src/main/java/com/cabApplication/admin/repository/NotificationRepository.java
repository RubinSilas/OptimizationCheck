package com.cabApplication.admin.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.cabApplication.admin.entity.Notification;

public interface NotificationRepository extends MongoRepository<Notification, Long> {

}
