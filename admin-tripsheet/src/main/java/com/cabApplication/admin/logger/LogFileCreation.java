package com.cabApplication.admin.logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.http.HttpRequest;

import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import com.cabApplication.admin.controller.AdminLoginController;
import com.cabApplication.admin.entity.Employee;
import com.cabApplication.admin.model.LogMessageInfo;

//package com.example.demo.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service
public class LogFileCreation {
	
	 LocalDate currentDate= LocalDate.now();
	 
	 final static Logger log = LoggerFactory.getLogger(LogFileCreation.class);
	 
	 File logfile=new File("D:/logger/"+currentDate+"-"+"Admin"+" - Log"+".txt");
	 //	File logFile=new File(currentDate+"-"+"ADMIN"+" - Log"+".txt");
	 
	 //File logfile=new File(currentDate+"-"+"Admin"+".txt");
	public void logFileCreation() {
		
		boolean result;  
		try   
		{  
		result = logfile.createNewFile();  //creates a new file  
		if(result)      // test if successfully created a new file  
		{  
		System.out.println("file created "+logfile.getCanonicalPath()); //returns the path string  
		}  
		else  
		{  
		System.out.println("File already exist at location: "+logfile.getCanonicalPath());  
		}  
		}   
		catch (IOException e)   
		{  
		e.printStackTrace();    //prints exception if any  
		}         
		}  
	



public boolean writeToLogFile(String method,HttpServletRequest request,String logInfo,ResponseEntity<?> response) throws IOException
{
	
	boolean result=false;
	try(PrintWriter writer=new PrintWriter(new FileWriter(logfile,true)))
	{
		writer.println(new LogMessageInfo(LocalDateTime.now(),method,request.getRequestURI(),request.getMethod(),logInfo,response));
		
		result=true;
	}catch(Exception e)
	{
		e.printStackTrace();
	}
	return result;	
}


}	
	

