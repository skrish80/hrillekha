package com.techlords.crown.business.exception;

import java.util.ArrayList;
import java.util.List;

import com.techlords.infra.AppModel;

@SuppressWarnings("serial")
public class CrownException extends Exception {

	private final List<String> messages = new ArrayList<String>();

	public CrownException() {
	}

	public CrownException(String msg) {
		super(msg);
		addMessage(msg);
	}

	public CrownException(String msg, List<String> msgs) {
		super(msg);
		messages.addAll(msgs);
	}

	public CrownException(Throwable throwable) {
		super(throwable);
	}

	public CrownException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public AppModel getModel() {
		return null;
	}

	public List<String> getMessages() {
		return messages;
	}
	
	public void addMessage(String msg) {
		messages.add(msg);
	}
}
