package edu.gac.mcs270.gack.client.domain;

import java.util.List;

import edu.gac.mcs270.gack.client.Registry;
import edu.gac.mcs270.gack.client.Utility;

public class AutoPerson extends Person {
	
	private static Registry registry;
	
	public static Registry getRegistry() { return registry; }
	public static void setRegistry(Registry registry) { AutoPerson.registry = registry; }

	private int threshold;
	private int restlessness;
	
	public AutoPerson(String name, Place place, int threshold) {
		super(name, place);
		this.threshold = threshold;
		this.restlessness = 0;
		registry.add(this);
	}
	
	public void maybeAct() {
		if (restlessness < threshold) {
			restlessness++;
		} else {
			restlessness = 0;
			act();
		}
	}
	
	public void act() {
		List<Place> neighbors = getPlace().neighbors();
		if (!neighbors.isEmpty()) {
			Place newPlace = neighbors.get(Utility.randInt(neighbors.size()));
			moveTo(newPlace);
		}
	}
}
