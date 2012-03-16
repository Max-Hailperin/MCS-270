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
	public void giveOwnedDonor(){
		donor = new Person("donor", place);
		setup(donor, donor);
		assertArrayEquals(new String[]{
				"At Place: donor says -- I lose Thing",
		"At Place: newOwner says -- I take Thing"},
		display.getMessages());

	}
	
	@Test
	public void giveOwnedByNewOwner(){
		donor = new Person("donor", place);
		setup(donor, newOwner);
		assertArrayEquals(new String[]{"newOwner already has Thing"},
				display.getMessages());
	}

	@Test
	public void giveUnownedByDonor(){
		donor = new Person ("donor", place);
		donor.give(newOwner, thing);
		assertArrayEquals(new String[]{"donor does not own Thing"},
				display.getMessages());
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
	
	private void setup(Person donor, Person owner) {
		setupOwnership(owner);
		donor.give(newOwner, thing);
		checkOwnership(newOwner);
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

	private void setupOwnership(Person owner) {
		thing.setOwner(owner);
		owner.getPossessions().add(thing);
	}

	private void checkOwnership(Person owner) {
		assertTrue(owner.getPossessions().contains(thing));
		assertEquals(owner, thing.getOwner());
	}

	@Before
	public void createObjects() {
		place = new Place("Place");
		newOwner = new Person("newOwner", place);
		thing = new Thing("Thing");
	}

}