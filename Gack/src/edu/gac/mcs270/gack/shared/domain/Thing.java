package edu.gac.mcs270.gack.shared.domain;

import java.io.Serializable;

public class Thing implements Serializable {
	
	private static final long serialVersionUID = -5228774276830723160L;
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
	
	protected Thing(){}
	
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
