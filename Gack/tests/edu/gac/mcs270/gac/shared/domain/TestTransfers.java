package edu.gac.mcs270.gac.shared.domain;

import org.junit.Test;

import edu.gac.mcs270.gack.shared.domain.Person;
import static org.junit.Assert.*;


public class TestTransfers {

	@Test
	public void testTakingUnowned(){
		Place place = new Place("place");
		Person newOwner = new Person("newOwner", place);
		Thing thing - new Thing("thing");
		newOwner.take(thing);
		assertEquals(newOwner, thing.getOwner());
		assertTrue(newOwner.getPossessions().contains(thing));
		
	}
	
}
