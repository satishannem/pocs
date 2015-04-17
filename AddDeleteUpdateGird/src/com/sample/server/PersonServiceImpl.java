package com.sample.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sample.client.Person;
import com.sample.client.PersonService;
import com.sample.shared.DBConnection;
import com.sample.shared.PersonData;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

public class PersonServiceImpl extends RemoteServiceServlet implements
		PersonService,IsSerializable{
	private static final long serialVersionUID = -2264545082005119810L;

		PagingLoadResult<Person> page = new PersonData();
		public PersonServiceImpl() {
			
		}
		@Override
		public PagingLoadResult<Person> getAllPersons(final PagingLoadConfig config) {
			
		
			List<Person> list= page.getData();
			List<Person> sublist = new ArrayList<Person>();
					        int start = config.getOffset();
			        int limit = list.size();
			        if (config.getLimit() > 0) {
			            limit = Math.min(start + config.getLimit(), limit);
			        }
			        for (int i = config.getOffset(); i < limit; i++) {
			            sublist.add(list.get(i));
			        }
			
			return new PagingLoadResultBean<Person>(sublist, list.size(), config.getOffset());

	}
		@Override
		public void addPerson(Person person) {
			Connection con = DBConnection.getInstance().getConnection();
			String sql="insert into persondetails values(?,?,?,?,?)";
			try {
				PreparedStatement preparedStatement = con.prepareStatement(sql);
				preparedStatement.setInt(1, person.getId());
				preparedStatement.setString(2, person.getFname());
				preparedStatement.setString(3, person.getLname());
				preparedStatement.setString(4, person.getPhone());
				preparedStatement.setString(5, person.getEmailId());
				
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DBConnection.getInstance().closeConecction(con);
			
		}
		public void deletePerson(int id) {
			Connection con = DBConnection.getInstance().getConnection();
			String sql="delete from persondetails where id = ?";
			try {
				PreparedStatement preparedStatement = con.prepareStatement(sql);
				preparedStatement.setInt(1, id);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DBConnection.getInstance().closeConecction(con);
			
		}
}
