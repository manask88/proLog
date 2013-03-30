package com.example.prolog;

import java.util.Comparator;

import android.util.Log;

import com.example.prolog.model.Contact;

public class ContactsChildCompareByName implements Comparator<ExpandListChild> {
	public static final String TAG = ContactsChildCompareByName.class
			.getSimpleName();

	@Override
	public int compare(ExpandListChild contact1, ExpandListChild contact2) {
		if (contact1.getName() == null && contact2.getName() == null) {
			return 0;
		}

		
		if (contact1.getName() == null) {
			return -1;
		}

		if (contact2.getName() == null) {
			return 1;
		}

		return contact1.getName().toLowerCase()
				.compareTo(contact2.getName().toLowerCase());

	}
}
