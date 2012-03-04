package edu.gac.mcs270.gack.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.gac.mcs270.gack.shared.domain.Person;

@RemoteServiceRelativePath("loader")
public interface Loader extends RemoteService {
	Person getPlayer();
}
