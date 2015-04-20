package com.sample.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sample.controller.Employee;
import com.sample.dao.EmployeeMgmtDao;

@Controller
public class EmployeeServiceImpl implements IEmployeeService {

	private EmployeeMgmtDao employeeMgmtDao;

	@Override
	public List<Employee> getAllEmployees() {
		return employeeMgmtDao.getAllEmployees();
	}

	@Override
	public Employee getEmployeesByLocation(String location) {

		return null;
	}

}
