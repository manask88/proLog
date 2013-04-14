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

public class ContactListActivity extends Activity {
	private Context context = this;
	private ContactsDataSource datasource;
	private ArrayList<Contact> contacts;
	private ArrayList<Contact> contactsSearchResult;
	private SearchView searchView;
	private ListView lv;
	private Button buttonAdd;
	private TextView textView;
	public static final String TAG = ContactListActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setTitle("Contacts");
		setContentView(R.layout.activity_contact_list);

		Log.i(TAG, "started ContactListActivity");
		datasource = new ContactsDataSource(this);
		textView = (TextView) findViewById(R.id.textView);
		textView.setText("Contacts");
		searchView = (SearchView) findViewById(R.id.searchView);
		lv = (ListView) findViewById(android.R.id.list);

		TextView empty = (TextView) findViewById(R.id.empty);

		lv.setEmptyView(empty);
		buttonAdd = (Button) findViewById(R.id.buttonAdd);

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

		lv.setAdapter(new ContactListAdapter(this,
				R.id.activityContactListTextView, contactsSearchResult));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(context, MyTabActivity.class);
				i.putExtra("contactId", contactsSearchResult.get(position)
						.getId());
				startActivity(i);
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

				lv.setAdapter(new ContactListAdapter(ContactListActivity.this,
						R.id.activityContactListTextView, contactsSearchResult));
			
				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				return true;
			}
		});

		buttonAdd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(context, AddNewContactActivity.class));
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
