package com.cabApplication.admin.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabApplication.admin.businessLayer.ManageDriversBL;
import com.cabApplication.admin.dataLayer.ManageDriversDL;
import com.cabApplication.admin.entity.DriverInfo;
import com.cabApplication.admin.exception.handler.GlobalExceptionHandler;
import com.cabApplication.admin.logger.LogFileCreation;
import com.cabApplication.admin.repository.ManageDriversRepository;
import com.cabApplication.admin.status.ManageDriverResponseStatus;

@RestController
@RequestMapping(path = "/manage/drivers")
public class ManageDriversController {
	
	@Autowired
	private ManageDriversBL manageDriversBL;
	
	@Autowired
	private ManageDriversDL manageDriversDL;
	
	@Autowired
	private ManageDriversRepository repo;
	
	@Autowired
	GlobalExceptionHandler globalException;
	
	@Autowired
	LogFileCreation logFile;
	
	// This method is used to get all the driver details//
	@GetMapping(path = "/list")
	public ResponseEntity<List<DriverInfo>> getAllDrivers(HttpServletRequest request) throws IOException {
		
		try {
			List<DriverInfo> driverList = this.manageDriversBL.findByIsDeleted(0);
			return ResponseEntity.status(HttpStatus.OK).body(driverList);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;

	}
	//End of get all driver details method//
	

	
	//This method is used to delete the driver details//
	@PutMapping(path = "/deletedriver/{driverId}")
	public ResponseEntity<DriverInfo> deleteDriver(@PathVariable("driverId") long driverId,HttpServletRequest request) throws IOException{
				
		try {
			DriverInfo info = this.manageDriversBL.deleteDriverDetails(driverId);
			
			return ResponseEntity.status(HttpStatus.OK).body(info);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
		
	}
	//End of delete method//
	

	
	@PostMapping(path = "/add/newdriver")
	public ResponseEntity<DriverInfo> addDriverInfo(@RequestBody DriverInfo driverInfo,HttpServletRequest request) throws IOException{
		
		try {
			DriverInfo reqDriver = driverInfo;
			reqDriver.setIsDeleted(0);
			
			DriverInfo createdBy = driverInfo;
			createdBy.setCreatedBy("Admin");
			
			DriverInfo createdDate = driverInfo;
			createdDate.setCreatedDate(LocalDateTime.now());
			
			driverInfo.setDriverId(repo.count()+1);
			Long driverId = driverInfo.getDriverId();
			
			Long number = driverInfo.getDriverNumber();
			Optional<DriverInfo> entitynumber = repo.findByDriverNumber(number);
			
			String licNumber = driverInfo.getLicenseNumber();
			Optional<DriverInfo> entitylic = repo.findByLicenseNumber(licNumber);
			
			DriverInfo saveDriverInfo = null;
			
			if(entitynumber.isPresent() && entitynumber.get().getIsDeleted() == 0) {
				
				return  ResponseEntity.status(ManageDriverResponseStatus.DRIVERNUMBEREXIST).body(null);
			}
			
			else if(entitylic.isPresent() && entitylic.get().getIsDeleted() == 0) {
				
				return  ResponseEntity.status(ManageDriverResponseStatus.LICENSENUMBEREXIST).body(null);					
			}
//		else if(entitynumber.isPresent() && entitylic.isPresent()) {
//			
//			return new ResponseEntity<>(null,HttpStatus.CONFLICT);
//			
//		}
			
			else {
				
				saveDriverInfo = this.manageDriversDL.saveDriverDetails(driverInfo);
				
				return ResponseEntity.status(HttpStatus.CREATED).body(saveDriverInfo);
			}
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
	}
	
	@PutMapping(path = "/edit")
	public ResponseEntity<DriverInfo> editDriverDetails(@RequestBody DriverInfo updateDriverDetails,HttpServletRequest request) throws IOException{
		
		
		try {
			DriverInfo isDeleted = updateDriverDetails;
			isDeleted.setIsDeleted(0);
			
			DriverInfo modifiedBy = updateDriverDetails;
			modifiedBy.setModifiedBy("Admin");
			
			DriverInfo modifiedDate = updateDriverDetails;
			modifiedDate.setModifiedDate(LocalDateTime.now());
			
			Long id = updateDriverDetails.getDriverId();
			
			Long number = updateDriverDetails.getDriverNumber();
			Optional<DriverInfo> entitynumber = repo.findByDriverNumber(number);
			
			String licNumber = updateDriverDetails.getLicenseNumber();
			Optional<DriverInfo> entitylic = repo.findByLicenseNumber(licNumber);
			
			Optional<DriverInfo> entity = repo.findById(id);
			DriverInfo updateDriverInfo = null;
			
			
			if(entitynumber.isPresent() && !(entitynumber.get().getDriverId() == updateDriverDetails.getDriverId()) && entitynumber.get().getIsDeleted() == 0) {
				
				return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
				
			}
			
			else if(entitylic.isPresent() && !(entitylic.get().getDriverId() == updateDriverDetails.getDriverId()) && entitylic.get().getIsDeleted() == 0) {
				
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

			}
			
			else {
					updateDriverInfo = this.manageDriversDL.updateDriverDetails(updateDriverDetails);
					return ResponseEntity.status(HttpStatus.CREATED).body(updateDriverInfo);
			}
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
	}
}
		
	


	
	
	


