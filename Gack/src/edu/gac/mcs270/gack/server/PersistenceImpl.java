package edu.gac.mcs270.gack.server;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.gac.mcs270.gack.shared.Persistence;
import edu.gac.mcs270.gack.shared.domain.Person;

public class PersistenceImpl extends RemoteServiceServlet implements Persistence {
	private static final long serialVersionUID = 7649793065322010303L;

	@PersistenceCapable(identityType=IdentityType.APPLICATION)
	private static class SavedPlayer {
		@SuppressWarnings("unused")
		@PrimaryKey
		private String name;

		@Persistent(serialized = "true")
		private Person player;

		public SavedPlayer(String name, Person player) {
			super();
			this.name = name;
			this.player = player;
		}

		public Person getPlayer() {
			return player;
		}
	}

	public Person getPlayer(String name){
		PersistenceManager persistenceManager = PMF.get().getPersistenceManager();
		try{
			return persistenceManager.getObjectById(SavedPlayer.class, name).getPlayer();
		} catch(JDOObjectNotFoundException e){
			return null;
		}
	}

	public void savePlayer(Person player){
	    SavedPlayer sp = new SavedPlayer(player.getName(), player);
	    PMF.get().getPersistenceManager().makePersistent(sp);
	}

	public PersistenceImpl() {
		super();
	}
}

