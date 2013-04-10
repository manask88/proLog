package com.example.prolog.model;

import java.util.ArrayList;
import java.util.HashMap;


public class Interaction {
	private long id;
	
	private String date;
	private boolean followUp;
	private String text;
	

	public Interaction ()
	{
		id=0;
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
