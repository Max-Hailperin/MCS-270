package edu.gac.mcs270.gack.server;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.apphosting.api.ApiProxy;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.gac.mcs270.gack.shared.Persistence;
import edu.gac.mcs270.gack.shared.domain.Person;

public class PersistenceImpl extends RemoteServiceServlet implements Persistence {
	private static final long serialVersionUID = 7649793065322010303L;

	@PersistenceCapable(identityType=IdentityType.APPLICATION)
	private static class SavedPlayer {
		@SuppressWarnings("unused")
		@PrimaryKey
		private String emailAndName;

		@Persistent(serialized = "true")
		private Person player;

		public SavedPlayer(String emailAndName, Person player) {
			super();
			this.emailAndName = emailAndName;
			this.player = player;
		}

		public Person getPlayer() {
			return player;
		}
	}

	public Person getPlayer(String name){
		String emailAndName = getEmailAndName(name);
		PersistenceManager persistenceManager = PMF.get().getPersistenceManager();
		try{
			return persistenceManager.getObjectById(SavedPlayer.class, emailAndName).getPlayer();
		} catch(JDOObjectNotFoundException e){
			return null;
		}
	}

	public void savePlayer(Person player){
		String emailAndName = getEmailAndName(player.getName()); 
	    SavedPlayer sp = new SavedPlayer(emailAndName, player);
	    PMF.get().getPersistenceManager().makePersistent(sp);
	}
	
	private String getEmailAndName(String name){
		// The test below just makes sure we didn't miss the lines in web.xml that
		// ensure the user has to be logged in.  If this exception is thrown, web.xml
		// needs fixing.
		if(!ApiProxy.getCurrentEnvironment().isLoggedIn()){
			throw new IllegalStateException("not logged in");
		}
		// In the same way, ApiProxy.getCurrentEnvironment().isAdmin() could be used to
		// determine whether the currently logged in user is known to AppEngine as an
		// administrator of this application.  I can't think of any reason to demonstrate
		// thatin the Land of Gack.
		return ApiProxy.getCurrentEnvironment().getEmail() + ":" + name;
	}

	public PersistenceImpl() {
		super();
	}
}

