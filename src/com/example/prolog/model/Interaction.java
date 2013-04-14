package com.example.prolog.model;

import java.util.ArrayList;
import java.util.HashMap;


public class Interaction {
	private long id;
	
	private String date;
	private boolean followUp;
	private String notes;
	private String location, event,type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Interaction ()
	{
		id=0;
		date="";
		followUp=false;
		notes="";
	
		
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	
}
