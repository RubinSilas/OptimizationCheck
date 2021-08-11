package com.cabApplication.admin.exception.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.cabApplication.admin.model.ErrorDetails;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
   
	LocalDate currentDate=LocalDate.now();
//	File errorFile= new File("D:/logger/"+currentDate+"-"+"ADMIN"+" - Exception"+".txt");
	
	File errorFile=new File(currentDate+"-"+"ADMIN"+" - Exception"+".txt");
	

	@ExceptionHandler(value = Exception.class)
	public ErrorDetails handleAllExceptions(Exception ex,WebRequest request) throws IOException
	{		
		return new ErrorDetails(LocalDateTime.now(),ex.getMessage(),request.getDescription(false), null, null);
		
	}
	
	
	public boolean writeToExceptionFile(Exception ex,HttpServletRequest request) throws IOException
	{
	boolean result=false;
	try(PrintWriter writer=new PrintWriter(new FileWriter(errorFile,true)))
	{
	
	ErrorDetails errorDetails=new ErrorDetails(LocalDateTime.now(),ex.getMessage(),request.getMethod(),request.getRequestURI(),ex.getClass().descriptorString());
		

	writer.println(errorDetails);
	result=true;
	}catch(Exception e)
	{
	e.printStackTrace();
	}
	return result;
	}

}
