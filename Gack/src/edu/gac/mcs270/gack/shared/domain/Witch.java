package edu.gac.mcs270.gack.shared.domain;

import java.util.List;
import java.util.ArrayList;

import edu.gac.mcs270.gack.client.Utility;

public class Witch extends AutoPerson {

	private static final long serialVersionUID = 7868030973413938410L;
	private Place pond;
	
	public Witch(String name, Place place, int threshold, Place pond) {
		super(name, place, threshold);
		this.pond = pond;
	}
	
	protected Witch(){}

	@Override
	public void act() {
		List<Person> others = otherPeopleAtSamePlace();
		if (!others.isEmpty()) {
			Person victim = others.get(Utility.randInt(others.size()));
			curse(victim);
		} else {
			super.act();
		}
	}

	public void curse(Person person) {
		say("Hah hah hah, I'm going to turn you into a frog, " + person);
		turnIntoFrog(person);
		say("Hee hee " + person + " looks better in green!");
	}
	
	public void turnIntoFrog(Person person) {
		for (Thing thing : new ArrayList<Thing>(person.getPossessions())) {
			person.lose(thing);
		}
		person.say("Ribbitt!");
		person.moveTo(pond);
	}
}
