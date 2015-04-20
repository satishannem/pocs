package com.sample.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.sample.common.DBConnection;
import com.sample.controller.Employee;

@Component
public class EmployeeMgmtDao {
	
	private Session session;
	public List<Employee> getAllEmployees(){
		session = DBConnection.getInstance().getSession();
		String query ="FROM Employee";
		Query q= session.createQuery(query);
		List<Employee> list = q.list();
		DBConnection.getInstance().closeSession(session);
		return list;	
		
	}
}
