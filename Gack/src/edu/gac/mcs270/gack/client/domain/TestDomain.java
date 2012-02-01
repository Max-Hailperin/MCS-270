package edu.gac.mcs270.gack.client.domain;

class TestDomain {

	public static void main(String[] args) {
		Place foodService = new Place("Food Service");
		Place po = new Place("Post Office");
		Place alumniHall = new Place("Alumni Hall");
		Place chamberOfWizards = new Place("Chamber of Wizards");
		Place library = new Place("Library");
		Place goodShipOlin = new Place("Good Ship Olin");
		Place lounge = new Place("Lounge");
		Place computerLab = new Place("Computer Lab");
		Place offices = new Place("Offices");
		Place dormitory = new Place("Dormitory");
		Place pond = new Place("Pond");
		
		foodService.addNewNeighbor("down", po);
		po.addNewNeighbor("south", alumniHall);
		alumniHall.addNewNeighbor("north", foodService);
		alumniHall.addNewNeighbor("east", chamberOfWizards);
		alumniHall.addNewNeighbor("west", library);
		chamberOfWizards.addNewNeighbor("west", alumniHall);
		chamberOfWizards.addNewNeighbor("south", dormitory);
		dormitory.addNewNeighbor("north", chamberOfWizards);
		dormitory.addNewNeighbor("west", goodShipOlin);
		library.addNewNeighbor("east", library);
		library.addNewNeighbor("south", goodShipOlin);
		goodShipOlin.addNewNeighbor("north", library);
		goodShipOlin.addNewNeighbor("east", dormitory);
		goodShipOlin.addNewNeighbor("up", lounge);
		lounge.addNewNeighbor("west", computerLab);
		lounge.addNewNeighbor("south", offices);
		computerLab.addNewNeighbor("east", lounge);
		offices.addNewNeighbor("north", lounge);
		
		new AutoPerson("Max", offices, 2);
		new AutoPerson("Karl", computerLab, 4);
		new Witch("Barbara", offices, 3, pond);
		new Wizard("Barbara", offices, 1, chamberOfWizards);
		Person player = new Person("player", dormitory);
		
		library.gain(new Scroll("Scroll of Enlightenment"));
		String[] someTitles = {"War and Peace", "Iliad", "Collected Works of Rilke"};
		for (String title : someTitles) {
			library.gain(new Scroll(title));
		}
		computerLab.gain(new Scroll("Unix Programmers Manual"));
		computerLab.gain(new Scroll("NeXT User's Reference"));
		
		

		Thing karlsGlasses = new Thing("Karl's glasses");
		offices.gain(karlsGlasses);
		
		
		System.out.println("goodShipOlin.exits(): " + goodShipOlin.exits());
		System.out.println("goodShipOlin.neighbors(): " + goodShipOlin.neighbors());
		System.out.println("goodShipOlin.neighborTowards(\"north\"): " + goodShipOlin.neighborTowards("north"));
		System.out.println("goodShipOlin.neighborTowards(\"west\"): " + goodShipOlin.neighborTowards("west"));
		player.listPossessions();
		player.lookAround();
		player.take(karlsGlasses);
		player.lookAround();
		player.go("north");
		
		String[] testStrings = {"asd", "qwe", "zxc"};
		for (String string : testStrings) {
			System.out.println(string);
		}
	}
}
