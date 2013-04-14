package com.example.prolog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;
import com.example.prolog.model.TypeValue;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactListGroupAddContactActivity extends Activity {
	private Context context = this;
	private ContactsDataSource datasource;
	private ArrayList<Contact> contacts, contactsCurrentGroup;
	private ArrayList<Contact> contactsSearchResult;
	private SearchView searchView;
	private ListView lv;
	private Button buttonAdd, buttonCancel, buttonSave;
	private TextView textView;
	public static final String TAG = ContactListGroupAddContactActivity.class
			.getSimpleName();
	private long groupId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setTitle("Contacts");
		setContentView(R.layout.activity_contact_list_with_checkbox);

		Log.i(TAG, "started ContactListGroupAddContactActivity");
		datasource = new ContactsDataSource(this);
		textView = (TextView) findViewById(R.id.textView);
		textView.setText("Contacts");
		searchView = (SearchView) findViewById(R.id.searchView);
		lv = (ListView) findViewById(android.R.id.list);
		buttonAdd = (Button) findViewById(R.id.buttonAdd);

		Bundle b = getIntent().getExtras();
		groupId = b.getLong("groupId");

		buttonSave = (Button) findViewById(R.id.buttonSave);

		buttonCancel = (Button) findViewById(R.id.buttonCancel);

		buttonCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
		datasource.open();

		contacts = datasource.findAllContacts();
		contactsCurrentGroup = datasource.findContactsbyGroupId(groupId);

		// TODO this can make it slow as this is very ineficient
		for (int i = 0; i < contacts.size(); i++) {
			for (int j = 0; j < contactsCurrentGroup.size(); j++) {

				Contact contact = contacts.get(i);
				if (contact.getId() == contactsCurrentGroup.get(j).getId()) {
					contact.setSelected(true);
					contacts.set(i, contact);
					break;

				}

			}

		}

		contactsSearchResult = new ArrayList<Contact>();

		for (Contact contact : contacts) {
			contactsSearchResult.add(contact);
		}
		Collections.sort(contactsSearchResult, new ContactsCompareByName());

		lv.setAdapter(new ContactListAdapterWithCheckBox(this,
				R.id.activityContactListTextView, contactsSearchResult));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);

				if (checkBox.isChecked()) {
					contactsSearchResult.get(position).setSelected(false);
					checkBox.setChecked(false);
				} else {
					contactsSearchResult.get(position).setSelected(true);
					checkBox.setChecked(true);

				}
				/*
				 * datasource.createGroupContacts(groupId, contactsSearchResult
				 * .get(position).getId());
				 */

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
						ContactListGroupAddContactActivity.this,
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

		buttonSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				for (Contact contact : contactsSearchResult) {
					if (contact.isSelected()) {
						datasource.createGroupContacts_r(groupId,
								contact.getId());
					} else
						datasource.deleteGroupContacts_r(groupId,
								contact.getId());
				}

				finish();

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
