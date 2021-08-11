package com.cabApplication.admin.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cabApplication.admin.businessLayer.AdminLoginBL;
import com.cabApplication.admin.businessLayer.AdminLoginBL2;
import com.cabApplication.admin.entity.Employee;
import com.cabApplication.admin.entity.EmployeeBO;
import com.cabApplication.admin.exception.handler.GlobalExceptionHandler;
import com.cabApplication.admin.logger.LogFileCreation;
import com.microsoft.aad.adal4j.AuthenticationException;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;

@RestController
@CrossOrigin("*")
//@RequestMapping("/admin")
public class AdminLoginController {

	
	@Autowired
	GlobalExceptionHandler globalException;
	
	@Autowired
	LogFileCreation logFile;
	
	@Autowired
	private AdminLoginBL loginBl;
	
	@Autowired
	private AdminLoginBL2 loginBl2;
	
	@PostMapping(path = "/authenticate")
	public ResponseEntity<?> validateUser(@RequestBody EmployeeBO employee, HttpServletRequest request) throws IOException {
		try {
//			return ResponseEntity.ok(loginBl.validateUser(employee));
			
//			String jwt = loginBl.validateUser(employee);
			
			String jwt = loginBl.validateUser(employee);
			
			request.getSession().setAttribute("Authorization", jwt);
			request.getSession().setAttribute("Admin", employee.getEmployeeMail());
			
			MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
			headerMap.put("Authorization", Arrays.asList(jwt));

			Employee emp = loginBl.getEmployeeDetails(employee.getEmployeeMail());
		
			ResponseEntity<?> response=new ResponseEntity<>(emp,HttpStatus.OK);
	           
            logFile.writeToLogFile("ValidateUser Request - ",request,employee+"-"+" user logged in",response);
		
			return new ResponseEntity<>(emp, headerMap, HttpStatus.OK);

		} catch(AccessDeniedException e) {
			
			globalException.writeToExceptionFile(e,request);
			return ResponseEntity.status(560).body(e.getLocalizedMessage());
			
		} catch(ExecutionException | BadCredentialsException | AuthenticationException e) {
		//logger.info("Bad Credentials",e.getMessage());
			
			globalException.writeToExceptionFile(e,request);
			return ResponseEntity.status(560).body("Bad Credentials");
		
		} catch (Exception e) {
			//log
			globalException.writeToExceptionFile(e,request);
		} 
		
		return ResponseEntity.ok(null);
	}
}
