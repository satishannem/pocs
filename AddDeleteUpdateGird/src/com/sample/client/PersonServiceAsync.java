package com.sample.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

public interface PersonServiceAsync {

	void getAllPersons(PagingLoadConfig config,
			AsyncCallback<PagingLoadResult<Person>> callback);
	 
	void addPerson(Person person, AsyncCallback<Void> callback);
	
	void deletePerson(int id, AsyncCallback<Void> callback);
	  

}
