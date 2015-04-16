package com.sample.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Person implements IsSerializable {
	
	private int id;
	private String fname;
	private String lname;
	private String phone;
	private String emailId;
	
	public Person() {
		super();
	}

	public Person(int id, String fname, String lname, String phone,
			String emailId) {
		super();
		this.id = id;
		this.fname = fname;
		this.lname = lname;
		this.phone = phone;
		this.emailId = emailId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	

}
