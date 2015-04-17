package com.sample.shared;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.sample.client.Person;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

public class PersonData implements IsSerializable, PagingLoadResult<Person> {
	private static final long serialVersionUID = 1135881380245850443L;
	private int offset = 1;
	private int totalLength = 0;
	List<Person> list = new ArrayList<Person>();

	@Override
	public List<Person> getData() {
		List<Person> person = new ArrayList<Person>();
		Connection con = DBConnection.getInstance().getConnection();
		String query = "select id,fname,lname,phone,email from persondetails";
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Person  p = new Person();
				p.setId(rs.getInt("id"));
				p.setFname(rs.getString("fname"));
				p.setLname(rs.getString("lname"));
				p.setPhone(rs.getString("phone"));
				p.setEmailId(rs.getString("email"));
				
				person.add(p);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBConnection.getInstance().closeConecction(con);
		return person;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public int getTotalLength() {

		return totalLength;
	}

	@Override
	public void setOffset(int offset) {
		this.offset = offset;

	}

	@Override
	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;

	}
	
	
}
