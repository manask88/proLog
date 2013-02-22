package com.example.prolog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;

import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

public class SyncActivity extends Activity{

	public static final String LOGTAG="EXPLORECA";

	private final List<Contact> spinnerContent = new LinkedList<Contact>();
	List<ExpandListChild> contacts = new LinkedList<ExpandListChild>();
	private ExpandListAdapter ExpAdapter;
	private ArrayList<ExpandListGroup> ExpListItems;
	private ExpandableListView ExpandList;
	private Button buttonSkip;
	private Button buttonSync;

	private Context context = this;
	ContactsDataSource datasource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync);
		datasource=new ContactsDataSource(this);
		datasource.open();

		buttonSkip = (Button) findViewById(R.id.buttonSyncSkip);
		buttonSkip.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					startActivity(new Intent(context,MainActivity.class));					
				}
			});
		    
		buttonSync = (Button) findViewById(R.id.buttonSyncSync);
		buttonSync.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					datasource.open();	
					/*Contact contact=new Contact();
					contact.setName("test");
					createContact(contact);*/
					/*queryAllRawContacts();*/
				}
			});
		
		
		ExpandList = (ExpandableListView) findViewById(R.id.ExpList);
	    ExpListItems = SetStandardGroups();
	    ExpAdapter = new ExpandListAdapter(SyncActivity.this, ExpListItems);
	    ExpandList.setAdapter(ExpAdapter);
	    }
	 
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		datasource.open();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		datasource.close();
	}
	
	private void createContact(Contact contact) {
		contact = datasource.create(contact);
		Log.i(LOGTAG, "Contact created with id "+ contact.getId());
	}
	
	private void queryAllRawContacts() {
		
		final String[] projection = new String[] {
				RawContacts.CONTACT_ID,					// the contact id column
				RawContacts.DELETED						// column if this contact is deleted
		};
		
		final Cursor rawContacts = managedQuery(
				RawContacts.CONTENT_URI,				// the uri for raw contact provider
				projection,	
				null,									// selection = null, retrieve all entries
				null,									// not required because selection does not contain parameters
				null);									// do not order

		final int contactIdColumnIndex = rawContacts.getColumnIndex(RawContacts.CONTACT_ID);
		final int deletedColumnIndex = rawContacts.getColumnIndex(RawContacts.DELETED);
		
		int cont=0;
		if(rawContacts.moveToFirst()) {					// move the cursor to the first entry
			while(!rawContacts.isAfterLast()) {			// still a valid entry left?
				final int contactId = rawContacts.getInt(contactIdColumnIndex);
				final boolean deleted = (rawContacts.getInt(deletedColumnIndex) == 1);
				if(!deleted) {
					Log.i(LOGTAG, "Contact created with id "+ contactId);
					
					  ExpandListChild ch2_1 = new ExpandListChild();
				        ch2_1.setName(queryDetailsForContactEntry(contactId).getName());
				        ch2_1.setTag(null);
					contacts.add(ch2_1);
					//createContact(queryDetailsForContactEntry(contactId));
				}
				rawContacts.moveToNext();				// move to the next entry
				if (cont==50) break; //hardcoded to get out of this loop if there are many contacts
				cont++;
			}
		}

		rawContacts.close();
	}

	private Contact queryDetailsForContactEntry(int contactId) {
		final String[] projection = new String[] {
				Contacts.DISPLAY_NAME,					// the name of the contact
				Contacts.PHOTO_ID/*,*/						// the id of the column in the data table for the image
				/*Email.DATA*/
		};

		final Cursor cursorContact = managedQuery(
				Contacts.CONTENT_URI,
				projection,
				Contacts._ID + "=?",						// filter entries on the basis of the contact id
				new String[]{String.valueOf(contactId)},	// the parameter to which the contact id column is compared to
				null);
		
		if(cursorContact.moveToFirst()) {
			String name = cursorContact.getString(
					cursorContact.getColumnIndex(Contacts.DISPLAY_NAME));
			/*String photoId = contact.getString(
					contact.getColumnIndex(Contacts.PHOTO_ID));*/
			/*Bitmap photo;*/
			/*String emailtype =  contact.getString(
					contact.getColumnIndex(Email.TYPE));*/
			/*String email =  cursorContact.getString(
					cursorContact.getColumnIndex(Email.DATA));	*/
			/*if(photoId != null) {
				photo = queryContactBitmap(photoId);
			} else {
				photo = null;
			}*/
			cursorContact.close();
			Contact contact=new Contact();
			contact.setName(name);
			/*contact.setEmail(email)*/
;			return contact;
		}
		cursorContact.close();
		return null;
	}
	
	
	
	
	
	
	    public ArrayList<ExpandListGroup> SetStandardGroups() {
	    	ArrayList<ExpandListGroup> list = new ArrayList<ExpandListGroup>();
	    	ArrayList<ExpandListChild> list2 = new ArrayList<ExpandListChild>();
	        ExpandListGroup gru1 = new ExpandListGroup();
	        gru1.setName("Phone Contacts");
	        /*ExpandListChild ch1_1 = new ExpandListChild();
	        ch1_1.setName("contact1");
	        ch1_1.setTag(null);
	        list2.add(ch1_1);
	        ExpandListChild ch1_2 = new ExpandListChild();
	        ch1_2.setName("contact2");
	        ch1_2.setTag(null);
	        list2.add(ch1_2);
	        ExpandListChild ch1_3 = new ExpandListChild();
	        ch1_3.setName("contact3");
	        ch1_3.setTag(null);
	        list2.add(ch1_3);*/
	        queryAllRawContacts();
	        gru1.setItems(contacts);
	        list2 = new ArrayList<ExpandListChild>();
	        
	        ExpandListGroup gru2 = new ExpandListGroup();
	        gru2.setName("LinkedIn");
	        ExpandListChild ch2_1 = new ExpandListChild();
	        ch2_1.setName("Cont1");
	        ch2_1.setTag(null);
	        list2.add(ch2_1);
	        ExpandListChild ch2_2 = new ExpandListChild();
	        ch2_2.setName("Cont2");
	        ch2_2.setTag(null);
	        list2.add(ch2_2);
	        ExpandListChild ch2_3 = new ExpandListChild();
	        ch2_3.setName("Cont3");
	        ch2_3.setTag(null);
	        list2.add(ch2_3);
	        //gru2.setItems(list2);
	        list.add(gru1);
	        list.add(gru2);
	        
	        return list;
	    }

	   
	    
	}
