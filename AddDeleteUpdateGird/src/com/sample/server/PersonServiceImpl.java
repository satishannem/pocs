package com.sample.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sample.client.Person;
import com.sample.client.PersonService;
import com.sample.shared.PersonData;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

public class PersonServiceImpl extends RemoteServiceServlet implements
		PersonService,IsSerializable{
	private static final long serialVersionUID = -2264545082005119810L;


		public PersonServiceImpl() {
			
		}
		@Override
		public PagingLoadResult<Person> getAllPersons(final PagingLoadConfig config) {
			
			PagingLoadResult<Person> page = new PersonData();
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
		

	
/*
	@Override
	public void addPerson(Person perons) {
		// TODO Auto-generated method stub
	}*/

	

}
