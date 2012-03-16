package edu.gac.mcs270.gack.shared.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.gac.mcs270.gack.client.Utility;

public class TestTransfers {

	private Place place;
	private Person newOwner;
	private Person donor;
	private Thing thing;
	private MockDisplay display;
	
	@Test
	public void giveOwnedBySelf(){
		setupOwnership(donor);
		donor.give(newOwner, thing);
		checkOwnership(newOwner);
		assertArrayEquals(new String[]{
				"At Place: donor says -- I lose Thing", 
				"At Place: donor says -- I give newOwner Thing"},
				display.getMessages());
	}
	@Test
	public void giveToSelf(){
		setupOwnership(donor);
		donor.give(donor, thing);
		checkOwnership(donor);
		assertArrayEquals(new String[]{"donor already has Thing"}, display.getMessages());
		
	}
	
	@Test
	public void giveOwnedByOther(){
		Person oldOwner = new Person("oldOwner", place);
		setupOwnership(oldOwner);
		donor.give(newOwner, thing);
		checkOwnership(oldOwner);
		assertArrayEquals(new String[]{"Thing is not donor's to give"}, display.getMessages());
	}
	
	@Test
	public void giveUnowned(){
		donor.give(newOwner, thing);
		checkOwnership();
		assertArrayEquals(new String[]{"Thing is not donor's to give"}, display.getMessages());
	}

	@Test
	public void takeUnowned() {
		newOwner.take(thing);
		checkOwnership(newOwner);
		assertArrayEquals(new String[]{"At Place: newOwner says -- I take Thing"},
				display.getMessages());
		}

	@Test
	public void takeOwnedBySelf() {
		setupOwnership(newOwner);
		newOwner.take(thing);
		checkOwnership(newOwner);
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
		checkOwnership(newOwner);
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

	private void checkOwnership(Person owner) {
		assertTrue(owner.getPossessions().contains(thing));
		assertEquals(owner, thing.getOwner());
	}

	private void checkOwnership() {
		assertFalse(thing.isOwned());
	}
	
	@Before
	public void createObjects() {
		place = new Place("Place");
		newOwner = new Person("newOwner", place);
		donor = new Person("donor", place);
		thing = new Thing("Thing");
	}
	
	

}