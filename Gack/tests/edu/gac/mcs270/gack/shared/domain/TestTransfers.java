package edu.gac.mcs270.gack.shared.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestTransfers {
	
	@Test
	public void testTakeUnowned(){
		Place place = new Place("place");
		Person newOwner = new Person("newOwner", place);
		Thing thing = new Thing("Karl's Glasses");
		
		newOwner.take(thing);
		assertEquals(newOwner, thing.getOwner());
		assertTrue(newOwner.getPossessions().contains(thing));
	}
}
