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

public class ContactListInteractionsAddContactActivity extends Activity {
	private Context context = this;
	private ContactsDataSource datasource;
	private ArrayList<Long> contactIds;
	private long[] contactIdsArray;

	private ArrayList<Contact> contacts, currentContacts;
	private ArrayList<Contact> contactsSearchResult;
	private SearchView searchView;
	private ListView lv;
	private Button  buttonCancel, buttonSave;
	private TextView textView;
	public static final String TAG = ContactListInteractionsAddContactActivity.class
			.getSimpleName();
	private long interactionId,contactId;
	public String callingActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setTitle("Contacts");
		setContentView(R.layout.activity_contact_list_with_checkbox);

		Log.i(TAG, "started ContactListInteractionsAddContactActivity");
		datasource = new ContactsDataSource(this);
		textView = (TextView) findViewById(R.id.textView);
		textView.setText("Contacts");
		searchView = (SearchView) findViewById(R.id.searchView);
		
		
		
		
		lv = (ListView) findViewById(android.R.id.list);

		Log.i(TAG, "interactionId: " + interactionId);

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
		Bundle b = getIntent().getExtras();
		callingActivity = b.getString(Commons.callingActivity);

		interactionId = b.getLong("interactionId");
		contactId= b.getLong("contactId");
		contactIdsArray = b.getLongArray("contactIds");

		if (interactionId >= 0)

		{

			currentContacts = datasource
					.findContactsbyInteractionId(interactionId);
			// TODO this can make it slow as this is very ineficient
			for (int i = 0; i < contacts.size(); i++) {
				for (int j = 0; j < currentContacts.size(); j++) {

					Contact contact = contacts.get(i);
					if (contact.getId() == currentContacts.get(j).getId()) {
						contact.setSelected(true);
						contacts.set(i, contact);
						break;

					}

				}

			}
		}
		if (contactIdsArray != null) {

			// TODO this can make it slow as this is very ineficient

			for (int i = 0; i < contacts.size(); i++) {
				for (int j = 0; j < contactIdsArray.length; j++) {

					Contact contact = contacts.get(i);
					
				
					if (contact.getId() == contactIdsArray[j]) {
						contact.setSelected(true);
						contacts.set(i, contact);
						break;

					}

				}

			}

		}
		contactsSearchResult = new ArrayList<Contact>();

		for (Contact contact : contacts) {
			if (contact.getId()!=contactId)
			contactsSearchResult.add(contact);
		}
		Collections.sort(contactsSearchResult, new ContactsCompareByName());
		TextView empty = (TextView) findViewById(R.id.empty);
		
		lv.setEmptyView(empty);
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

			}

		});

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
						ContactListInteractionsAddContactActivity.this,
						R.id.activityContactListTextView, contactsSearchResult));
				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				return true;
			}
		});

		

		buttonSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent;

				{

					int i = 0;
					for (Contact contact : contactsSearchResult) {
						if (contact.isSelected()) {
							i++;
						}
					}
					
					contactIdsArray = new long[i];
					i = 0;
					for (Contact contact : contactsSearchResult) {
						if (contact.isSelected()) {
							contactIdsArray[i]=contact.getId();
							i++;
						}
					}

				

					intent = new Intent(context, NewInteractionActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent.putExtra("contactIds", contactIdsArray);

				}

				

				intent.putExtra("interactionId", interactionId);
				intent.putExtra("calledActivity", TAG);
				startActivity(intent);
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
