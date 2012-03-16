package edu.gac.mcs270.gac.shared.domain;

import java.util.ArrayList;

import edu.gac.mcs270.gack.client.MessageDisplay;

public class MockDisplay implements MessageDisplay {
	
	ArrayList<String> messages = new ArrayList<String>();

	@Override
	public void displayMessage(String message) {
		messages.add(message);
	}
	
	public String[] getMessages(){
		return messages.toArray(new String[0]);
	}

}