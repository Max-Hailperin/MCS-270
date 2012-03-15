package edu.gac.mcs270.gack.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.gac.mcs270.gack.shared.Loader;
import edu.gac.mcs270.gack.shared.domain.Person;

import edu.gac.mcs270.utilities.SpreadsheetDrivenInstantiator;

public class LoaderImpl extends RemoteServiceServlet implements Loader {
	private static final long serialVersionUID = -282279873378400604L;

	public Person getPlayer(){
		SpreadsheetDrivenInstantiator sdi = new SpreadsheetDrivenInstantiator(
				"0Av6ZjRxtPYJqdHI3aEtxR3lUeEtMeDNQcnpKZDZvbGc",
				"edu.gac.mcs270.gack.shared.domain");
		return sdi.getInstance(Person.class, "player");
	}

	public LoaderImpl() {
		super();
	}
}

