package edu.gac.mcs270.gack.shared.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.gac.mcs270.gack.client.Utility;

public class Person implements Serializable {

	private static final long serialVersionUID = -7660549827532417321L;
	private String name;
	private Place place;
	private List<Thing> possessions;

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public Place getPlace() { return place; }
	public List<Thing> getPossessions() { return possessions; }

	public Person(String name, Place place) {
		super();
		this.name = name;
		this.place = place;
		this.possessions = new ArrayList<Thing>();
		place.gain(this);
	}


	protected Person(){}

	public void say(String text) {
		Utility.displayMessage("At " + place + ": " + this + " says -- " + text);
	}

	public List<Thing> otherThingsAtSamePlace() {
		List<Thing> otherThingsAtSamePlace = new ArrayList<Thing>();
		for (Thing thing : place.getContents()) {
			if (!possessions.contains(thing)) {
				otherThingsAtSamePlace.add(thing);
			}
		}
		return otherThingsAtSamePlace;
	}

	public List<Person> otherPeopleAtSamePlace() {
		List<Person> otherPeopleAtSamePlace = new ArrayList<Person>();
		for (Person occupant : place.getOccupants()) {
			if (occupant != this) {
				otherPeopleAtSamePlace.add(occupant);
			}
		}
		return otherPeopleAtSamePlace;
	}

	public void lookAround() {
		say("I see " + Utility.verbalizeList(otherPeopleAtSamePlace(), "no people") +
				" and " + Utility.verbalizeList(otherThingsAtSamePlace(), "no objects") +
				" and can go " + Utility.verbalizeList(place.exits(), "nowhere"));
	}

	public void listPossessions() {
		say("I have " + Utility.verbalizeList(possessions, "nothing"));
	}

	public void read(Scroll scroll) {
		if (scroll.getOwner() == this) {
			scroll.beRead();
		} else {
			Utility.displayMessage(this + " does not have " + scroll);
		}
	}

	public void haveFit() {
		say("Yaaaah! I am upset");
	}

	public void moveTo(Place newPlace) {
		Utility.displayMessage(this + " moves from " + place + " to " + newPlace);
		place.lose(this);
		newPlace.gain(this);
		for(Thing possession : possessions) {
			place.lose(possession);
			newPlace.gain(possession);
		}
		place = newPlace;
		greet(otherPeopleAtSamePlace());
	}

	public void go(String direction) {
		Place newPlace = place.neighborTowards(direction);
		if (newPlace == null) {
			Utility.displayMessage("You cannot go " + direction + " from " + place);
		} else {
			moveTo(newPlace);
		}
	}

	public void give(Person newOwner, Thing thing){
		if (newOwner == thing.getOwner()){
			Utility.displayMessage(newOwner + " already has " + thing);
		}
		else if (this.possessions.contains(thing) == false){
			Utility.displayMessage(this + " does not own " + thing);
		}
		else{
			this.lose(thing);
			newOwner.take(thing);	
		}
	}
	
	public void take(Thing thing) {
		if (this == thing.getOwner()) {
			Utility.displayMessage(this + " already has " + thing);
		} else {
			if (thing.isOwned()) {
				Person owner = thing.getOwner();
				owner.lose(thing);
				owner.haveFit();
			}
			thing.setOwner(this);
			possessions.add(thing);
			say("I take " + thing);
		}
	}

	public void lose(Thing thing) {
		if (thing.getOwner() != this) {
			Utility.displayMessage(this + " doesn't have " + thing);
		} else {
			thing.becomeUnowned();
			possessions.remove(thing);
			say("I lose " + thing);
		}
	}

	public void greet(List<Person> people) {
		if (!people.isEmpty()) {
			say("Hi " + Utility.verbalizeList(people, "no one")); // "no one" can't happen
		}
	}

	@Override
	public String toString() {
		return name;
	}
}
