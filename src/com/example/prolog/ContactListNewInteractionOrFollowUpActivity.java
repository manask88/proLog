package com.example.prolog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactListNewInteractionOrFollowUpActivity extends Activity {
	private Context context = this;
	private ContactsDataSource datasource;
	private ArrayList<Contact> contacts;
	private ArrayList<Contact> contactsSearchResult;
	private SearchView searchView;
	private ListView lv;
	private TextView textView;
	public static final String TAG = ContactListNewInteractionOrFollowUpActivity.class
			.getSimpleName();
	private long groupId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setTitle("Contacts");
		setContentView(R.layout.activity_contact_list_new_interaction);

		Log.i(TAG, "started ContactListGroupAddContactActivity");
		datasource = new ContactsDataSource(this);
		textView = (TextView) findViewById(R.id.textView);
		textView.setText("Contacts");
		searchView = (SearchView) findViewById(R.id.searchView);
		lv = (ListView) findViewById(android.R.id.list);

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
		datasource.open();

		contacts = (ArrayList<Contact>) datasource.findAllContacts();

		contactsSearchResult = new ArrayList<Contact>();

		for (Contact contact : contacts) {
			contactsSearchResult.add(contact);
		}

		Collections.sort(contactsSearchResult, new ContactsCompareByName());
		TextView empty = (TextView) findViewById(R.id.empty);
		
		lv.setEmptyView(empty);
		lv.setAdapter(new ContactListAdapter(this,
				R.id.activityContactListTextView, contactsSearchResult));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				int next_action_id=getIntent().getExtras().getInt(Commons.NEXT_ACTION);		
				//Intent i = new Intent(context,NewInteractionActivity.class);
				Intent intent = new Intent(context, MyTabActivity.class);
				intent.putExtra(Commons.CONTACT_ID, contactsSearchResult.get(position).getId());
				
				if (next_action_id==Commons.NEXT_ACTION_NEW_INTERACTION)
				intent.putExtra(Commons.TAB_ID, Commons.TAB_ID_ADD_INTERACTION);
				if (next_action_id==Commons.NEXT_ACTION_NEW_FOLLOW_UP)
				intent.putExtra(Commons.TAB_ID, Commons.TAB_ID_NEW_FOLLOW_UP);

				startActivity(intent);
				finish();

			}

		});
		// lv.setAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.countries)));

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextChange(String newText) {
				contactsSearchResult.clear();
				for (int i = 0; i < contacts.size(); i++) {
					if (contacts.get(i).getName().toLowerCase()
							.contains(newText.toString().toLowerCase())
							|| contacts.get(i).getCompany().toLowerCase()
									.contains(newText.toString().toLowerCase())
							|| contacts.get(i).getTitle().toLowerCase()
									.contains(newText.toString().toLowerCase())) {
						contactsSearchResult.add(contacts.get(i));
					}
				}

				Collections.sort(contactsSearchResult,
						new ContactsCompareByName());

				
				
				
				lv.setAdapter(new ContactListAdapter(
						ContactListNewInteractionOrFollowUpActivity.this,
						R.id.activityContactListTextView, contactsSearchResult));
				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				return true;
			}
		});

		
	}

	@Override
	protected void onPause() {

		super.onPause();
		datasource.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_list, menu);
		return true;
	}

	
}
