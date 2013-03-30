package com.example.prolog;

import java.util.Comparator;

import com.example.prolog.model.Contact;

public class ContactsCompareByName implements Comparator<Contact> {


@Override
public int compare(Contact contact1, Contact contact2) {
	return contact1.getName().toLowerCase().compareTo(contact2.getName().toLowerCase());
}
}
