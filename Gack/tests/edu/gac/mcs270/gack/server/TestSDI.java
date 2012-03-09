package edu.gac.mcs270.gack.server;
import static org.junit.Assert.*;

import org.junit.Test;

import edu.gac.mcs270.gack.shared.domain.Person;
import edu.gac.mcs270.gack.shared.domain.Place;
import edu.gac.mcs270.gack.shared.domain.Thing;
import edu.gac.mcs270.utilities.SpreadsheetDrivenInstantiator;


public class TestSDI {

	@Test
	public void test() {
		SpreadsheetDrivenInstantiator sdi = new SpreadsheetDrivenInstantiator("0Av6ZjRxtPYJqdHI3aEtxR3lUeEtMeDNQcnpKZDZvbGc",
				"edu.gac.mcs270.gack.shared.domain");
		Person player = sdi.getInstance(Person.class, "player");
		Place dormitory = sdi.getInstance(Place.class, "Dormitory");
		assertEquals(dormitory, player.getPlace());
		assertTrue(dormitory.getOccupants().contains(player));
		Place lounge = sdi.getInstance(Place.class, "Lounge");
		Thing glasses = sdi.getInstance(Thing.class, "Karl's Glasses");
		Place gso = sdi.getInstance(Place.class, "Good Ship Olin");
		assertEquals(lounge, gso.neighborTowards("up"));
		assertTrue(lounge.getContents().contains(glasses));
	}

}
