package edu.gac.mcs270.gac.shared.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.gac.mcs270.gack.client.Utility;
import edu.gac.mcs270.gack.shared.domain.Person;
import edu.gac.mcs270.gack.shared.domain.Place;
import edu.gac.mcs270.gack.shared.domain.Thing;

public class TestTransfers {

	private Place place;
	private Person newOwner;
	private Thing thing;
	private MockDisplay display;
	private Person donor;
	
	@Test
	public void giveOwnedBySelf(){
		donor = new Person("donor", place);
		setupOwnership(donor);
		donor.give(newOwner, thing);
		checkOwnership();
		assertTrue(!donor.getPossessions().contains(thing));
		assertArrayEquals(new String[]{"At Place: donor says -- I lose " + thing, 
				donor + " gave the " + thing + " to " + newOwner}, display.getMessages());
	}
	
	@Test
	public void giveSomethingNotOwned()
	{
		donor = new Person("donor", place);
		donor.give(newOwner, thing);
		assertArrayEquals(new String[]{donor + " does not own " + thing}, display.getMessages());
		assertTrue(thing.getOwner() != donor);
		assertTrue(!donor.getPossessions().contains(thing));
	}

	@Test
	public void giveSomethingToSelf()
	{
		donor = new Person("donor", place);
		setupOwnership(donor);
		donor.give(donor, thing);
		assertArrayEquals(new String[]{donor + " already owns " + thing}, display.getMessages());
	}
	
	@Test
	public void takeUnowned() {
		newOwner.take(thing);
		checkOwnership();
		assertArrayEquals(new String[]{"At Place: newOwner says -- I take Thing"},
				display.getMessages());
		}

	@Test
	public void takeOwnedBySelf() {
		setupOwnership(newOwner);
		newOwner.take(thing);
		checkOwnership();
		assertArrayEquals(new String[]{newOwner + " already has " + thing}, display.getMessages());
	}
	
	@After
	public void cleanupDisplay() {
		Utility.displayMessage(null);
	}

	@Before
	public void setupDisplay() {
		display = new MockDisplay();
		Utility.setMessageDisplay(display);
	}
	
	@Test
	public void takeOwnedByOther() {
		Person oldOwner = new Person("oldOwner", place);
		setupOwnership(oldOwner);
		newOwner.take(thing);
		checkOwnership();
		assertArrayEquals(new String[]{
				"At Place: oldOwner says -- I lose Thing",
				"At Place: oldOwner says -- Yaaaah! I am upset",
				"At Place: newOwner says -- I take Thing"},
				display.getMessages());

	}

	private void setupOwnership(Person owner) {
		thing.setOwner(owner);
		owner.getPossessions().add(thing);
	}

	private void checkOwnership() {
		assertTrue(newOwner.getPossessions().contains(thing));
		assertEquals(newOwner, thing.getOwner());
	}

	@Before
	public void createObjects() {
		place = new Place("Place");
		newOwner = new Person("newOwner", place);
		thing = new Thing("Thing");
	}

}