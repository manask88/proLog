package com.example.prolog;

import java.util.Comparator;

import com.example.prolog.model.Group;

public class GroupCompareByName implements Comparator<Group> {

	@Override
	public int compare(Group group1, Group group2) {
		// TODO Auto-generated method stub
		return group1.getName().toLowerCase().compareTo(group2.getName().toLowerCase());

	}

}
