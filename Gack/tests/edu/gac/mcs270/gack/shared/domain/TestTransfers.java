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
	private Person donor;
	
	@Test
	public void giveOwnedByDonor(){
		setupOwnership(donor);
		donor.give(newOwner, thing);
		checkOwnership();
		assertArrayEquals(new String[]{
				"At Place: donor says -- newOwner, have Thing", 
				"At Place: newOwner says -- Thanks, donor!"},
				display.getMessages());
	}
	
	@Test
	public void giveUnowned(){
	donor.give(newOwner, thing);	
	//figure out ownership
	assertArrayEquals(new String[]{"donor doesn't have Thing"},
			display.getMessages());
	}
	
	public void giveOwnedByRecipient(){
		setupOwnership(newOwner);
		donor.give(newOwner, thing);
		checkOwnership();
		assertArrayEquals(new String[]{"newOwner already has Thing"},
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
		donor = new  Person("donor", place);
	}

}