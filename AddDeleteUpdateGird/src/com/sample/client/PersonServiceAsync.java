package com.sample.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

public interface PersonServiceAsync {

	/*void addPerson(Person perons, AsyncCallback<Void> callback);*/

	

	void getAllPersons(PagingLoadConfig config,
			AsyncCallback<PagingLoadResult<Person>> callback);

}
