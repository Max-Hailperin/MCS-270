package edu.gac.mcs270.gack.shared.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gac.mcs270.gack.client.Utility;

public class Place implements Serializable {
	
	private static final long serialVersionUID = 4262130263204312985L;
	private String name;
	private Map<String, Place> neighborMap;
	private List<Thing> contents;
	private List<Person> occupants;
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public List<Thing> getContents() { return contents; }
	public List<Person> getOccupants() { return occupants; }

	public Place(String name) {
		super();
		this.name = name;
		this.neighborMap = new HashMap<String, Place>();
		this.contents = new ArrayList<Thing>();
		this.occupants = new ArrayList<Person>();
	}
	
	protected Place(){}
	
	public List<String> exits() {
		return new ArrayList<String>(neighborMap.keySet());
	}
	
	public List<Place> neighbors() {
		return new ArrayList<Place>(neighborMap.values());
	}
	
	public Place neighborTowards(String direction) {
		return neighborMap.get(direction);
	}
	
	public void addNewNeighbor(String direction, Place newNeighbor) {
		if (exits().contains(direction)) {
			Utility.displayMessage("there is already a neighbor " + direction + " from " + this);
		} else {
			neighborMap.put(direction, newNeighbor);
		}
	}
	
	public void gain(Thing newItem) {
		if (contents.contains(newItem)) {
			Utility.displayMessage(newItem + " is already at " + this);
		} else {
			contents.add(newItem);
		}
	}
	
	public void gain(Person newPerson) {
		if (occupants.contains(newPerson)) {
			Utility.displayMessage(newPerson + " is already at " + this);
		} else {
			occupants.add(newPerson);
		}
	}
	
	public void lose(Thing item) {
		if (contents.contains(item)) {
			contents.remove(item);
		} else {
			Utility.displayMessage(item + " is not at " + this);
		}
	}
	
	public void lose(Person person) {
		if (occupants.contains(person)) {
			occupants.remove(person);
		} else {
			Utility.displayMessage(person + " is not at " + this);
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
}
