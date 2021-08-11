package com.cabApplication.admin.model;

import java.time.LocalDateTime;


import org.springframework.http.ResponseEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
public class LogMessageInfo {
	
	LocalDateTime localDateTime;
	String method;
	String requestPath;
	String restApiMethod;
	String info;
	ResponseEntity<?> response;
	
	@Override
	public String toString() {
		return "LogMessageInfo [localDateTime=" + localDateTime + ", method=" + method + ", requestPath=" + requestPath
				+ ", restApiMethod=" + restApiMethod + ", info=" + info + ", response=" + response + "]";
	}
		
	

	
	
	
		
}
