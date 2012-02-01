package edu.gac.mcs270.gack.client;

import edu.gac.mcs270.gack.client.domain.Person;

public class World {
	
	private Person player;
	
	public Person getPlayer() {
		return player;
	}

	protected void setPlayer(Person player) {
		this.player = player;
	}

	public World() {}
}
