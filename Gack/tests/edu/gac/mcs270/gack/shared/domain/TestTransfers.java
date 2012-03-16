package edu.gac.mcs270.gack.shared.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.gac.mcs270.gack.client.Utility;

public class TestTransfers {

	private Place place;
	private Person newOwner;
	private Thing thing;
	private MockDisplay display;
	
	@Test
	public void giveOwnedByDonor(){
		Person donor = new Person("donor", place);
		setupOwnership(donor);
		donor.give(newOwner, thing);
		checkOwnership();
		// TODO check appropriate message
		assertArrayEquals(new String[]{"At Place: donor says -- I give Thing to newOwner"},
				display.getMessages());
	}
	
	@Test
	public void giveUnowned(){
		Person donor = new Person("donor", place);
		donor.give(newOwner, thing);
		assertArrayEquals(new String[]{"donor does not have Thing"},
				display.getMessages());
	}
	
	@Test
	public void giveOwnedByRecipient(){
		Person donor = new Person("donor", place);
		setupOwnership(newOwner);
		donor.give(newOwner, thing);
		checkOwnership();
		assertArrayEquals(new String[]{"donor does not have Thing"},
				display.getMessages());
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
		// TODO check that appropriate message(s) was displayed.
		assertArrayEquals(new String[]{"newOwner already has Thing"},
				display.getMessages());
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