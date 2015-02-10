package org.jboss.teiid.drools;

import java.util.List;

public class Message {
	
	public static final int HELLO = 0;
	
	public static final int GOODBYE = 1;
	
	private String message;
	
	private int status;
	
	public Message() {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public boolean isSomething(String msg, List<Object> list) {
		list.add( this );
		return this.message.equals( msg );
	}
	
	
}
