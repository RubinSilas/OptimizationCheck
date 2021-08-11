package com.cabApplication.admin.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cabApplication.admin.entity.TripCabInfo;

public interface AssignedCabRepository extends MongoRepository<TripCabInfo ,Long> {

	List<TripCabInfo> findByCabNumber(String cabNumber);

	List<TripCabInfo> findByDestination(String destination);

	
	

	



	
   
	

}
