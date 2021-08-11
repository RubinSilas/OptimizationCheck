package com.cabApplication.admin.dataLayer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cabApplication.admin.entity.Destination;
import com.cabApplication.admin.entity.DropPoint;
import com.cabApplication.admin.entity.TimeSlot;
import com.cabApplication.admin.repository.DestinationRepository;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

@Service
public class DestinationDL {

	@Autowired
	DestinationRepository repo;
	@Autowired
	MongoTemplate mongoTemplate;

	public List<Destination> findAll() {
		return repo.findAll();
	}

	public Optional<Destination> findById(Integer id) {
		return repo.findById(id);
	}

	public List<String> findAllDestName() {

		List<String> destNameList = new ArrayList<>();

		MongoCollection mongoCollection = mongoTemplate.getCollection("destination");

		DistinctIterable distinctIterable = mongoCollection.distinct("destName", String.class);

		MongoCursor cursor = distinctIterable.iterator();

		while (cursor.hasNext()) {
			String destName = (String) cursor.next();
			destNameList.add(destName);
		}
		return destNameList;
	}

	public Destination addDestination(Destination entity) {
		entity.setDestinationId(repo.count() + 1);

		entity.setIsDeleted(0);

		entity.setCreatedBy("Admin");

		entity.setCreatedDate(LocalDateTime.now());

		Destination dest = this.repo.save(entity);

		return dest;
	}

	public Destination addDropPoint(Destination destInfo) {

		return this.repo.save(destInfo);
	}

	public Destination addTimeSlot(Destination destInfo) {

		return this.repo.save(destInfo);
	}

	public Destination deleteRoute(String destination) {

		Optional<Destination> route = this.repo.findByDestination(destination);
		Destination deletedRoute = route.get();

		deletedRoute.setIsDeleted(1);
		deletedRoute.setModifiedBy("Admin");

		deletedRoute.setModifiedDate(LocalDateTime.now());
		return this.repo.save(deletedRoute);
	}

	public List<Destination> findByIsDeleted(int c) {

		return this.repo.findByIsDeleted(c);
	}
}
