package com.cabApplication.admin.controller;


import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabApplication.admin.businessLayer.AssigningBL;
import com.cabApplication.admin.entity.AssigningBO;
import com.cabApplication.admin.entity.BookingRequest;
import com.cabApplication.admin.entity.CabInfo;
import com.cabApplication.admin.entity.Destination;
import com.cabApplication.admin.entity.DriverInfo;
import com.cabApplication.admin.entity.SourceBO;
import com.cabApplication.admin.entity.TripCabInfo;
import com.cabApplication.admin.exception.handler.GlobalExceptionHandler;
import com.cabApplication.admin.logger.LogFileCreation;
import com.cabApplication.admin.status.CustomStatus;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/bookingRequest")
public class AssigningController {

	@Autowired
	private AssigningBL assigningBL;
	
	
	@Autowired
	GlobalExceptionHandler globalException;
	

	@Autowired
	LogFileCreation logFile;
//--------------------------------------------------------Today's Request Starts Here-----------------------------------------------------//
	
	public AssigningController(AssigningBL busy) {
		super();
		this.assigningBL = busy;
	}
	
	
	
	
	

	
	// to get all bookings
	
	@GetMapping(path="/bookings/srch")
	public List<BookingRequest> getAllBooks(HttpServletRequest request) throws IOException
	{
		try {
			
			return this.assigningBL.findAllBooks();
		} catch (Exception e) {
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	//to get all bookings with status booked
	
	@GetMapping(path="/bookings/todayBookings/{skip}/{limit}")
	public List<BookingRequest> findAllBooking(@PathVariable("skip") long skip,@PathVariable("limit") int limit,HttpServletRequest request) throws IOException
	{
		try {
			Query query = new Query();
			query.limit(limit).skip(skip);
			Criteria sourceCriteria = Criteria.where("status").is("Booked");
			query.addCriteria(sourceCriteria);
			
			return this.assigningBL.findAllBooking(query);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	//to apply filters
	
	@GetMapping("/filter/{source}/{destination}/{dropPoint}/{timeSlot}/{skip}/{limit}")
	   public List<BookingRequest> getByFilterRequest(@PathVariable("source") String source,@PathVariable("destination") String destination,@PathVariable("dropPoint") String dropPoint,@PathVariable("timeSlot") String timeSlot,@PathVariable("skip") long skip,@PathVariable("limit") int limit,HttpServletRequest request) throws IOException{
	       Query dynamicQuery;
		try {
			dynamicQuery = new Query();
			   if (!(source.equals("0")))
			   {
			      Criteria sourceCriteria = Criteria.where("source").is(source);
			      dynamicQuery.addCriteria(sourceCriteria);
			   }
			   if (!(destination.equals("0")))
			   {
			      Criteria destinationCriteria =     Criteria.where("destination").is(destination);
			      dynamicQuery.addCriteria(destinationCriteria);
			   }
			   if (!(dropPoint.equals("0")))
			   {
			      Criteria dropPointCriteria =     Criteria.where("dropPoint").is(dropPoint);
			      dynamicQuery.addCriteria(dropPointCriteria);
			   }
			   
			   Criteria criteria = Criteria.where("status").is("Booked");
				dynamicQuery.addCriteria(criteria);
	
	       
			
	       
	    //   List<BookingRequestBO> result1=result.stream().skip(skip).limit(limit).collect(Collectors.toList());
	       
	      
	       
	       
	       if (!(timeSlot.equals("0")))
	       {
//	    	   LocalTime lt=LocalTime.parse(timeSlot);
//	           List<BookingRequestBO> timeFilter= result.stream().filter(e->e.getTimeSlot().equals(lt)).collect(Collectors.toList());
//	        //   List<BookingRequestBO> timeFilter1=timeFilter.stream().skip(skip).limit(limit).collect(Collectors.toList());
//	           return timeFilter;
	    	   
	    	   String splittedTimeSlot[] = timeSlot.split(":");
	    	 //  System.out.println(splittedTimeSlot[0]);
	    	   Criteria timeSlotCriteria =     Criteria.where("timeSlot").is(LocalTime.of(Integer.parseInt(splittedTimeSlot[0]), Integer.parseInt(splittedTimeSlot[1]), Integer.parseInt(splittedTimeSlot[2]))); 
	    	    
		          dynamicQuery.addCriteria(timeSlotCriteria);
	    	   
	       }
	       
	       dynamicQuery.limit(limit).skip(skip);
	       
	       List<BookingRequest> result=this.assigningBL.filterRequests(dynamicQuery);
	       

	      return result;	
	      } catch (Exception e) {
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}
		return null;
	   }
	
	//test
	@GetMapping("/test")
	public List<BookingRequest> getTime(HttpServletRequest request) throws IOException {
		
		try {
			Query query = new Query();
//		query.limit(limit).skip(skip);
			Criteria sourceCriteria = Criteria.where("status").is("Booked");
			Criteria sourceCriteria1 = Criteria.where("timeSlot").is(LocalTime.of(00, 00, 00));
			query.addCriteria(sourceCriteria);
			query.addCriteria(sourceCriteria1);
			
			return this.assigningBL.filterRequests(query);
		} catch (Exception e) {
			
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}
		return null;

		
	}
	
	
	// to delete requests
	
	@DeleteMapping("/bookings/{bookingId}")
	public Long removeRequest(@PathVariable("bookingId")Long bookingId,HttpServletRequest request) throws IOException
	{
		try {
			return this.assigningBL.deleteRequest(bookingId);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}
		return null;
	}
	
	//to get all sources
	
	@GetMapping(path="/sources")
	public List<SourceBO> getAllSources(HttpServletRequest request) throws IOException
	{
		try {
			return this.assigningBL.findAllSources();
		} catch (Exception e) {
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}
		return null;
	}
	
	//to add new sources
	
	@PostMapping(path="/sources/addSources")
	public SourceBO addSource(@RequestBody SourceBO source,HttpServletRequest request) throws IOException
	{
		try {
			return this.assigningBL.addSource(source);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}
		return null;
	}
	
	//to get all destinations
	
	@GetMapping(path="/destinations")
	public List<Destination> getAllDestinations(HttpServletRequest request) throws IOException
	{
		try {
			return this.assigningBL.findAllDestinations();
		} catch (Exception e) {
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}
		return null;
	}
	
	//to add new destinations
	
		@PostMapping(path="/destinations/addDestinations")
		public Destination addDestination(@RequestBody Destination dest,HttpServletRequest request) throws IOException
		{
			try {
				return this.assigningBL.addDestination(dest);
			} catch (Exception e) {
				globalException.writeToExceptionFile(e, request);
				e.printStackTrace();
			}
			return null;
		}
		
		// To get the data count
		@GetMapping(path="/bookings/count")
		public Long getCount(HttpServletRequest request) throws IOException{
		try {
			return this.assigningBL.getCount();
		} catch (Exception e) {
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}
		return null;
		}
		
		
		// For Search
		
		@GetMapping(path="/search/{searchValue}/{skip}/{limit}")
		public List<BookingRequest> searchByName(@PathVariable(name="searchValue") String text,@PathVariable("skip") long skip,@PathVariable("limit") int limit,HttpServletRequest request ) throws IOException
		{
			try {
				Query query = new Query();
				
				query.limit(limit).skip(skip);
				
				 Criteria criteria1 = Criteria.where("status").is("Booked");
					query.addCriteria(criteria1);
					
					Criteria criteria2 = Criteria.where("employeeName").regex(text, "i");
					query.addCriteria(criteria2);
					
					return this.assigningBL.searchByName(query);
			} catch (Exception e) {
				globalException.writeToExceptionFile(e, request);
				e.printStackTrace();
			}
			return null;
		}
	


//-----------------------------------------------------------Today's Request Ends here--------------------------------------------------------//
	
//--------------------------------------------------------Assign To Cab Pop up Starts here---------------------------------------------------//
    //	 Fetch the Cab Model as list
	@GetMapping(path="cabModel")
    public ResponseEntity<Set<String>> getAllCabModel(HttpServletRequest request) throws IOException
    {
        Set<String> cabModel;
		try {
			cabModel = this.assigningBL.findAllCabModel(0);  
			return ResponseEntity.status(HttpStatus.OK).body(cabModel);
		} catch (Exception e) {
			
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}
       
		return ResponseEntity.ok(null);
      
    }
	
	// Get Cab number by cab model, destination, timeslot and selected number of employee's
    @GetMapping(path="cabNum/{cabModel}/{selected}/{destination}/{timeSlot}")
    public ResponseEntity<List<AssigningBO>> getCabNumber(@PathVariable ("cabModel") String cabModel,@PathVariable ("selected") Integer selected,
            @PathVariable ("destination") String destination,@PathVariable ("timeSlot") String timeSlot,HttpServletRequest request) throws IOException{
   
        
		try {
			List<AssigningBO> cabs = this.assigningBL.getCabNumber(cabModel, selected, destination, timeSlot);
			return ResponseEntity.status(HttpStatus.OK).body(cabs);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}

		return ResponseEntity.ok(null);
    }

	
	// To update and save in the Trip cab info , update in Booking Request table
	@PostMapping(path="save/tripInfo")
	public ResponseEntity<?> saveTrip(@RequestBody AssigningBO info,HttpServletRequest request) throws IOException {
		try {
			String result;
      boolean cancelledFlag = this.assigningBL.checkEmpStatusBeforeAssignment(info);
				
				//code to check if status is not cancelled
			
				if(cancelledFlag) {
					
					result = "Someone has cancelled their ride or a trip has been assigned already, please refresh the page and try again";
					
					
					return ResponseEntity.status(CustomStatus.INVALID).body(result);
				}
			
					
			   AssigningBO save=this.assigningBL.saveTrip(info);
					
			     ResponseEntity<?> response= ResponseEntity.status(CustomStatus.VALID).body(save);
			               
			     logFile.writeToLogFile("SaveTrip Request - ",request,info+"-"+"cab assigned",response);

				return ResponseEntity.status(CustomStatus.VALID).body(save);
		} catch (Exception e) {
			
			globalException.writeToExceptionFile(e, request);
			e.printStackTrace();
		}
	//	return ResponseEntity.status(HttpStatus.OK).body(null);
		 
		return ResponseEntity.ok(null);
		
		
		
	}

	//------------------------------Assign To Cab Pop up ends here--------------------------------//
	   

//---------------------------------------------------To find and store in DB--------------------------------------------------------//	
    // Find the CabInfo
	@GetMapping(path="cabInfo")
	public ResponseEntity<List<CabInfo>> getAllCabInfo(){
		
		List<CabInfo> cabInfo=this.assigningBL.findAllCabInfo();
		return ResponseEntity.status(HttpStatus.OK).body(cabInfo);
	}
	
    // Save CabInfo
	@PostMapping(path="save/cabInfo")
	public CabInfo addCabInfo(@RequestBody CabInfo info) {
		
		return this.assigningBL.save(info);
	}
			
	// Find all DriverInfo
	@GetMapping(path="driverInfo")
	public ResponseEntity<List<DriverInfo>> getAllDriverDetails(){
		
		List<DriverInfo> driverInfo=this.assigningBL.findAllDriverInfo();
		return ResponseEntity.status(HttpStatus.OK).body(driverInfo);
	}

	// Save DriverDetails
	@PostMapping(path="save/driverInfo")
	public DriverInfo addDriverInfo(@RequestBody DriverInfo info) {
		
		return this.assigningBL.save(info);
	}

	// Find all Employee Info
//	@GetMapping(path="employeeInfo")
//	public ResponseEntity<List<EmployeeInfo>> getAllEmployeeDetails(){
//		
//		List<EmployeeInfo> empInfo=this.assigningBL.findAllEmployeeInfo();
//		return ResponseEntity.status(HttpStatus.OK).body(empInfo);
//	}
//
//    // Save Employee Details
//	@PostMapping(path="save/employeeInfo")
//	public EmployeeInfo addEmployeeInfo(@RequestBody EmployeeInfo info) {
//			
//		return this.assigningBL.save(info);
//	}

	// Find all Booking Info
	@GetMapping(path="bookingRequest")
	public ResponseEntity<List<BookingRequest>> getAllBookingReq(){
		
		List<BookingRequest> req=this.assigningBL.findAllBookingReq();
		return ResponseEntity.status(HttpStatus.OK).body(req);
	}

    // Save Booking Details
	@PostMapping(path="save/bookingRequest")
	public BookingRequest addBookingRequest(@RequestBody BookingRequest info) {
			
		return this.assigningBL.save(info);
	}
	
	
	// Find all Trip Info
	@GetMapping(path="tripInfo")
	public ResponseEntity<List<TripCabInfo>> getAllTrips(){
		
		List<TripCabInfo> trips=this.assigningBL.findAllTrips();
		return ResponseEntity.status(HttpStatus.OK).body(trips);
	}

    // Save Trip Details
	@PostMapping(path="save/tripCabInfo")
	public TripCabInfo addBookingRequest(@RequestBody TripCabInfo info) {
		
		return this.assigningBL.save(info);
	}
	


}
