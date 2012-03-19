package edu.gac.mcs270.gack.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.gac.mcs270.gack.shared.domain.Person;

@RemoteServiceRelativePath("persistence")
public interface Persistence extends RemoteService {
	Person getPlayer(String name);
	void savePlayer(Person player);
}
