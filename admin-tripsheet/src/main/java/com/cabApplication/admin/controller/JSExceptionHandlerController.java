package com.cabApplication.admin.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabApplication.admin.exception.handler.GlobalExceptionHandler;


@RestController
@RequestMapping(path = "/admin")
public class JSExceptionHandlerController {
	
	@Autowired
	GlobalExceptionHandler globalExceptionHandler;
	
	@PutMapping(path = "/jsExceptions/{jsException}")
	public void handleJsException(@PathVariable("jsException") Exception jsException,HttpServletRequest request) throws IOException {
		
		globalExceptionHandler.writeToExceptionFile(jsException, request);
	}
	

}
