package com.cabApplication.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabApplication.admin.businessLayer.AssignedCabsTripSheetBL;
import com.cabApplication.admin.entity.BookingRequest;
import com.cabApplication.admin.entity.DriverInfo;
import com.cabApplication.admin.entity.TripCabInfo;
import com.cabApplication.admin.entity.TripCabInfoDTO;
import com.cabApplication.admin.exception.handler.GlobalExceptionHandler;
import com.cabApplication.admin.logger.LogFileCreation;

@RestController
@RequestMapping(path="/api/v1/")
@CrossOrigin(origins = "*")
public class OnGoingTripSheetController {
	
	@Autowired
	private AssignedCabsTripSheetBL tripSheetBl;

	@Autowired
	GlobalExceptionHandler globalException;
	
	@Autowired
	LogFileCreation logFile;
	
	//get cab details and employee details
		@GetMapping(path = "/ongoingtripsheet/{tripCabId}")
		public ResponseEntity<TripCabInfoDTO> gettripCabId(@PathVariable ("tripCabId" ) long tripCabId,HttpServletRequest request) throws IOException{
			
			try {
				TripCabInfo cab= this.tripSheetBl.gettripCabId(tripCabId);
				
				List<BookingRequest> booking=this.tripSheetBl.getBookingRequests(tripCabId);
				
				DriverInfo driver=this.tripSheetBl.getDriverInfo(cab.getDriverId());
				
				TripCabInfoDTO cabDto=new  TripCabInfoDTO(tripCabId, cab.getCabNumber() , driver , cab.getSource() , cab.getDestination(), cab.getDateOfTravel(), cab.getTimeSlot() , cab.getTotalSeats(), cab.getAllocatedSeats(), cab.getRemainingSeats(), cab.getStatus(), cab.getStartTime(), cab.getEndTime(), booking);
				
				return ResponseEntity.status(HttpStatus.OK).body(cabDto);
			} catch (Exception e) {

				globalException.writeToExceptionFile(e,request);
				e.printStackTrace();
			}
			return null;
		}
		
		
		
		//endTime
		@PutMapping(path= "/ongoingtripsheet/endtime/{tripCabId}")
		public ResponseEntity<TripCabInfo> updateEndTime(@PathVariable ("tripCabId" ) long tripCabId,HttpServletRequest request) throws IOException
		{
			try {
				TripCabInfo trip = this.tripSheetBl.updateEndTime(tripCabId);
				return ResponseEntity.status(HttpStatus.OK).body(trip);
			} catch (Exception e) {

				globalException.writeToExceptionFile(e,request);
				e.printStackTrace();
			}
			return null;
		}
		//reached time for each emplolyee
	    @PutMapping(path = "/update/reachedtime/{bookingId}")
		public BookingRequest updateReachedTime(@PathVariable ("bookingId" ) long bookingId,HttpServletRequest request) throws IOException {
	    	
	    	try {
				return this.tripSheetBl.updateReachedTime(bookingId);
			} catch (Exception e) {
				globalException.writeToExceptionFile(e,request);
				e.printStackTrace();
			}
			return null;
	    }

	    //update the cabstatus and employee status
	    @PutMapping(path = "/update/time/status/{tripCabId}/{showList}/{noShowList}")
	    public ResponseEntity<Boolean> updateTimeAndStatus(@PathVariable ("tripCabId" ) long tripCabId, @PathVariable ("showList" ) List<String> showList, @PathVariable ("noShowList" ) List<String> noShowList,HttpServletRequest request) throws IOException {
	    	
	    	try {
				this.tripSheetBl.updateCabStatus(tripCabId);
				this.tripSheetBl.updateEmployeeStatus(tripCabId, showList, noShowList);
				return ResponseEntity.status(HttpStatus.OK).body(true);
			} catch (Exception e) {
				globalException.writeToExceptionFile(e,request);
				e.printStackTrace();
			}
			return null;
	    }
	    
	    
}