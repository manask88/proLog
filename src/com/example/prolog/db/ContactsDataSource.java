package com.example.prolog.db;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.prolog.Commons;
import com.example.prolog.model.Contact;
import com.example.prolog.model.Group;
import com.example.prolog.model.GroupContact;
import com.example.prolog.model.Interaction;
import com.example.prolog.model.InteractionContact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ContactsDataSource {

	public static final String TAG = ContactsDataSource.class.getSimpleName();

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
			ContactsDBOpenHelper.COLUMN_CUSTOM_FIELDS,
			ContactsDBOpenHelper.COLUMN_PHOTO };

	private static final String[] allColumnsInteractions = {
			ContactsDBOpenHelper.COLUMN_INTERACTIONS_ID,
			ContactsDBOpenHelper.COLUMN_INTERACTIONS_DATE,
			ContactsDBOpenHelper.COLUMN_INTERACTIONS_LOCATION,
			ContactsDBOpenHelper.COLUMN_INTERACTIONS_EVENT,
			ContactsDBOpenHelper.COLUMN_INTERACTIONS_TYPE,
			ContactsDBOpenHelper.COLUMN_INTERACTIONS_FOLLOWUP,
			ContactsDBOpenHelper.COLUMN_INTERACTIONS_TEXT };

	private static final String[] allColumnsGroups = {
			ContactsDBOpenHelper.COLUMN_GROUPS_ID,
			ContactsDBOpenHelper.COLUMN_GROUPS_NAME };

	private static final String[] allColumnsGroupContacts = {
			ContactsDBOpenHelper.COLUMN_GROUP_CONTACTS_CONTACT_ID,
			ContactsDBOpenHelper.COLUMN_GROUP_CONTACTS_GROUP_ID };

	private static final String[] allColumnsInteractionContacts = {
		ContactsDBOpenHelper.COLUMN_INTERACTION_CONTACTS_CONTACT_ID,
		ContactsDBOpenHelper.COLUMN_INTERACTION_CONTACTS_INTERACTION_ID };
	public ContactsDataSource(Context context) {
		dbhelper = new ContactsDBOpenHelper(context);

	}

	public void open() {
		Log.i(TAG, "Database opened");
		database = dbhelper.getWritableDatabase();

	}

	public void close() {
		Log.i(TAG, "Database closed");
		dbhelper.close();

	}

	public Group createGroup(Group group) {
		ContentValues values = new ContentValues();
		values.put(ContactsDBOpenHelper.COLUMN_GROUPS_NAME, group.getName());

		long insertid = database.insert(ContactsDBOpenHelper.TABLE_GROUPS,
				null, values);

		Log.i(TAG, "group created with id " + insertid);

		group.setId(insertid);
		return group;

	}
	

	
	public Interaction createInteraction(Interaction interaction) {
	long insertid = database.insert(
				ContactsDBOpenHelper.TABLE_INTERACTIONS, null, setContentValuesForInteraction(interaction));
		interaction.setId(insertid);
		Log.i(TAG, "Returned Interaction- " + interaction.getId()
				+ " text: " + interaction.getNotes());
		return interaction;

	}

	public void createGroupContacts(long groupId, long contactId) {
		ContentValues values = new ContentValues();
		values.put(ContactsDBOpenHelper.COLUMN_GROUP_CONTACTS_CONTACT_ID,
				contactId);
		values.put(ContactsDBOpenHelper.COLUMN_GROUP_CONTACTS_GROUP_ID, groupId);

		database.insert(ContactsDBOpenHelper.TABLE_GROUP_CONTACTS, null, values);

		Log.i(TAG, "groupid " + groupId + " and contact id entry created "
				+ contactId);

	}

	public void createInteractionContacts(long interactionId, long contactId) {
		ContentValues values = new ContentValues();
		values.put(ContactsDBOpenHelper.COLUMN_INTERACTION_CONTACTS_CONTACT_ID,
				contactId);
		values.put(ContactsDBOpenHelper.COLUMN_INTERACTION_CONTACTS_INTERACTION_ID, interactionId);

		database.insert(ContactsDBOpenHelper.TABLE_INTERACTION_CONTACTS, null, values);

		Log.i(TAG, "interactionId " + interactionId + " and contact id entry created "
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

		// handle custom fields
		if (contact.getAllCustomFields() != null) {

			// Convert HashMap to bytes and save it in the database
			byte[] customFieldsAsBytes = BlobHelper.object2Byte(contact
					.getAllCustomFields());
			values.put(ContactsDBOpenHelper.COLUMN_CUSTOM_FIELDS,
					customFieldsAsBytes);
		}

		// TODO potential thing to change
		if (contact.getPhoto() != null) {
			Log.i(TAG, "getPhoto != null");

			/*
			 * ByteArrayOutputStream out = new ByteArrayOutputStream();
			 * 
			 * contact.getPhoto().compress(Bitmap.CompressFormat.PNG, 100, out);
			 * 
			 * values.put(ContactsDBOpenHelper.COLUMN_PHOTO, out.toByteArray());
			 */
			values.put(ContactsDBOpenHelper.COLUMN_PHOTO,
					Commons.getBlobfromImage(contact.getPhoto()));
		} else {
			Log.i(TAG, "getPhoto is null");
		}
		long insertid = database.insert(ContactsDBOpenHelper.TABLE_CONTACTS,
				null, values);
		contact.setId(insertid);
		Log.i(TAG, "Contact created with id " + contact.getId());
		return contact;

	}

	public void deleteContactById(long id) {
		database.delete(ContactsDBOpenHelper.TABLE_CONTACTS,
				ContactsDBOpenHelper.COLUMN_ID + "=?",
				new String[] { Long.toString(id) });
	}

	public void deleteGroupById(long id) {

		deleteGroupContactsByGroupId(id);
		database.delete(ContactsDBOpenHelper.TABLE_GROUPS,
				ContactsDBOpenHelper.COLUMN_GROUPS_ID + "=?",
				new String[] { Long.toString(id) });

	}

	public void deleteGroupContactsByGroupIdAndContactId(long groupId, long  contactId) {
		database.delete(ContactsDBOpenHelper.TABLE_GROUP_CONTACTS,
				ContactsDBOpenHelper.COLUMN_GROUP_CONTACTS_GROUP_ID + "=?" + " and "+ContactsDBOpenHelper.COLUMN_GROUP_CONTACTS_CONTACT_ID + "=?",
				new String[] { Long.toString(groupId),Long.toString(contactId) });
	}
	public void deleteInteractionContactsByInteractionIdAndContactId(long interactionId, long  contactId) {
		database.delete(ContactsDBOpenHelper.TABLE_INTERACTION_CONTACTS,
				ContactsDBOpenHelper.COLUMN_INTERACTION_CONTACTS_INTERACTION_ID + "=?" + " and "+ContactsDBOpenHelper.COLUMN_INTERACTION_CONTACTS_CONTACT_ID + "=?",
				new String[] { Long.toString(interactionId),Long.toString(contactId) });
	}
	
	public void deleteGroupContactsByGroupId(long id) {
		database.delete(ContactsDBOpenHelper.TABLE_GROUP_CONTACTS,
				ContactsDBOpenHelper.COLUMN_GROUP_CONTACTS_GROUP_ID + "=?",
				new String[] { Long.toString(id) });
	}

	public void deleteGroupContactsByContactId(long id) {
		database.delete(ContactsDBOpenHelper.TABLE_GROUP_CONTACTS,
				ContactsDBOpenHelper.COLUMN_GROUP_CONTACTS_CONTACT_ID + "=?",
				new String[] { Long.toString(id) });
	}

	/*
	public void deleteInteractionsByContactId(long id) {
		database.delete(ContactsDBOpenHelper.TABLE_INTERACTIONS,
				ContactsDBOpenHelper.COLUMN_INTERACTIONS_CONTACT_ID + "=?",
				new String[] { Long.toString(id) });
	}
*/
	
	public void deleteInteractionsByContactId(long id) {
	
	}
	
	public void deleteInteractionByInteractionId(long id) {
		database.delete(ContactsDBOpenHelper.TABLE_INTERACTIONS,
				ContactsDBOpenHelper.COLUMN_INTERACTIONS_ID + "=?",
				new String[] { Long.toString(id) });
	}

	
	public Contact updateContact(Contact contact) {
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
		values.put(ContactsDBOpenHelper.COLUMN_PHOTO,
				Commons.getBlobfromImage(contact.getPhoto()));

		if (contact.getAllCustomFields() != null
				&& contact.getAllCustomFields().size() > 0) {
			// handle custom fields
			if (contact.getAllCustomFields() != null) {

				// Convert HashMap to bytes and save it in the database
				byte[] customFieldsAsBytes = BlobHelper.object2Byte(contact
						.getAllCustomFields());
				values.put(ContactsDBOpenHelper.COLUMN_CUSTOM_FIELDS,
						customFieldsAsBytes);

			}
		}

		database.update(ContactsDBOpenHelper.TABLE_CONTACTS, values,
				ContactsDBOpenHelper.COLUMN_ID + "=?",
				new String[] { Long.toString(contact.getId()) });

		Log.i(TAG, "Contact  with id " + contact.getId() + " updated");
		return contact;

	}

	public Group updateGroup(Group group) {
		ContentValues values = new ContentValues();
		values.put(ContactsDBOpenHelper.COLUMN_GROUPS_NAME, group.getName());

		database.update(ContactsDBOpenHelper.TABLE_GROUPS, values,
				ContactsDBOpenHelper.COLUMN_GROUPS_ID + "=?",
				new String[] { Long.toString(group.getId()) });

		Log.i(TAG, "Group  with id " + group.getId() + " updated");
		return group;

	}

	public Interaction updateInteractionByInteractionId(Interaction interaction) {
	

		database.update(ContactsDBOpenHelper.TABLE_INTERACTIONS, setContentValuesForInteraction(interaction),
				ContactsDBOpenHelper.COLUMN_INTERACTIONS_ID + "=?",
				new String[] { Long.toString(interaction.getId()) });

		Log.i(TAG, "Interaction  with id " + interaction.getId() + " updated");
		return interaction;
	}

	

	public ArrayList<Group> findAllGroups() {
		ArrayList<Group> groups = new ArrayList<Group>();
		Group group;
		Cursor cursor = database.query(ContactsDBOpenHelper.TABLE_GROUPS,
				allColumnsGroups, null, null, null, null, null);

		Log.i(TAG, "Returned" + cursor.getCount() + " rows");

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
		Log.i(TAG, "Filled" + groups.size() + " groups in arraylist");

		return groups;

	}
	
	
/*
	public ArrayList<Interaction> findInteractionsbyContactId(long contactId) {
		ArrayList<Interaction> interactions = new ArrayList<Interaction>();
		Interaction interaction;
		Cursor cursor = database.query(ContactsDBOpenHelper.TABLE_INTERACTIONS,
				allColumnsInteractions,
				ContactsDBOpenHelper.COLUMN_INTERACTIONS_CONTACT_IDS + "=?",
				new String[] { Long.toString(contactId) }, null, null, null);

		Log.i(TAG, "Returned" + cursor.getCount() + " rows");

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
		Log.i(TAG, "Filled" + interactions.size()
				+ " interactions in arraylist");

		return interactions;

	}
*/
	private ArrayList<GroupContact> findContactsIdbyGroupId(long groupId) {
		ArrayList<GroupContact> groupContacts = new ArrayList<GroupContact>();
		GroupContact groupContact;
		Cursor cursor = database.query(
				ContactsDBOpenHelper.TABLE_GROUP_CONTACTS,
				allColumnsGroupContacts,
				ContactsDBOpenHelper.COLUMN_GROUP_CONTACTS_GROUP_ID + "=?",
				new String[] { Long.toString(groupId) }, null, null, null);

		Log.i(TAG, "Returned" + cursor.getCount() + " rows");

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				groupContact = new GroupContact();
				groupContact
						.setGroupId(cursor.getLong(cursor
								.getColumnIndex(ContactsDBOpenHelper.COLUMN_GROUP_CONTACTS_GROUP_ID)));
				groupContact
						.setContactId(cursor.getLong(cursor
								.getColumnIndex(ContactsDBOpenHelper.COLUMN_GROUP_CONTACTS_CONTACT_ID)));

				groupContacts.add(groupContact);

			}

		}
		cursor.close();
		Log.i(TAG, "Filled" + groupContacts.size()
				+ " groupContacts in arraylist");

		return groupContacts;

	}
	
	public ArrayList<InteractionContact> findContactIdsbyInteractionId(long interactionId) {
		ArrayList<InteractionContact> interactionContacts = new ArrayList<InteractionContact>();
		InteractionContact interactionContact;
		Cursor cursor = database.query(
				ContactsDBOpenHelper.TABLE_INTERACTION_CONTACTS,
				allColumnsInteractionContacts,
				ContactsDBOpenHelper.COLUMN_INTERACTION_CONTACTS_INTERACTION_ID + "=?",
				new String[] { Long.toString(interactionId) }, null, null, null);

		Log.i(TAG, "Returned" + cursor.getCount() + " rows");

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				interactionContact = new InteractionContact();
				interactionContact
						.setInteractionId(cursor.getLong(cursor
								.getColumnIndex(ContactsDBOpenHelper.COLUMN_INTERACTION_CONTACTS_INTERACTION_ID)));
				interactionContact
						.setContactId(cursor.getLong(cursor
								.getColumnIndex(ContactsDBOpenHelper.COLUMN_INTERACTION_CONTACTS_CONTACT_ID)));

				interactionContacts.add(interactionContact);

			}

		}
		cursor.close();
		Log.i(TAG, "Filled" + interactionContacts.size()
				+ " interactionContacts in arraylist");

		return interactionContacts;
	}


	private ArrayList<InteractionContact> findInteractionIdsbyContactId(long contactId) {
		ArrayList<InteractionContact> contactInteractions = new ArrayList<InteractionContact>();
		InteractionContact contactInteraction;
		Cursor cursor = database.query(
				ContactsDBOpenHelper.TABLE_INTERACTION_CONTACTS,
				allColumnsInteractionContacts,
				ContactsDBOpenHelper.COLUMN_INTERACTION_CONTACTS_CONTACT_ID + "=?",
				new String[] { Long.toString(contactId) }, null, null, null);

		Log.i(TAG, "Returned" + cursor.getCount() + " rows");

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				contactInteraction = new InteractionContact();
				contactInteraction
						.setContactId(cursor.getLong(cursor
								.getColumnIndex(ContactsDBOpenHelper.COLUMN_INTERACTION_CONTACTS_CONTACT_ID)));
				contactInteraction
						.setInteractionId(cursor.getLong(cursor
								.getColumnIndex(ContactsDBOpenHelper.COLUMN_INTERACTION_CONTACTS_INTERACTION_ID)));

				contactInteractions.add(contactInteraction);

			}

		}
		cursor.close();
		Log.i(TAG, "Filled" + contactInteractions.size()
				+ " contactInteractions in arraylist");

		return contactInteractions;

	}
	
	public ArrayList<Interaction> findInteractionsbyContactId(long contactId) {
		ArrayList<InteractionContact> interactionContacts;
		ArrayList<Interaction> interactions = new ArrayList<Interaction>();
		Interaction interaction;
		interactionContacts = findInteractionIdsbyContactId(contactId);

		for (InteractionContact interactionContact : interactionContacts) {
			interaction = findInteractionbyId(interactionContact.getInteractionId());
			interactions.add(interaction);
		}
		return interactions;
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
							
	public ArrayList<Contact> findContactsbyInteractionId(long interactionId) {
		ArrayList<InteractionContact> interactionContacts;
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		Contact contact;

		interactionContacts = findContactIdsbyInteractionId(interactionId);

		for (InteractionContact interactionContact : interactionContacts) {
			contact = findContactbyId(interactionContact.getContactId());
			contacts.add(contact);
		}
		return contacts;

	}
	
	

	public GroupContact findGroupContactbyGroupIdAndContactId(long groupId, long  contactId) {

		GroupContact groupContact = null;
		Cursor cursor = database.query(ContactsDBOpenHelper.TABLE_GROUP_CONTACTS,
				allColumnsGroupContacts, ContactsDBOpenHelper.COLUMN_GROUP_CONTACTS_GROUP_ID + "=?" + " and "+ContactsDBOpenHelper.COLUMN_GROUP_CONTACTS_CONTACT_ID + "=?",
				new String[] { Long.toString(groupId),Long.toString(contactId) }, null, null, null);
		Log.i(TAG, "Returned" + cursor.getCount() + " rows on findGroupContactbyGroupIdAndContactId");
		
		if (cursor != null) {

			if (cursor.moveToFirst()) {
				Log.i(TAG, "found something rows on findGroupContactbyGroupIdAndContactId");

				groupContact = new GroupContact();
				groupContact.setContactId(cursor.getLong(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_GROUP_CONTACTS_CONTACT_ID)));

				groupContact.setGroupId(cursor.getLong(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_GROUP_CONTACTS_GROUP_ID)));

			}
			cursor.close();
		}

		return groupContact;

	}
	
	public InteractionContact findInteractionContactbyInteractionIdAndContactId(long interactionId, long  contactId) {

		InteractionContact interactionContact = null;
		Cursor cursor = database.query(ContactsDBOpenHelper.TABLE_INTERACTION_CONTACTS,
				allColumnsInteractionContacts, ContactsDBOpenHelper.COLUMN_INTERACTION_CONTACTS_INTERACTION_ID + "=?" + " and "+ContactsDBOpenHelper.COLUMN_INTERACTION_CONTACTS_CONTACT_ID + "=?",
				new String[] { Long.toString(interactionId),Long.toString(contactId) }, null, null, null);
		Log.i(TAG, "Returned" + cursor.getCount() + " rows on findInteractionContactbyInteractionIdAndContactId");
		
		if (cursor != null) {

			if (cursor.moveToFirst()) {
				Log.i(TAG, "found something rows on findInteractionContactbyInteractionIdAndContactId");

				interactionContact = new InteractionContact();
				interactionContact.setContactId(cursor.getLong(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_INTERACTION_CONTACTS_CONTACT_ID)));

				interactionContact.setInteractionId(cursor.getLong(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_INTERACTION_CONTACTS_INTERACTION_ID)));

			}
			cursor.close();
		}

		return interactionContact;

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
	public Interaction findInteractionbyId(long id) {

		Interaction interaction = null;
		Cursor cursor = database.query(ContactsDBOpenHelper.TABLE_INTERACTIONS,
				allColumnsInteractions, ContactsDBOpenHelper.COLUMN_INTERACTIONS_ID + "=?",
				new String[] { Long.toString(id) }, null, null, null);

		if (cursor != null) {

			if (cursor.moveToFirst()) {

				interaction = new Interaction();
				interaction.setDate(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_INTERACTIONS_DATE)));

				interaction.setNotes(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_INTERACTIONS_TEXT)));
				interaction.setId(cursor.getLong(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_INTERACTIONS_ID)));
				interaction.setLocation(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_INTERACTIONS_LOCATION)));
				interaction.setEvent(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_INTERACTIONS_EVENT)));
				interaction.setType(cursor.getString(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_INTERACTIONS_TYPE)));
				interaction.setFollowUp(cursor.getInt(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_INTERACTIONS_FOLLOWUP))==1?true:false);
			}
			cursor.close();
		}

		return interaction;

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

				// get custom fields
				byte[] custom_fields = cursor
						.getBlob(cursor
								.getColumnIndex(ContactsDBOpenHelper.COLUMN_CUSTOM_FIELDS));
				if (custom_fields != null)
					contact.setCustomFields(BlobHelper
							.byte2HashMap(custom_fields));

				byte[] blob = cursor.getBlob(cursor
						.getColumnIndex(ContactsDBOpenHelper.COLUMN_PHOTO));
				if (blob != null) {
					contact.setPhoto(Commons.getImageFromBlob(blob));

					Log.i(TAG, "found pic");
				} else
					Log.i(TAG, "not found pic");

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

		Log.i(TAG, "Returned" + cursor.getCount() + " rows");

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

				// get custom fields
				byte[] custom_fields = cursor
						.getBlob(cursor
								.getColumnIndex(ContactsDBOpenHelper.COLUMN_CUSTOM_FIELDS));
				if (custom_fields != null)
					contact.setCustomFields(BlobHelper
							.byte2HashMap(custom_fields));

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

	public void createGroupContacts_r(long groupId, long contactId) {
		
		if (findGroupContactbyGroupIdAndContactId(groupId,contactId)==null)
			{createGroupContacts(groupId,contactId);
			
			Log.i(TAG, "null on createGroupContacts_r");

			}
		
		Log.i(TAG, "not null on createGroupContacts_r");

		
	}

	public void deleteGroupContacts_r(long groupId, long contactId) {

		if (findGroupContactbyGroupIdAndContactId(groupId,contactId)!=null)
			{deleteGroupContactsByGroupIdAndContactId(groupId, contactId);
			Log.i(TAG, "not null on deleteGroupContacts_r");
			
			}
		Log.i(TAG, "null on deleteGroupContacts_r");

	}

	public void createInteractionContacts_r(long interactionId, long contactId) {
		
		if (findInteractionContactbyInteractionIdAndContactId(interactionId,contactId)==null)
			{createInteractionContacts(interactionId,contactId);
			
			Log.i(TAG, "null on createInteractionContacts_r");

			}
		
		Log.i(TAG, "not null on createInteractionContacts_r");

		
	}

	public void deleteInteractionContacts_r(long interactionId, long contactId) {

		if (findInteractionContactbyInteractionIdAndContactId(interactionId,contactId)!=null)
			{deleteInteractionContactsByInteractionIdAndContactId(interactionId, contactId);
			Log.i(TAG, "not null on deleteInteractionContacts_r");
			
			}
		Log.i(TAG, "null on deleteInteractionContacts_r");

	}

	
	ContentValues setContentValuesForInteraction (   Interaction interaction  ){
		ContentValues values = new ContentValues();

		
		values.put(ContactsDBOpenHelper.COLUMN_INTERACTIONS_DATE,
				interaction.getDate());
		values.put(ContactsDBOpenHelper.COLUMN_INTERACTIONS_TEXT,
				interaction.getNotes());
		values.put(ContactsDBOpenHelper.COLUMN_INTERACTIONS_LOCATION,
				interaction.getLocation());
		values.put(ContactsDBOpenHelper.COLUMN_INTERACTIONS_EVENT,
				interaction.getEvent());
		values.put(ContactsDBOpenHelper.COLUMN_INTERACTIONS_TYPE,
				interaction.getType());
		values.put(ContactsDBOpenHelper.COLUMN_INTERACTIONS_FOLLOWUP,
				interaction.isFollowUp()?1:0);
	
		
		return values;
		
	}
	
}
