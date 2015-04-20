package com.sample.controller;

import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sample.common.RestURIConstants;
import com.sample.services.IEmployeeService;

@RestController
public class EmployeeController {

	@Autowired
	private IEmployeeService employeeService;

	@RequestMapping(value = RestURIConstants.GET_ALL_EMPLOYEES, method = RequestMethod.GET)
	@ResponseBody
	public Response getAllEmloyees() {
		List<Employee> employees = employeeService.getAllEmployees();
		return Response.ok().entity(employees).build();
	}
	
}
