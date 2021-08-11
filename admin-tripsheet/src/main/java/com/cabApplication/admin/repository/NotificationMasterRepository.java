package com.cabApplication.admin.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.cabApplication.admin.entity.NotificationMaster;

public interface NotificationMasterRepository extends MongoRepository<NotificationMaster, Long> {

}
