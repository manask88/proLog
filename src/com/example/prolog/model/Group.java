package com.example.prolog.model;

public class Group {
	long id;
	String name;
	
	public Group() {
		id=0;
		name="";
		
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
}
