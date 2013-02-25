package com.example.prolog;

import java.util.Comparator;

import com.example.prolog.model.Contact;

public class ContactsCompareByName implements Comparator<Contact> {


@Override
public int compare(Contact contact1, Contact contact2) {
	// TODO Auto-generated method stub
	return contact1.getName().compareTo(contact2.getName());
}
}
