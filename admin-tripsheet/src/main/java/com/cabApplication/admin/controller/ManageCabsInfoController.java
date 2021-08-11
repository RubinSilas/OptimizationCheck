package com.cabApplication.admin.controller;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;


import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cabApplication.admin.businessLayer.ManageCabsInfoBL;
import com.cabApplication.admin.dataLayer.ManageCabsInfoDL;
import com.cabApplication.admin.entity.CabInfo;
import com.cabApplication.admin.entity.DriverInfo;
import com.cabApplication.admin.exception.handler.GlobalExceptionHandler;
import com.cabApplication.admin.logger.LogFileCreation;
import com.cabApplication.admin.repository.ManageCabsInfoRepository;
import com.cabApplication.admin.status.ManageCabsResponseStatus;


@RestController

public class ManageCabsInfoController {
	 
	@Autowired
	ManageCabsInfoBL cabInfoBl;

	@Autowired
	ManageCabsInfoDL cabInfoDl;
	
	@Autowired
	ManageCabsInfoRepository cabRepo;
	
	@Autowired
	GlobalExceptionHandler globalException;
	
	@Autowired
	LogFileCreation logFile;
	
		//call a business layer method to get List of cabModels and return unique set of cab models
		@GetMapping(path="/all/cabModel")
	    public ResponseEntity<Set<String>> getAllCabModels( HttpServletRequest request) throws IOException
	    {
	    	try {
				List<CabInfo> cabInfo =this.cabInfoBl.getAllCabModels(0);
 
				Set<String> cabModel=new HashSet<>();
				
				for(CabInfo eachCab:cabInfo) {
					
					String eachCabModel=eachCab.getCabModel();
					
						cabModel.add(eachCabModel);
				}
				
				return ResponseEntity.status(HttpStatus.OK).body(cabModel);
			} catch (Exception e) {
				globalException.writeToExceptionFile(e,request);
				e.printStackTrace();
			}
			return null;
	    }
	
				
		// call a business layer method to get & return list of driver details
		@GetMapping(path="/all/driverInfo")
	    
	    public ResponseEntity<List<DriverInfo>> getAllDrivers(HttpServletRequest request) throws IOException{
	        	
	        try {
				List<DriverInfo> driverInfo =this.cabInfoBl.getAllDrivers();
				
				return ResponseEntity.status(HttpStatus.OK).body(driverInfo);
			} catch (Exception e) {
				globalException.writeToExceptionFile(e,request);
				e.printStackTrace();
			}
			return null;
	    }
	
		
		
		//call a BL method to get & return list of cab details which is not deleted by the user
	    @GetMapping(path="/all/cabInfo")
	    
	    public ResponseEntity<List<CabInfo>> getAllCabDetails(HttpServletRequest request) throws IOException{
	    	
	    	try {
				List<CabInfo> cabInfo =this.cabInfoBl.getAllCabDetails();
				
				return ResponseEntity.status(HttpStatus.OK).body(cabInfo);
			} catch (Exception e) {
				globalException.writeToExceptionFile(e,request);
				e.printStackTrace();
			}
			return null;
	    }
	        	    
	    
	    //call a BL method to  add new cab into the database
	    @PostMapping(path="/addCabInfo")
	    public ResponseEntity<CabInfo> addCabDetails(@RequestBody CabInfo cabInfo,HttpServletRequest request) throws IOException{
	    	
	    	try {
				CabInfo reqCab = cabInfo;
				reqCab.setIsDeleted(0);
				
				CabInfo createdBy=cabInfo;
				createdBy.setCreatedBy("Admin");
				
				CabInfo createdDate=cabInfo;
				createdDate.setCreatedDate(LocalDateTime.now());
				
				
				//call a BL method to get cab details if cab number already have cab details
				String cabNumber = cabInfo.getCabNumber();
				Optional<CabInfo> entityCabNum=cabInfoBl.getCabNumber(cabNumber);

				
				//call a business layer method to check if the insurance number is already given to any cab
				boolean isInsuranceAvailable=cabInfoBl.getInsuranceNum(cabInfo);
				
				
				//call a business layer method to check if the driver has already been assigned a cab
				boolean isDriverAvailable = cabInfoBl.isDriverAvailable(cabInfo);
				
				CabInfo saveCabInfo = null;
				
				if(entityCabNum.isPresent()) {
					
					//"CabNumber already exist"
					return  ResponseEntity.status(ManageCabsResponseStatus.CABNUMBEREXIST).body(null);
					
				}
				
				else if(isInsuranceAvailable)
				{
					//"Insurance Number already exist"

					return  ResponseEntity.status(ManageCabsResponseStatus.INSURANCENUMBEREXIST).body(null);
					
				}
				
				else {
				
					if(isDriverAvailable)
						saveCabInfo=this.cabInfoDl.addCabInfo(cabInfo);
						
					else {
						
						//"Driver already assigned a cab";			
						return  ResponseEntity.status(ManageCabsResponseStatus.DRIVEREXIST).body(null);
					}
					return ResponseEntity.status(HttpStatus.CREATED).body(saveCabInfo);
				}
			} catch (Exception e) {
			
				globalException.writeToExceptionFile(e,request);
				e.printStackTrace();
			}
			return null;
	    	 	
	    }
	    
