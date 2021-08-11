package com.cabApplication.admin.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabApplication.admin.entity.Destination;
import com.cabApplication.admin.entity.DropPoint;
import com.cabApplication.admin.entity.TimeSlot;
import com.cabApplication.admin.exception.handler.GlobalExceptionHandler;
import com.cabApplication.admin.logger.LogFileCreation;
import com.cabApplication.admin.repository.DestinationRepository;

@RestController
@RequestMapping(path = "/api/v1/")
@CrossOrigin(origins = "*")
public class DestinationController {

	@Autowired
	com.cabApplication.admin.dataLayer.DestinationDL dlLayer;
	@Autowired
	com.cabApplication.admin.businessLayer.DestinationBL blLayer;
	@Autowired
	DestinationRepository repo;

	@Autowired
	GlobalExceptionHandler globalException;

	@Autowired
	LogFileCreation logFile;

	@GetMapping(path = "/managingRoute")
	public ResponseEntity<List<Destination>> getAllRoute(HttpServletRequest request) throws IOException {

		try {
			List<Destination> destination = this.blLayer.finfByIsDeleted(0);

			return ResponseEntity.status(HttpStatus.OK).body(destination);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping(path = "/managingRoute/newRoute")
	public ResponseEntity<Destination> addNewRoute(@RequestBody Destination entity, HttpServletRequest request)
			throws IOException {

		try {
			Optional<Destination> dest = repo.findByDestination(entity.getDestination());
			if (dest.isPresent()) {
				if (dest.get().getIsDeleted() == 1) {
					// dest.get().setIsDeleted(0);
					repo.save(entity);
					return ResponseEntity.status(HttpStatus.CREATED).body(entity);
				}

				else {
					return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(entity);
				}

			}

			Destination post = blLayer.addDestination(entity);

			return ResponseEntity.status(HttpStatus.CREATED).body(post);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}
		return null;
	}

	@PutMapping(path = "/put/deleteRouteDetails/{destination}")
	public ResponseEntity<Destination> deleteRoute(@PathVariable("destination") String destination,
			HttpServletRequest request) throws IOException {

		try {
			Destination RouteInfo = this.dlLayer.deleteRoute(destination);

			return ResponseEntity.status(HttpStatus.OK).body(RouteInfo);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}
		return null;
	}

	@PutMapping(path = "/updateRouteInfo/{destination}")
	public ResponseEntity<Destination> editRouteDetails(@PathVariable("destination") String destination,
			@RequestBody Destination updateRouteInfo, HttpServletRequest request) throws IOException {

		try {
			Destination dest = repo.findByDestination(destination).get();
			dest.setIsDeleted(0);

			dest.setModifiedBy("Admin");
			dest.setModifiedDate(LocalDateTime.now());

			Destination saveRouteInfo = null;

			dest.setTimeSlots(updateRouteInfo.getTimeSlots());
			dest.setDropPoints(updateRouteInfo.getDropPoints());

			saveRouteInfo = repo.save(dest);

			return ResponseEntity.status(HttpStatus.OK).body(saveRouteInfo);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping(path = "/editRoute/{destination}")
	public ResponseEntity<Destination> getDestinationDetails(@PathVariable("destination") String destination,
			HttpServletRequest request) throws IOException {
		try {
			Optional<Destination> details = this.repo.findByDestination(destination);
			return ResponseEntity.status(HttpStatus.OK).body(details.get());
		} catch (Exception e) {
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping(path = "/managigRoute/all/destName")
	public ResponseEntity<List<String>> getdestName(HttpServletRequest request) throws IOException {
		try {
			List<String> destName = this.dlLayer.findAllDestName();

			return ResponseEntity.status(HttpStatus.OK).body(destName);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}
		return null;
	}

}
