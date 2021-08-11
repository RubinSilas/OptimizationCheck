package com.cabApplication.admin.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabApplication.admin.businessLayer.BusinessLayer;
import com.cabApplication.admin.dataLayer.ComplaintsService;
import com.cabApplication.admin.entity.BookingRequest;
import com.cabApplication.admin.entity.Destination;
import com.cabApplication.admin.entity.DriverInfo;
import com.cabApplication.admin.entity.Employee;
import com.cabApplication.admin.entity.SourceBO;
import com.cabApplication.admin.entity.TripCabInfo;
import com.cabApplication.admin.entity.UserComplaintsBO;
import com.cabApplication.admin.exception.handler.GlobalExceptionHandler;
import com.cabApplication.admin.logger.LogFileCreation;
import com.cabApplication.admin.repository.BookingRequestRepository;
import com.cabApplication.admin.repository.DestinationRepository;
import com.cabApplication.admin.repository.DriverInfoRepository;
import com.cabApplication.admin.repository.EmployeeRepository;
import com.cabApplication.admin.repository.SourceRepository;
import com.cabApplication.admin.repository.TripCabInfoRepository;

@RestController
@RequestMapping(path="/api/v1")
public class ComplaintsController {
     @Autowired
     BusinessLayer BL;
     
     @Autowired
     MongoTemplate template;
     
     @Autowired
 	BookingRequestRepository repo;
 	
 	@Autowired
 	DriverInfoRepository repo1;
 	
 	@Autowired
 	EmployeeRepository empRepo;
 	
 	@Autowired
 	SourceRepository srcRepo;
 	
 	@Autowired
 	DestinationRepository destRepo;
 	
 	@Autowired
 	ComplaintsService service;
 	
 	
 	@Autowired
 	TripCabInfoRepository tripRepo;
     

	@Autowired
	GlobalExceptionHandler globalException;
	
	@Autowired
	LogFileCreation logFile;
	
	
     
    
     
// -------------------------***----------------------------------------// 
	
    // FindAll method--->starts
    //Find by Complaintsdesp
   @GetMapping(path="/complaints/complaintsdesp")

