package com.example.prolog;

import java.util.ArrayList;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;
import com.example.prolog.model.Group;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ViewGroupActivity extends Activity {
	private ListView lv;
	private Context context = this;
	private ContactsDataSource datasource;
	private TextView textView;
	private long groupId;
	private ArrayList<Contact> contacts;
	private static final String TAG = ViewGroupActivity.class.getSimpleName();
	private Button addContact;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setTitle("Add New Contact");
		setContentView(R.layout.activity_group_view);

		datasource = new ContactsDataSource(context);

		Bundle b = getIntent().getExtras();
		groupId = b.getLong("groupId");


		lv = (ListView) findViewById(android.R.id.list);
		textView= (TextView) findViewById(R.id.textViewGroupName);
		datasource.open();
		textView.setText("Group "+datasource.findGroupbyId(groupId).getName());
		datasource.close();
		addContact = (Button) findViewById(R.id.buttonAdd);
		addContact.setOnClickListener(new View.OnClickListener() { public
		  void onClick(View v) { 		
			Intent i = new Intent(context, ContactListGroupAddContactActivity.class);
			i.putExtra("groupId", groupId);
			startActivity(i);
 } });
		 
	}

	@Override
	protected void onResume() {
		super.onResume();
		datasource.open();
		contacts = datasource.findContactsbyGroupId(groupId);
		lv.setAdapter(new GroupListAdapter(this,
				R.id.activityContactListTextView, contacts));
	}

	@Override
	protected void onPause() {
		super.onPause();
		datasource.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_contact, menu);
		return true;
	}

	private class GroupListAdapter extends ArrayAdapter<Contact> {

		private ArrayList<Contact> items;

		public GroupListAdapter(Context context, int textViewResourceId,
				ArrayList<Contact> contacts) {
			super(context, textViewResourceId, contacts);
			items = contacts;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View row = inflater.inflate(R.layout.activity_contact_list_item,
					parent, false);

			TextView tv = (TextView) row
					.findViewById(R.id.activityContactListTextView);
			Log.i(TAG, "id :" + items.get(position).getId());
			tv.setText(items.get(position).getName());

			return row;
		}
	}

}