	    //call a BL method to update and return the cab detail
		//@PutMapping(path="/updateCabInfo")
		@PutMapping(path="/updateCabInfo/{cabNumber}")
		public ResponseEntity<CabInfo> editCabDetails(@PathVariable("cabNumber") String cabNumber,@RequestBody CabInfo updateCabInfo,HttpServletRequest request) throws IOException
		{
			try {
				CabInfo cab = this.cabRepo.findByCabNumber(cabNumber).get();
				
				int isDeleted=cab.getIsDeleted();
				String createdBy=cab.getCreatedBy();
				LocalDateTime createdTime=cab.getCreatedDate();
				
				CabInfo isDeletedUpdate = updateCabInfo;
				isDeletedUpdate.setIsDeleted(isDeleted);
				
				CabInfo createdByUpdate = updateCabInfo;
				createdByUpdate.setCreatedBy(createdBy);
				
				CabInfo createdDateUpdate = updateCabInfo;
				createdDateUpdate.setCreatedDate(createdTime);
				
				CabInfo modifiedBy=updateCabInfo;
				modifiedBy.setModifiedBy("Admin");
				
				CabInfo modifiedDate=updateCabInfo;
				modifiedDate.setModifiedDate(LocalDateTime.now());
				
							
				//call a business layer method to check if the insurance number is already given to any cab
				boolean isInsuranceAvailable=cabInfoBl.getInsuranceNum(updateCabInfo);
				
					    	
				//call a business layer method to check if the driver has already been assigned a cab
				boolean isDriverAvailable = cabInfoBl.isDriverAvailable(updateCabInfo);
				
				CabInfo saveCabInfo = null;

				if(isInsuranceAvailable) {
					// insurance number already exist
					return  ResponseEntity.status(ManageCabsResponseStatus.INSURANCENUMBEREXIST).body(null);	
				}
				else 
				{
					if(isDriverAvailable) {
						saveCabInfo=this.cabInfoDl.updateCabDetails(updateCabInfo);
					}else {
						// driver already assign to a cab
						return  ResponseEntity.status(ManageCabsResponseStatus.DRIVEREXIST).body(null);	
					}
					
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(saveCabInfo);
}
			} catch (Exception e) {

				globalException.writeToExceptionFile(e,request);
				e.printStackTrace();
			}
			return null;
	}    	
	    
	   
	    // call a BL method to delete the cab detail
	    @PutMapping(path="/deleteCabInfo/{cabNumber}")
	    public ResponseEntity<CabInfo> deleteCab(@PathVariable("cabNumber") String cabNumber,HttpServletRequest request) throws IOException
	    {   	
	    		
	    	try {
				CabInfo cabInfo=this.cabInfoBl.deleteCab(cabNumber);
				
				return ResponseEntity.status(HttpStatus.OK).body(cabInfo);
			} catch (Exception e) {

				globalException.writeToExceptionFile(e,request);
				e.printStackTrace();
			}
			return null;
	    }
	    
	    

	    
	    
}
