package edu.gac.mcs270.gack.server;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.gac.mcs270.gack.shared.Loader;
import edu.gac.mcs270.gack.shared.domain.AutoPerson;
import edu.gac.mcs270.gack.shared.domain.Person;
import edu.gac.mcs270.gack.shared.domain.Place;
import edu.gac.mcs270.gack.shared.domain.Scroll;
import edu.gac.mcs270.gack.shared.domain.Thing;
import edu.gac.mcs270.gack.shared.domain.Witch;
import edu.gac.mcs270.gack.shared.domain.Wizard;

public class LoaderImpl extends RemoteServiceServlet implements Loader {

	private static final long serialVersionUID = 3141859568997802325L;
	private Map<String, Place> places;
	private Map<String, Thing> things;
	
	public Person getPlayer(){
		SpreadsheetService service = new SpreadsheetService("max.mcs.gac.edu-Gack-0.0");
		WorksheetFeed feed;
		try {
			URL feedURL = new URL("https://spreadsheets.google.com/feeds/worksheets/0Av6ZjRxtPYJqdHI3aEtxR3lUeEtMeDNQcnpKZDZvbGc/public/values");
			feed = service.getFeed(feedURL, WorksheetFeed.class);
		} catch (Exception e) {
			throw new Error("Trouble getting worksheet feed", e);
		}
		ListFeed placeFeed=null, neighborsFeed=null, autoPersonFeed=null, witchFeed=null, wizardFeed=null,
				thingFeed=null, scrollFeed=null, gainFeed=null, personFeed=null;
		for (WorksheetEntry worksheet : feed.getEntries()) {
			String title = worksheet.getTitle().getPlainText();
			if("Place".equals(title)){
				placeFeed = getListFeed(service, worksheet.getListFeedUrl());
			} else if("Place.addNewNeighbor".equals(title)){
				neighborsFeed = getListFeed(service, worksheet.getListFeedUrl());
			} else if("AutoPerson".equals(title)){
				autoPersonFeed = getListFeed(service, worksheet.getListFeedUrl());
			} else if("Witch".equals(title)){
				witchFeed = getListFeed(service, worksheet.getListFeedUrl());
			} else if("Wizard".equals(title)){
				wizardFeed = getListFeed(service, worksheet.getListFeedUrl());
			} else if("Thing".equals(title)){
				thingFeed = getListFeed(service, worksheet.getListFeedUrl());
			} else if("Scroll".equals(title)){
				scrollFeed = getListFeed(service, worksheet.getListFeedUrl());
			} else if("Place.gain".equals(title)){
				gainFeed = getListFeed(service, worksheet.getListFeedUrl());
			} else if("Person".equals(title)){
				personFeed = getListFeed(service, worksheet.getListFeedUrl());
			} else {
				throw new Error("Unexpected worksheet " + title);
			}
		}

		places = new HashMap<String, Place>();
		things = new HashMap<String, Thing>();
		for (ListEntry entry : placeFeed.getEntries()) {
			String name = entry.getCustomElements().getValue("name");
			places.put(name, new Place(name));
		}

		for (ListEntry entry : neighborsFeed.getEntries()) {
			CustomElementCollection customElements = entry.getCustomElements();
			Place source = getPlace(customElements.getValue("source"));
			String direction = customElements.getValue("direction");
			Place destination = getPlace(customElements.getValue("destination"));
			source.addNewNeighbor(direction, destination);
		}

		for (ListEntry entry : autoPersonFeed.getEntries()){
			CustomElementCollection customElements = entry.getCustomElements();
			String name = customElements.getValue("name");
			Place place = getPlace(customElements.getValue("place"));
			int threshold = Integer.parseInt(customElements.getValue("threshold"));
			new AutoPerson(name, place, threshold);
		}

		for (ListEntry entry : witchFeed.getEntries()){
			CustomElementCollection customElements = entry.getCustomElements();
			String name = customElements.getValue("name");
			Place place = getPlace(customElements.getValue("place"));
			int threshold = Integer.parseInt(customElements.getValue("threshold"));
			Place pond = getPlace(customElements.getValue("pond"));
			new Witch(name, place, threshold, pond);
		}

		for (ListEntry entry : wizardFeed.getEntries()){
			CustomElementCollection customElements = entry.getCustomElements();
			String name = customElements.getValue("name");
			Place place = getPlace(customElements.getValue("place"));
			int threshold = Integer.parseInt(customElements.getValue("threshold"));
			Place chamber = getPlace(customElements.getValue("chamber"));
			new Wizard(name, place, threshold, chamber);
		}

		for (ListEntry entry : thingFeed.getEntries()){
			CustomElementCollection customElements = entry.getCustomElements();
			String name = customElements.getValue("name");
			things.put(name, new Thing(name));
		}

		for (ListEntry entry : scrollFeed.getEntries()){
			CustomElementCollection customElements = entry.getCustomElements();
			String name = customElements.getValue("name");
			things.put(name, new Scroll(name));
		}

		for (ListEntry entry : gainFeed.getEntries()){
			CustomElementCollection customElements = entry.getCustomElements();
			Place place = getPlace(customElements.getValue("place"));
			Thing thing = getThing(customElements.getValue("thing"));
			place.gain(thing);
		}

		Person player = null;
		for (ListEntry entry : personFeed.getEntries()){
			CustomElementCollection customElements = entry.getCustomElements();
			String name = customElements.getValue("name");
			Place place = getPlace(customElements.getValue("place"));
			Person p = new Person(name, place);
			if(name.equals("player")){
				player = p;
			}
		}
		return player;
	}

	public LoaderImpl() {
		super();
	}

	private ListFeed getListFeed(SpreadsheetService service, URL url) throws Error {
		ListFeed feed;
		try {
			feed = service.getFeed(url, ListFeed.class);
		} catch (Exception e) {
			throw new Error("Trouble getting list feed", e);
		}
		return feed;
	}

	private Place getPlace(String placeName) throws Error {
		Place place = places.get(placeName);
		if(place==null)
			throw new Error("Unkown place " + placeName);
		return place;
	}

	private Thing getThing(String thingName) throws Error {
		Thing thing = things.get(thingName);
		if(thing==null)
			throw new Error("Unkown thing " + thingName);
		return thing;
	}

}

