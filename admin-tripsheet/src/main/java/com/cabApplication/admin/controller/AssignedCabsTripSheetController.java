package com.cabApplication.admin.controller;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

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

import com.cabApplication.admin.businessLayer.AssignedCabsTripSheetBL;
import com.cabApplication.admin.entity.BookingRequest;
import com.cabApplication.admin.entity.Destination;
import com.cabApplication.admin.entity.DriverInfo;
import com.cabApplication.admin.entity.Employee;
import com.cabApplication.admin.entity.TripCabInfo;
import com.cabApplication.admin.entity.TripCabInfoDTO;
import com.cabApplication.admin.exception.handler.GlobalExceptionHandler;
import com.cabApplication.admin.logger.LogFileCreation;

@RestController
@RequestMapping(path="/api/v1/")
public class AssignedCabsTripSheetController {
	
	
	@Autowired
	GlobalExceptionHandler globalException;
	
	@Autowired
	LogFileCreation logFile;
	
	@Autowired
	private AssignedCabsTripSheetBL tripSheetBl;	
	
	//get cab details and employee details
	@GetMapping(path = "/tripsheet/{tripCabId}")
	public ResponseEntity<TripCabInfoDTO> gettripCabId(@PathVariable ("tripCabId" ) long tripCabId,HttpServletRequest request) throws IOException{
		try {
			TripCabInfo cab= this.tripSheetBl.gettripCabId(tripCabId);
			
			if(cab.getStatus().equalsIgnoreCase("Cancelled")) {
				return ResponseEntity.status(233).body(null);
			}
			
			List<BookingRequest> booking=this.tripSheetBl.getBookingRequests(tripCabId);
			
			DriverInfo driver=this.tripSheetBl.getDriverInfo(cab.getDriverId());
			
			TripCabInfoDTO cabDto=new  TripCabInfoDTO(tripCabId, cab.getCabNumber() , driver , cab.getSource() , cab.getDestination(), cab.getDateOfTravel(), cab.getTimeSlot() , cab.getTotalSeats(), cab.getAllocatedSeats(), cab.getRemainingSeats(), cab.getStatus(), cab.getStartTime(), cab.getEndTime(), booking);
			
			if(cabDto.getStartTime() != null)
			{
				return ResponseEntity.status(232).body(cabDto);
			} 
			return ResponseEntity.status(HttpStatus.OK).body(cabDto);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
	}
	
	
	//startTime
	@PutMapping(path= "/tripsheet/starttime/{tripCabId}")
	public void updateStartTime(@PathVariable ("tripCabId" ) long tripCabId,HttpServletRequest request) throws IOException
	{
		try {
			this.tripSheetBl.updateStartTime(tripCabId);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
	}
	

	//get employee name
	
	@GetMapping(path = "/tripsheet/employee/{employeeId}")
	public ResponseEntity<Employee> getEmployeeName(@PathVariable ("employeeId") String employeeId,HttpServletRequest request) throws IOException
	{
		try {
			Employee emp=this.tripSheetBl.getEmployeeName(employeeId);
			
			return ResponseEntity.status(HttpStatus.OK).body(emp);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
		
	}
	
	@GetMapping(path = "/tripsheet/employeeName/{employeeName}")
	public ResponseEntity<Employee> getEmployeeId(@PathVariable ("employeeName") String employeeName,HttpServletRequest request) throws IOException
	{
		try {
			Employee emp=this.tripSheetBl.getEmployeeId(employeeName);
			
			return ResponseEntity.status(HttpStatus.OK).body(emp);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	//add new employee
	@PostMapping(path = "/tripsheet/addemployee/{tripCabId}")
    public ResponseEntity<BookingRequest> addEmployee(@RequestBody BookingRequest request, @PathVariable("tripCabId") long tripCabId,HttpServletRequest httpRequest) throws IOException
    {
       
        try {
			BookingRequest req=this.tripSheetBl.addEmployee(request, tripCabId);
      
			if(req==null) {
			    return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(req);
			}
      
			return ResponseEntity.status(HttpStatus.OK).body(req);
		} catch (Exception e) {
	
			globalException.writeToExceptionFile(e,httpRequest);
			
			e.printStackTrace();
		}
		return null;
               
    }
	
	
	
	//update drop point
	
	@PutMapping(path = "/tripsheet/update/droppoint")
	public ResponseEntity<Boolean> updateEmployee(@RequestBody BookingRequest request,HttpServletRequest httpRequest) throws IOException
	{
		try {
			boolean req=this.tripSheetBl.updateEmployee(request);
			
			return ResponseEntity.status(HttpStatus.OK).body(req);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,httpRequest);
			e.printStackTrace();
		}
		return null;
				
	}
	
	//delete employee
	@PutMapping(path= "/tripsheet/delete/booking/{bookingId}")
	public ResponseEntity<Boolean> deleteEmployee(@PathVariable ("bookingId") long bookingId,HttpServletRequest request) throws IOException
	{
		try {
			boolean req=this.tripSheetBl.deleteEmployee(bookingId);
			return ResponseEntity.status(HttpStatus.OK).body(req);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
	}
	
	//get drop points
	@GetMapping(path = "/tripsheet/droppoints/{destination}")
	public ResponseEntity<Destination> getDestination(@PathVariable ("destination") String destination,HttpServletRequest request) throws IOException
	{
		try {
			Destination dest=this.tripSheetBl.getDestination(destination);
			return ResponseEntity.status(HttpStatus.OK).body(dest);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	//get employee details
	@GetMapping(path= "/tripsheet/getallemployee")
	public ResponseEntity<List<Employee>> getAllEmployeeDetails(HttpServletRequest request) throws IOException{
		try {
			List<Employee> employees=this.tripSheetBl.getAllEmployeeDetails();
			return ResponseEntity.status(HttpStatus.OK).body(employees);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
	}
	

	
	//All employee are deleted in the tripsheet   
    @PutMapping(path = "/tripSheet/NoEmployees/endTrip/{tripCabId}")
    public ResponseEntity<TripCabInfo> updateTripCabInfoStatus(@PathVariable("tripCabId") long tripCabId,HttpServletRequest request) throws IOException{
       
        try {
			TripCabInfo trip = this.tripSheetBl.updateTripCabInfoStatus(tripCabId);
			return ResponseEntity.status(HttpStatus.OK).body(trip);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
       
    }
	
	
	//serverTime
	
	@GetMapping(path = "/serverTime")
	public LocalTime getServerTime(HttpServletRequest request) throws IOException {
		try {
			return LocalTime.now();
		} catch (Exception e) {

			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
}