     public ResponseEntity<List<UserComplaintsBO>> getComplaintsScreen(HttpServletRequest request) throws IOException{
	
	   try {
		List<UserComplaintsBO> trip=  this.BL.getComplaintsScreen();
		   
		return  ResponseEntity.status(HttpStatus.OK).body(trip);
	} catch (Exception e) {
		globalException.writeToExceptionFile(e,request);
		e.printStackTrace();
	}
	return null;
	}
// -------------------------***----------------------------------------// 
 //@PutMapping annotation is used to map "/booking/{id}"Http PUT  request onto a specific handler method-update remark
   @PutMapping(path = "/remarks/{bookingId}/{remark}")
   public ResponseEntity<BookingRequest> updateRemark(@PathVariable("bookingId") long bookingId,@PathVariable("remark") String remark,HttpServletRequest request) throws IOException{
	   try {
		BookingRequest req = this.BL.updateRemark(bookingId,remark);
		
		ResponseEntity<?> response= ResponseEntity.status(HttpStatus.OK).body(req);
        
        logFile.writeToLogFile("updateRemark Request - ",request,bookingId+"-"+" remarks added ",response);

		
		   return  ResponseEntity.status(HttpStatus.OK).body(req);
	} catch (Exception e) {
		globalException.writeToExceptionFile(e,request);
		e.printStackTrace();
	}
	return null;
   }
   
// -------------------------***----------------------------------------//   
 // Scroll method with MongoTemplate   
//  @GetMapping(path="/scroll/{skip}/{limit}")
//  public List<BookingRequest> getByScroll(@PathVariable("skip") long skip,@PathVariable("limit") int limit){
// 	 Query query = new Query();
// 	 query.limit(limit).skip(skip);
// 	 
// 	 List<BookingRequest> cabs=this.template.find(query, BookingRequest.class,"BookingRequest");
// 	 return cabs;
//  }
//-------------------------***----------------------------------------// 
//  //To get the data count
	@GetMapping(path = "/countComplaints")
	public ResponseEntity<Long> getCount(HttpServletRequest request) throws IOException {
		try {
			Long count = this.BL.getCount();
			return ResponseEntity.status(HttpStatus.OK).body(count);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
	}
  
 

	@GetMapping(path = "/source")
	public ResponseEntity<List<SourceBO>> getSource(HttpServletRequest request) throws IOException{
		
		try {
			List<SourceBO> sources= this.BL.getSource();
			return ResponseEntity.status(HttpStatus.OK).body(sources);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
		
	}
 
	@GetMapping(path = "/destination")
	public ResponseEntity<List<Destination>> getDestination(HttpServletRequest request) throws IOException{
		
		try {
			List<Destination> destinations= this.BL.getDestination();
			return ResponseEntity.status(HttpStatus.OK).body(destinations);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
		
	}

 
////-------------------**********---------------------------------------
//	//filter
//	
	@GetMapping(path = "/filterComplaints/{source}/{destination}/{timeSlot}")
	public ResponseEntity<List<UserComplaintsBO>> filter(@PathVariable ("source") String source,@PathVariable ("destination") String destination,@PathVariable ("timeSlot") String timeSlot,HttpServletRequest request) throws IOException{
		
		
		try {
			Query dynamicQuery = new Query();
			if (!(source.equals("0"))) {
				Criteria sourceCriteria = Criteria.where("source").is(source);
				dynamicQuery.addCriteria(sourceCriteria);
			}
			if (!(destination.equals("0"))) {
				Criteria destinationCriteria = Criteria.where("destination").is(destination);
				dynamicQuery.addCriteria(destinationCriteria);

			}
			
			Criteria criteria1 = Criteria.where("complaintDescription").ne(null);
			Criteria criteria2= Criteria.where("remarks").is(null);
			Criteria criteria3= new Criteria();
			criteria3.andOperator(criteria1,criteria2);
			dynamicQuery.addCriteria(criteria3);
			
			List<UserComplaintsBO> uc =new ArrayList<>();
			//get complaintsdescription from bookingrepository
			List<BookingRequest> bookings =  this.template.find(dynamicQuery, BookingRequest.class);
				Long empNumbers = null;
				String cabNumbers = null;
				LocalDate dates = null;
				String driver = null;
			 	
			  for(BookingRequest eachBook:bookings) {
			// By using employeeId getting
			  Optional<Employee> empOp = this.empRepo.findById(eachBook.getEmployeeId());
					
			  Employee emp  = empOp.get();
					
			  empNumbers=emp.getPhoneNumber();
					
			  Optional<TripCabInfo> tripCabOp = this.tripRepo.findById(eachBook.getTripCabId());
					
			  TripCabInfo trip = tripCabOp.get();
				
			  cabNumbers=trip.getCabNumber();
				
				dates=trip.getDateOfTravel();
					Optional<DriverInfo> driverOp = this.repo1.findById(trip.getDriverId());
					 driver = driverOp.get().getDriverName();
					
				
					 UserComplaintsBO userComplaints = new UserComplaintsBO();
					 userComplaints.setDatesOfTravel(dates);
					 userComplaints.setDriverName(driver);
					 userComplaints.setEmpNumbers(empNumbers);
					 userComplaints.setCabNumbers(cabNumbers);
					 userComplaints.setRequests(eachBook);
					 
					 uc.add(userComplaints);
					
				}

			  if (!(timeSlot.equals("0"))) {

					LocalTime lt = LocalTime.parse(timeSlot);
					List<UserComplaintsBO> timeFilter = uc.stream().filter(e -> e.getRequests().getTimeSlot().equals(lt))
							.collect(Collectors.toList());
					
					return ResponseEntity.status(HttpStatus.OK).body(timeFilter);
				}
			
			return  ResponseEntity.status(HttpStatus.OK).body(uc);
		} catch (Exception e) {
			globalException.writeToExceptionFile(e,request);
			e.printStackTrace();
		}
		return null;
		
	}
// 
//   
//
////-----------------******--------------------------//
//
////search method starts here
@GetMapping(path = "searchComplaint/{searchValue}/{skip}/{limit}")
public ResponseEntity<List<UserComplaintsBO>> getByTextSearch(@PathVariable("searchValue") String text,
		@PathVariable("skip") long skip, @PathVariable("limit") int limit,HttpServletRequest request) throws IOException{
	
	try {
		List<UserComplaintsBO> uc = this.BL.getBySearch(text, skip, limit);
		return ResponseEntity.status(HttpStatus.OK).body(uc);
	} catch (Exception e) {
		globalException.writeToExceptionFile(e,request);
		e.printStackTrace();
	}
	return null;
}
////search method ends here
////--------------------********----------------------------------












}
