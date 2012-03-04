package edu.gac.mcs270.gack.shared.domain;

import java.util.List;

import edu.gac.mcs270.gack.client.Registry;
import edu.gac.mcs270.gack.client.Utility;

public class AutoPerson extends Person {
	
	private static final long serialVersionUID = 1630121848650414931L;
	private static Registry registry;
	
	public static Registry getRegistry() { return registry; }
	public static void setRegistry(Registry registry) { AutoPerson.registry = registry; }

	private int threshold;
	private int restlessness;
	
	public AutoPerson(String name, Place place, int threshold) {
		super(name, place);
		this.threshold = threshold;
		this.restlessness = 0;
		if(registry != null)
			registry.add(this);
	}
	
	protected AutoPerson(){
		if(registry != null)
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
