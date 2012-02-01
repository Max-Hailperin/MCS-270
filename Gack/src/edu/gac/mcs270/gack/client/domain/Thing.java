package edu.gac.mcs270.gack.client.domain;

public class Thing {
	
	private String name;
	private Person owner;
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public Person getOwner() { return owner; }
	public void setOwner(Person owner) { this.owner = owner; }

	public Thing(String name) {
		super();
		this.name = name;
	}
	
	public boolean isOwned() {
		return owner != null;
	}
	
	public void becomeUnowned() {
		owner = null;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
