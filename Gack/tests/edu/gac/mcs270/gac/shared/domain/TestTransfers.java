package edu.gac.mcs270.gac.shared.domain;

import static org.junit.Assert.*;

import edu.gac.mcs270.gack.shared.domain.*;
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
	public void giveOwnedBySelf(){
		Person donor = new Person("donor", place);
		setupOwnership(donor);
		donor.give(newOwner, thing);
		checkOwnership(newOwner);
		assertArrayEquals(new String[]{"At Place: donor says -- I lose Thing","At Place: newOwner says -- I take Thing"},
				display.getMessages());
	}
	
	@Test
	public void giveUnowned(){
		//Tests the case where thing is unowned when donor tries to give it.	
			Person donor = new Person("donor", place);
			setupOwnership(null);
			donor.give(newOwner, thing);
			checkOwnership(null);
			//check messages
			assertArrayEquals(new String[]{"At Place: donor says -- I can't do that!"},
					display.getMessages());
	}
	@Test
	public void giveOwnedByOther(){
		//Tests the case where a third party originally owns thing
		Person donor = new Person("donor", place);
		Person oldOwner = new Person("oldOwner", place);
		setupOwnership(oldOwner);
		donor.give(newOwner, thing);
		checkOwnership(oldOwner);
		//check messages
		assertArrayEquals(new String[]{"At Place: donor says -- I can't do that!"},
				display.getMessages());
	
	}
	@Test
	public void giveOwnedByRecipient(){
		//Should function essentially the same as giveOwnedByOther, but specifically, "other" is "newOwner"
		
		Person donor = new Person("donor", place);
		setupOwnership(newOwner);
		donor.give(newOwner, thing);
		checkOwnership(newOwner);
		//check messages
		assertArrayEquals(new String[]{"At Place: donor says -- I can't do that!"},
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
		assertArrayEquals(new String[]{newOwner + " already has " + thing},
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
		if (owner != null) {
			owner.getPossessions().add(thing);
		}


	}

	private void checkOwnership(Person owner) {
		if(owner!=null){
			assertTrue(owner.getPossessions().contains(thing));
		}
		assertEquals(owner, thing.getOwner());
	}

	@Before
	public void createObjects() {
		place = new Place("Place");
		newOwner = new Person("newOwner", place);
		thing = new Thing("Thing");
	}

}