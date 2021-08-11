package com.cabApplication.admin.repository;



import org.springframework.data.mongodb.repository.MongoRepository;

import com.cabApplication.admin.entity.SourceBO;

public interface SourceRepository extends MongoRepository<SourceBO, Integer> {

	
	
}
