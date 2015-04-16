package com.sample.client;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface PersonProperties extends PropertyAccess<Person> {
	/*ModelKeyProvider<Person> id();*/

	ValueProvider<Person, Integer> id();
	
	ValueProvider<Person, String> fname();

	ValueProvider<Person, String> lname();

	ValueProvider<Person, String> phone();

	ValueProvider<Person, String> emailId();

}
