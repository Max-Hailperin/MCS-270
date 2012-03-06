package edu.gac.mcs270.gack.shared.domain;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestTransfers {
	
	@Test
	public void takeUnowned(){
		Place place = new Place("place");
		Person newOwner = new Person("newOwner", place);
		Thing thing = new Thing("thing");
		newOwner.take(thing);
		assertEquals(newOwner, thing.getOwner());
		assertTrue(newOwner.getPossessions().contains(thing));
	}

}
