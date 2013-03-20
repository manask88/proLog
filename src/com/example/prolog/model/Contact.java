package com.example.prolog.model;

import android.graphics.Bitmap;

public class Contact {
	private long id;
	private String name;
	private String title;
	private String company;
	private String home_phone;
	private String work_phone;
	private String email;
	private String location;
	private long contactManagerId;
	private Bitmap photo;

	
	public Bitmap getPhoto() {
		return photo;
	}
	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}
	public Contact ()
	{
		id=0;
		name="";
		title="";
		company="";
		
		work_phone="pred2";
		email="";
		location="";
		contactManagerId=0;
		photo=null;
	}
	public long getContactManagerId() {
		return contactManagerId;
	}
	public void setContactManagerId(long contactManagerId) {
		this.contactManagerId = contactManagerId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getHome_phone() {
		return home_phone;
	}
	public void setHome_phone(String home_phone) {
		this.home_phone = home_phone;
	}
	public String getWork_phone() {
		return work_phone;
	}
	public void setWork_phone(String work_phone) {
		this.work_phone = work_phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}
