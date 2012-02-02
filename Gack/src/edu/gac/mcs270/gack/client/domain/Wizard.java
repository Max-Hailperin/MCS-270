package edu.gac.mcs270.gack.client.domain;

import java.util.List;

public class Wizard extends AutoPerson {
	
	private Place chamber;
	
	public Wizard(String name, Place place, int threshold, Place chamber) {
		super(name, place, threshold);
		this.chamber = chamber;
	}
	
	@Override
	public void act() {
		List<Scroll> scrolls = Scroll.scrollsIn(getPlace());
		if ((!scrolls.isEmpty()) && (!getPlace().equals(chamber))) {
			take(scrolls.get(0));
			moveTo(chamber);
			lose(scrolls.get(0));
		} else {
			super.act();
		}
	}
	
	public static void main(String[] args) {
		Place chamberOfWizards = new Place("Chamber of Wizards");
		Place karlsOffice = new Place("Karl's Office");
		Wizard elvee = new Wizard("Elvee", karlsOffice, 2, chamberOfWizards);
		
		karlsOffice.gain(new Scroll("Scroll of Enlightenment"));
		karlsOffice.gain(new Thing("Karl's phone"));
		
		System.out.println(karlsOffice.getContents());
	}
}
