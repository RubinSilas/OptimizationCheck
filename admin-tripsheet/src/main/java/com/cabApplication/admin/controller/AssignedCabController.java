package com.cabApplication.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabApplication.admin.businessLayer.AssignedCabBL;
import com.cabApplication.admin.entity.Destination;
import com.cabApplication.admin.entity.DriverInfo;
import com.cabApplication.admin.entity.SourceBO;
import com.cabApplication.admin.entity.TripCabInfo;
import com.cabApplication.admin.entity.TripDetails;
import com.cabApplication.admin.exception.handler.GlobalExceptionHandler;
import com.cabApplication.admin.logger.LogFileCreation;



@RestController
@RequestMapping(path = "/api/v1")

public class AssignedCabController {
	@Autowired
	AssignedCabBL assignedCabBl;
	
	@Autowired
	GlobalExceptionHandler globalException;
	
	@Autowired
	LogFileCreation logFile;
	

	@Autowired
	com.cabApplication.admin.repository.DriverInfoRepository driverrepo;
//----------------------------------AssignedCab Method Starts--------------------------------------------------	

// FindAll method--->starts

	@GetMapping(path = "/assignedCab/cabs")
	public ResponseEntity<List<TripCabInfo>> findAssignedCab(HttpServletRequest request) throws IOException {

		try {
			List<TripCabInfo> trip = this.assignedCabBl.findAssignedCab();
			return ResponseEntity.status(HttpStatus.OK).body(trip);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
	}
//-------------------------------------------------------------------------------------------	

// To add source to dropDown ---> controller
	@PostMapping(path = "/source")
	public ResponseEntity<SourceBO> addSource(@RequestBody SourceBO source,HttpServletRequest request) throws IOException {
		try {
			SourceBO src = this.assignedCabBl.save(source);
			return ResponseEntity.status(HttpStatus.OK).body(src);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping(path = "/sourcelist")
	public ResponseEntity<List<SourceBO>> getSource(HttpServletRequest request) throws IOException {
		try {
			List<SourceBO> source = this.assignedCabBl.findSource();
			return ResponseEntity.status(HttpStatus.OK).body(source);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
	}
	// ----> source ---> end

//---------------------------------------------------------------------------------------  
//-->To add Destination List to dropDown - starts

	@PostMapping(path = "/destination")
	public ResponseEntity<Destination> addDestination(@RequestBody Destination des,HttpServletRequest request) throws IOException {

		try {
			Destination desBo = this.assignedCabBl.save(des);
			return ResponseEntity.status(HttpStatus.OK).body(desBo);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping(path = "/destinationlist")
	public ResponseEntity<List<Destination>> getDestination(HttpServletRequest request) throws IOException {
		try {
			List<Destination> dest = this.assignedCabBl.findDestination();
			return ResponseEntity.status(HttpStatus.OK).body(dest);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
	}
// ----- Destination ends  

//---------------------------------------------------------------------------------------   

// Using MongoTemplate for (FilterRequest)  
	@GetMapping("/filter/{source}/{destination}/{timeSlot}/{skip}/{limit}")
	public ResponseEntity<List<TripDetails>> getByFilterRequest(@PathVariable("source") String source,
			@PathVariable("destination") String destination, @PathVariable("timeSlot") String timeSlot,
			@PathVariable("skip") long skip, @PathVariable("limit") int limit,HttpServletRequest request) throws IOException {
	
     try {
		List<TripDetails> details= this.assignedCabBl.getByFilter(source,destination,timeSlot,skip,limit);
		 return ResponseEntity.status(HttpStatus.OK).body(details);
	} catch (Exception e) {
		globalException.writeToExceptionFile(e,request);
		e.printStackTrace();
	}
	return null; 
	}

//---------------------------------------------------------------------------------------   
// Scroll method with MongoTemplate to Fetch all AssignedCabs-------FindAll
	@GetMapping(path = "/scroll/{skip}/{limit}")
	public ResponseEntity<List<TripDetails>> getAssignedCabByScroll(@PathVariable("skip") long skip,
			@PathVariable("limit") int limit,HttpServletRequest request) throws IOException {
		try {
			//System.out.println("scroll method called");
			List<TripDetails> details= this.assignedCabBl.getAssignedCabByScroll(skip,limit);
			//System.out.println(details);
			return ResponseEntity.status(HttpStatus.OK).body(details);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
	}

//---------------------------------------------------------------------------------------
// To get the data count
	@GetMapping(path = "/count")
	public ResponseEntity<Long> getCount(HttpServletRequest request) throws IOException {
		try {
			Long count = this.assignedCabBl.getCount();
			return ResponseEntity.status(HttpStatus.OK).body(count);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
	}

//--------------------------------------------------------------------------------------
//Search Method
	@GetMapping(path = "/{searchValue}/{skip}/{limit}")
	public ResponseEntity<?> getByTextSearch(@PathVariable(name = "searchValue") String text,
			@PathVariable("skip") long skip, @PathVariable("limit") int limit,HttpServletRequest request) throws IOException {

    try {
		List<TripDetails> details= this.assignedCabBl.getByTextSearch(text,skip,limit);
		return ResponseEntity.status(HttpStatus.OK).body(details);
	} catch (Exception e) {
		globalException.writeToExceptionFile(e,request);
		e.printStackTrace();
	}
	return null;
	
	}
// Search----> ends.

//---------------------------------AssignedCab Methods End-------------------------------------------------------	



	@PostMapping(path = "/driver")
	public ResponseEntity<DriverInfo> addDriver(@RequestBody DriverInfo driver,HttpServletRequest request) throws IOException {

		try {
			DriverInfo drv = this.assignedCabBl.save(driver);
			return ResponseEntity.status(HttpStatus.OK).body(drv);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
	}
}
