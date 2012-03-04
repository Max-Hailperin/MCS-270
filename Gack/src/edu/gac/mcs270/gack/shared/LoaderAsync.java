package edu.gac.mcs270.gack.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.gac.mcs270.gack.shared.domain.Person;

public interface LoaderAsync {

	void getPlayer(AsyncCallback<Person> callback);

}
