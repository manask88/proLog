package com.example.prolog.model;

import java.util.ArrayList;

public class CustomField {

	String parentTable;
	ArrayList<String> fieldNames;
	
	public CustomField(){
		parentTable = null;
		fieldNames = null;
	}
	
	public String getParentTable() {
		return parentTable;
	}
	
	public void setParentTable(String parentTable) {
		this.parentTable = parentTable;
	}
	
	public ArrayList<String> getFieldNames() {
		return fieldNames;
	}
	
	public void setFieldNames(ArrayList<String> fieldNames) {
		this.fieldNames = fieldNames;
	}
		
}
