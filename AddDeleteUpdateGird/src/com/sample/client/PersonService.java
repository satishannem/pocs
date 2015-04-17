package com.sample.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

@RemoteServiceRelativePath("person")
public interface PersonService extends RemoteService {
	PagingLoadResult<Person> getAllPersons(PagingLoadConfig config);

	void addPerson(Person person);
	void deletePerson(int id);

}
