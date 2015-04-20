package com.sample.services;

import java.util.List;



import com.sample.controller.Employee;

public interface IEmployeeService {
	
	List<Employee> getAllEmployees();
	Employee getEmployeesByLocation(String location);

}
