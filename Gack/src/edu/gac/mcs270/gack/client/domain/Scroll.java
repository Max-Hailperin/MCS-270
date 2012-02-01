package edu.gac.mcs270.gack.client.domain;

import java.util.ArrayList;
import java.util.List;

import edu.gac.mcs270.gack.client.Utility;

public class Scroll extends Thing {
	
	public Scroll(String title) {
		super(title);
	}

	public void beRead() {
		Person owner = getOwner();
		if (owner == null) {
			Utility.displayMessage("No one has " + getName());
		} else {
			owner.say("I have read " + getName());
		}
	}
	
	public static List<Scroll> scrollsIn(Place place) {
		ArrayList<Scroll> scrollsIn = new ArrayList<Scroll>();
		for (Thing thing : place.getContents()) {
			if (thing instanceof Scroll) {
				scrollsIn.add((Scroll) thing);
			}
		}
		return scrollsIn;
	}
}
