package com.example.prolog.db;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.prolog.Commons;
import com.example.prolog.model.Contact;
import com.example.prolog.model.Group;
import com.example.prolog.model.GroupContact;
import com.example.prolog.model.Interaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ContactsDataSource {

	public static final String LOGTAG = "EXPLORECA";

	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;

	private static final String[] allColumnsContacts = {
			ContactsDBOpenHelper.COLUMN_ID, ContactsDBOpenHelper.COLUMN_NAME,
			ContactsDBOpenHelper.COLUMN_TITLE,
			ContactsDBOpenHelper.COLUMN_COMPANY,
			ContactsDBOpenHelper.COLUMN_HOME_PHONE,
			ContactsDBOpenHelper.COLUMN_WORK_PHONE,
			ContactsDBOpenHelper.COLUMN_EMAIL,
			ContactsDBOpenHelper.COLUMN_LOCATION,
			ContactsDBOpenHelper.COLUMN_PHOTO };

	private static final String[] allColumnsInteractions = {
			ContactsDBOpenHelper.COLUMN_INTERACTIONS_ID,
			ContactsDBOpenHelper.COLUMN_INTERACTIONS_CONTACT_ID,
			ContactsDBOpenHelper.COLUMN_INTERACTIONS_DATE,
			ContactsDBOpenHelper.COLUMN_INTERACTIONS_TEXT };

	private static final String[] allColumnsGroups = {
			ContactsDBOpenHelper.COLUMN_GROUPS_ID,
			ContactsDBOpenHelper.COLUMN_GROUPS_NAME };

	private static final String[] allColumnsGroupContacts = {
			ContactsDBOpenHelper.COLUMN_GROUP_CONTACT_ID,
			ContactsDBOpenHelper.COLUMN_GROUP_GROUP_ID };

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

	public Group createGroup(Group group) {
		ContentValues values = new ContentValues();
		values.put(ContactsDBOpenHelper.COLUMN_GROUPS_NAME, group.getName());

		long insertid = database.insert(ContactsDBOpenHelper.TABLE_GROUPS,
				null, values);

		Log.i(LOGTAG, "group created with id " + insertid);

		group.setId(insertid);
		return group;

	}

	public void createGroupContacts(long groupId, long contactId) {
		ContentValues values = new ContentValues();
		values.put(ContactsDBOpenHelper.COLUMN_GROUP_CONTACT_ID, contactId);
		values.put(ContactsDBOpenHelper.COLUMN_GROUP_GROUP_ID, groupId);

		database.insert(ContactsDBOpenHelper.TABLE_GROUP_CONTACTS, null, values);

		Log.i(LOGTAG, "groupid " + groupId + " and contact id entry created "
				+ contactId);

	}

	public Contact createContact(Contact contact) {
		ContentValues values = new ContentValues();
		values.put(ContactsDBOpenHelper.COLUMN_NAME, contact.getName());
		values.put(ContactsDBOpenHelper.COLUMN_TITLE, contact.getTitle());
		values.put(ContactsDBOpenHelper.COLUMN_COMPANY, contact.getCompany());
		values.put(ContactsDBOpenHelper.COLUMN_HOME_PHONE,
				contact.getHome_phone());
		values.put(ContactsDBOpenHelper.COLUMN_WORK_PHONE,
				contact.getWork_phone());
		values.put(ContactsDBOpenHelper.COLUMN_EMAIL, contact.getEmail());
		values.put(ContactsDBOpenHelper.COLUMN_LOCATION, contact.getLocation());

		if (contact.getPhoto() != null) {
			/*
			 * ByteArrayOutputStream out = new ByteArrayOutputStream();
			 * 
			 * contact.getPhoto().compress(Bitmap.CompressFormat.PNG, 100, out);
			 * 
			 * values.put(ContactsDBOpenHelper.COLUMN_PHOTO, out.toByteArray());
			 */
			values.put(ContactsDBOpenHelper.COLUMN_PHOTO,
					Commons.getBlobfromImage(contact.getPhoto()));
		}

		long insertid = database.insert(ContactsDBOpenHelper.TABLE_CONTACTS,
				null, values);
		contact.setId(insertid);
		Log.i(LOGTAG, "Contact created with id " + contact.getId());
		return contact;

	}

	public void deleteContactById(long id) {
		database.delete(ContactsDBOpenHelper.TABLE_CONTACTS,
				ContactsDBOpenHelper.COLUMN_ID + "=?",
				new String[] { Long.toString(id) });
	}

	public void deleteInteractionsByContactId(long id) {
		database.delete(ContactsDBOpenHelper.TABLE_INTERACTIONS,
				ContactsDBOpenHelper.COLUMN_INTERACTIONS_CONTACT_ID + "=?",
				new String[] { Long.toString(id) });
	}

	public Contact updateContact(Contact contact) {
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
		values.put(ContactsDBOpenHelper.COLUMN_PHOTO,
				Commons.getBlobfromImage(contact.getPhoto()));
		database.update(ContactsDBOpenHelper.TABLE_CONTACTS, values,
				ContactsDBOpenHelper.COLUMN_ID + "=?",
				new String[] { Long.toString(contact.getId()) });

		Log.i(LOGTAG, "Contact  with id " + contact.getId() + " updated");
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
		Log.i(LOGTAG, "Returned Interaction- " + interaction.getContactId()
				+ " text: " + interaction.getText());
		return interaction;

	}

	public ArrayList<Group> findAllGroups() {
		ArrayList<Group> groups = new ArrayList<Group>();
		Group group;
		Cursor cursor = database.query(ContactsDBOpenHelper.TABLE_GROUPS,
				allColumnsGroups, null, null, null, null, null);

		Log.i(LOGTAG, "Returned" + cursor.getCount() + " rows");

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				group = new Group();
				group.setId(cursor.getLong(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_GROUPS_ID)));
				group.setName(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_GROUPS_NAME)));

				groups.add(group);

			}

		}
		cursor.close();
		Log.i(LOGTAG, "Filled" + groups.size() + " groups in arraylist");

		return groups;

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

	private ArrayList<GroupContact> findContactsIdbyGroupId(long groupId) {
		ArrayList<GroupContact> groupContacts = new ArrayList<GroupContact>();
		GroupContact groupContact;
		Cursor cursor = database.query(
				ContactsDBOpenHelper.TABLE_GROUP_CONTACTS,
				allColumnsGroupContacts,
				ContactsDBOpenHelper.COLUMN_GROUP_GROUP_ID + "=?",
				new String[] { Long.toString(groupId) }, null, null, null);

		Log.i(LOGTAG, "Returned" + cursor.getCount() + " rows");

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				groupContact = new GroupContact();
				groupContact
						.setGroupId(cursor.getLong(cursor
								.getColumnIndex(ContactsDBOpenHelper.COLUMN_GROUP_GROUP_ID)));
				groupContact
						.setContactId(cursor.getLong(cursor
								.getColumnIndex(ContactsDBOpenHelper.COLUMN_GROUP_CONTACT_ID)));

				groupContacts.add(groupContact);

			}

		}
		cursor.close();
		Log.i(LOGTAG, "Filled" + groupContacts.size()
				+ " groupContacts in arraylist");

		return groupContacts;

	}

	public ArrayList<Contact> findContactsbyGroupId(long groupId) {
		ArrayList<GroupContact> groupContacts;
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		Contact contact;

		groupContacts = findContactsIdbyGroupId(groupId);

		for (GroupContact groupContact : groupContacts) {
			contact = findContactbyId(groupContact.getContactId());
			contacts.add(contact);
		}
		return contacts;

	}

	public Group findGroupbyId(long id) {

		Group group = null;
		Cursor cursor = database.query(ContactsDBOpenHelper.TABLE_GROUPS,
				allColumnsGroups, ContactsDBOpenHelper.COLUMN_GROUPS_ID + "=?",
				new String[] { Long.toString(id) }, null, null, null);

		if (cursor != null) {

			if (cursor.moveToFirst()) {

				group = new Group();
				group.setName(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_GROUPS_NAME)));

				group.setId(cursor.getLong(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_GROUPS_ID)));

			}
			cursor.close();
		}

		return group;

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
				contact.setEmail(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_EMAIL)));
				contact.setWork_phone(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_WORK_PHONE)));
				contact.setHome_phone(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_HOME_PHONE)));
				contact.setId(cursor.getLong(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_ID)));
				contact.setCompany(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_COMPANY)));
				contact.setLocation(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_LOCATION)));
				contact.setTitle(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_TITLE)));

				byte[] blob = cursor.getBlob(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_PHOTO));
				if (blob != null)
					contact.setPhoto(Commons.getImageFromBlob(blob));

				/**
				 * ByteArrayOutputStream out = new ByteArrayOutputStream();
				 * 
				 * contact.getPhoto().compress(Bitmap.CompressFormat.PNG, 100,
				 * out);
				 * 
				 * values.put(ContactsDBOpenHelper.COLUMN_PHOTO,
				 * out.toByteArray());
				 */

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
				byte[] blob = cursor.getBlob(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_PHOTO));
				if (blob != null)
					contact.setPhoto(Commons.getImageFromBlob(blob));
				contacts.add(contact);

			}

		}
		cursor.close();
		return contacts;

	}
}
