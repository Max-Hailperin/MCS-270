package edu.gac.mcs270.gack.shared.domain;

import java.util.List;

public class Wizard extends AutoPerson {

	private static final long serialVersionUID = -1241325420434534600L;
	private Place chamber;
	
	public Wizard(String name, Place place, int threshold, Place chamber) {
		super(name, place, threshold);
		this.chamber = chamber;
	}
	
	protected Wizard(){}
	
	@Override
	public void act() {
		List<Scroll> scrolls = Scroll.scrollsIn(getPlace());
		if ((!scrolls.isEmpty()) && (getPlace() != chamber)) {
			take(scrolls.get(0));
			moveTo(chamber);
			lose(scrolls.get(0));
		} else {
			super.act();
		}
	}
}
