package com.example.prolog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;


import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ContactListActivity extends Activity {

ContactsDataSource datasource;
ArrayList<Contact> contacts = new ArrayList<Contact>();
public static final String LOGTAG="EXPLORECA";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		Log.i(LOGTAG,"started ContactListActivity");
		datasource=new ContactsDataSource(this);
		datasource.open();
		contacts=(ArrayList<Contact>) datasource.findAll();
		Log.i(LOGTAG,"there are numer" + contacts.size());
		ListView lv = (ListView)findViewById(android.R.id.list);
		lv.setAdapter(new ContactListAdapter(this, R.id.activityContactListTextView, datasource.findAll()));
		//lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.countries)));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_list, menu);
		return true;
	}

	
	private class ContactListAdapter extends ArrayAdapter<Contact> {

		
		private ArrayList<Contact> items;
		
		public ContactListAdapter(Context context, int textViewResourceId, ArrayList<Contact> contacts) {
			super(context, textViewResourceId, contacts);
			items=contacts;
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			View row = inflater.inflate(R.layout.activity_contact_list_item, parent, false);
			
			ImageView iv = (ImageView) row.findViewById(R.id.activityContactListImageView);
			TextView tv = (TextView) row.findViewById(R.id.activityContactListTextView);
			Log.i(LOGTAG,"id :"+ items.get(position).getId());
			tv.setText(items.get(position).getName());
			iv.setImageResource(R.drawable.ic_launcher);
			return row;
		}
	}
}
