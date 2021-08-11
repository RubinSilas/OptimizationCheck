package com.cabApplication.admin.dataLayer;

import com.cabApplication.admin.entity.Employee;
import com.cabApplication.admin.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminLoginDL {

	@Autowired
	private EmployeeRepository employeeRepo;
	
	public Employee findEmployeeByMail(String email) {
		return this.employeeRepo.findByEmployeeMail(email);
	}
	
}
