package edu.gac.mcs270.gack.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.gac.mcs270.gack.shared.domain.Person;

public interface PersistenceAsync {

	void getPlayer(String name, AsyncCallback<Person> callback);

	void savePlayer(Person player, AsyncCallback<Void> callback);

}
