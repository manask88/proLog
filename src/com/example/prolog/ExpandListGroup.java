package com.example.prolog;

import java.util.ArrayList;
import java.util.List;


public class ExpandListGroup {

	private String Name;
	private List<ExpandListChild> Items;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		this.Name = name;
	}
	public List<ExpandListChild> getItems() {
		return Items;
	}
	public void setItems(List<ExpandListChild> Items) {
		this.Items = Items;
	}
	
	
}
