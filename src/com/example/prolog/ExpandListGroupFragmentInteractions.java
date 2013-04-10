package com.example.prolog;

import java.util.ArrayList;
import java.util.List;

import com.example.prolog.model.Interaction;


public class ExpandListGroupFragmentInteractions {

	private String Name;
	private long id;


	private ExpandListChildFragmentInteractions expandListChildFragmentInteractions;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		this.Name = name;
	}

	public void setExpandListChildFragmentInteractions(Interaction interaction) {
		
		expandListChildFragmentInteractions=new ExpandListChildFragmentInteractions();
		expandListChildFragmentInteractions.setName(interaction.getText());
		expandListChildFragmentInteractions.setTag("tag1");

	}
	
	public ExpandListChildFragmentInteractions getChildInteraction() {
		return expandListChildFragmentInteractions;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
