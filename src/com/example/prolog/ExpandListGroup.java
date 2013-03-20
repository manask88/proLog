package com.example.prolog;

import java.util.ArrayList;
import java.util.List;


public class ExpandListGroup {

	private String Name;
	private List<ExpandListChild> Items;
	private boolean isChecked;
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
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
