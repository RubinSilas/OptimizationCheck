package com.cabApplication.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cabApplication.admin.entity.Destination;

@Repository
public interface DestinationRepository extends MongoRepository<Destination, Integer> {

	Optional<Destination> findByDestination(String destination);

	List<Destination> findByIsDeleted(int isDeleted);

}
