package com.example.prolog.model;


public class Interaction {
	private long id;
	private long contactId;
	private String date;
	private boolean followUp;
	private String text;
	
	public Interaction ()
	{
		id=0;
		contactId=0;
		date="";
		followUp=false;
		text="";
	
		
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getContactId() {
		return contactId;
	}
	public void setContactId(long contactId) {
		this.contactId = contactId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public boolean isFollowUp() {
		return followUp;
	}
	public void setFollowUp(boolean followUp) {
		this.followUp = followUp;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	
	
}
