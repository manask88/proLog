package com.example.prolog.db;

import java.util.ArrayList;
import java.util.List;

import com.example.prolog.model.Contact;
import com.example.prolog.model.Interaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactsDataSource {

	public static final String LOGTAG = "EXPLORECA";

	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;

	private static final String[] allColumnsContacts = {
			ContactsDBOpenHelper.COLUMN_ID, 
			ContactsDBOpenHelper.COLUMN_NAME,
			ContactsDBOpenHelper.COLUMN_TITLE,
			ContactsDBOpenHelper.COLUMN_COMPANY,
			ContactsDBOpenHelper.COLUMN_HOME_PHONE,
			ContactsDBOpenHelper.COLUMN_WORK_PHONE,
			ContactsDBOpenHelper.COLUMN_EMAIL,
			ContactsDBOpenHelper.COLUMN_LOCATION };

	private static final String[] allColumnsInteractions = {
			ContactsDBOpenHelper.COLUMN_INTERACTIONS_ID,
			ContactsDBOpenHelper.COLUMN_INTERACTIONS_CONTACT_ID,
			ContactsDBOpenHelper.COLUMN_INTERACTIONS_DATE,
			ContactsDBOpenHelper.COLUMN_INTERACTIONS_TEXT,
	};

	public ContactsDataSource(Context context) {
		dbhelper = new ContactsDBOpenHelper(context);

	}

	public void open() {
		Log.i(LOGTAG, "Database opened");
		database = dbhelper.getWritableDatabase();

	}

	public void close() {
		Log.i(LOGTAG, "Database closed");
		dbhelper.close();

	}

	public Contact createContact(Contact contact) {
		ContentValues values = new ContentValues();
		values.put(ContactsDBOpenHelper.COLUMN_NAME, contact.getName());
		values.put(ContactsDBOpenHelper.COLUMN_TITLE, contact.getTitle());
		values.put(ContactsDBOpenHelper.COLUMN_COMPANY, contact.getEmail());
		values.put(ContactsDBOpenHelper.COLUMN_HOME_PHONE,
				contact.getHome_phone());
		values.put(ContactsDBOpenHelper.COLUMN_WORK_PHONE,
				contact.getWork_phone());
		values.put(ContactsDBOpenHelper.COLUMN_EMAIL, contact.getEmail());
		values.put(ContactsDBOpenHelper.COLUMN_LOCATION, contact.getLocation());

		long insertid = database.insert(ContactsDBOpenHelper.TABLE_CONTACTS,
				null, values);
		contact.setId(insertid);
		return contact;

	}

	public Interaction createInteraction(Interaction interaction) {
		ContentValues values = new ContentValues();
		values.put(ContactsDBOpenHelper.COLUMN_INTERACTIONS_CONTACT_ID,
				interaction.getContactId());
		values.put(ContactsDBOpenHelper.COLUMN_INTERACTIONS_DATE,
				interaction.getDate());
		values.put(ContactsDBOpenHelper.COLUMN_INTERACTIONS_TEXT,
				interaction.getText());

		long insertid = database.insert(
				ContactsDBOpenHelper.TABLE_INTERACTIONS, null, values);
		interaction.setId(insertid);
		Log.i(LOGTAG, "Returned Interaction- " + interaction.getContactId() + " text: " + interaction.getText());
		return interaction;

	}

	public ArrayList<Interaction> findInteractionsbyContactId(long contactId) {
		ArrayList<Interaction> interactions = new ArrayList<Interaction>();
		Interaction interaction;
		Cursor cursor = database.query(ContactsDBOpenHelper.TABLE_INTERACTIONS,
				allColumnsInteractions,
				ContactsDBOpenHelper.COLUMN_INTERACTIONS_CONTACT_ID + "=?",
				new String[] { Long.toString(contactId) }, null, null, null);

		Log.i(LOGTAG, "Returned" + cursor.getCount() + " rows");

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				interaction = new Interaction();
				interaction
						.setId(cursor.getLong(cursor
								.getColumnIndex(ContactsDBOpenHelper.COLUMN_INTERACTIONS_ID)));
				interaction
						.setContactId(cursor.getLong(cursor
								.getColumnIndex(ContactsDBOpenHelper.COLUMN_INTERACTIONS_CONTACT_ID)));
				interaction
						.setDate(cursor.getString(cursor
								.getColumnIndex(ContactsDBOpenHelper.COLUMN_INTERACTIONS_DATE)));
				interaction
						.setText(cursor.getString(cursor
								.getColumnIndex(ContactsDBOpenHelper.COLUMN_INTERACTIONS_TEXT)));
				interactions.add(interaction);

			}

		}
		cursor.close();
		Log.i(LOGTAG, "Filled" + interactions.size()
				+ " interactions in arraylist");

		return interactions;

	}

	public Contact findContactbyId(long id) {

		Contact contact = null;
		Cursor cursor = database.query(ContactsDBOpenHelper.TABLE_CONTACTS,
				allColumnsContacts, ContactsDBOpenHelper.COLUMN_ID + "=?",
				new String[] { Long.toString(id) }, null, null, null);

		if (cursor != null) {

			if (cursor.moveToFirst()) {

				contact = new Contact();
				contact.setName(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_NAME)));

			}
			cursor.close();
		}

		return contact;

	}

	public ArrayList<Contact> findAllContacts() {
		ArrayList<Contact> contacts = new ArrayList<Contact>();

		Cursor cursor = database.query(ContactsDBOpenHelper.TABLE_CONTACTS,
				allColumnsContacts, null, null, null, null, null);

		Log.i(LOGTAG, "Returned" + cursor.getCount() + " rows");

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Contact contact = new Contact();
				contact.setId(cursor.getLong(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_ID)));
				contact.setName(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_NAME)));
				contact.setTitle(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_TITLE)));
				contact.setCompany(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_COMPANY)));
				contact.setHome_phone(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_HOME_PHONE)));
				contact.setWork_phone(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_WORK_PHONE)));
				contact.setLocation(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_LOCATION)));

				contacts.add(contact);

			}

		}
		cursor.close();
		return contacts;

	}
}
