package com.example.prolog.model;

public class TypeValue {

	String type;
	String value;
	
	public TypeValue()
	{
		type="";
		value="";
		
		
	}
	
	public TypeValue(String type, String value)
	{
		this.type=type;
		this.value=value;
		
		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
