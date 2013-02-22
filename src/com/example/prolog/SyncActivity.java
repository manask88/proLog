package com.example.prolog;

import java.util.ArrayList;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

public class SyncActivity extends Activity{

	public static final String LOGTAG="EXPLORECA";

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
					Contact contact=new Contact();
					contact.setName("test");
					createContact(contact);
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
	
	    public ArrayList<ExpandListGroup> SetStandardGroups() {
	    	ArrayList<ExpandListGroup> list = new ArrayList<ExpandListGroup>();
	    	ArrayList<ExpandListChild> list2 = new ArrayList<ExpandListChild>();
	        ExpandListGroup gru1 = new ExpandListGroup();
	        gru1.setName("Phone Contacts");
	        ExpandListChild ch1_1 = new ExpandListChild();
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
	        list2.add(ch1_3);
	        gru1.setItems(list2);
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
	        gru2.setItems(list2);
	        list.add(gru1);
	        list.add(gru2);
	        
	        return list;
	    }

	   
	    
	}
