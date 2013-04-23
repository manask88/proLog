package com.example.prolog.model;

import java.util.HashMap;

import android.graphics.Bitmap;

public class FollowUp {
	private long id;
	private long contactId;
	private String title;
	private String date;
	private String notes;


	public FollowUp ()
	{
		id=0;
		contactId=0;
		title="";
		date="";
		notes="";
	
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


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getNotes() {
		return notes;
	}


	public void setNotes(String notes) {
		this.notes = notes;
	}

}
