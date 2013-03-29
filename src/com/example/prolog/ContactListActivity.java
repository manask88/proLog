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
import android.widget.TextView;
import android.widget.Toast;

public class ContactListActivity extends Activity {
	private Context context = this;
	private ContactsDataSource datasource;
	private ArrayList<Contact> contacts = new ArrayList<Contact>();
	private ArrayList<Contact> contactsSearchResult = new ArrayList<Contact>();
	private Contact[] lv_arr;
	private EditText editTextSearch;
	private ListView lv;
	private Button button;
	public static final String TAG = ContactListActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Contacts");
		setContentView(R.layout.activity_contact_list);

		Log.i(TAG, "started ContactListActivity");
		datasource = new ContactsDataSource(this);

		editTextSearch = (EditText) findViewById(R.id.editTextNewFollowUpActivityDate);

		button = (Button) findViewById(R.id.save);

	}

	@Override
	protected void onResume() {
		super.onResume();
		datasource.open();
		contacts = (ArrayList<Contact>) datasource.findAllContacts();
		Collections.sort(contacts, new ContactsCompareByName());
		// TODO we shoyld be copyng this properly... this is nasty!!
		contactsSearchResult = (ArrayList<Contact>) datasource.findAllContacts();
		Collections.sort(contactsSearchResult, new ContactsCompareByName());

		lv_arr = new Contact[contacts.size()];
		Iterator i = contacts.iterator();
		int j = 0;
		while (i.hasNext()) {
			lv_arr[j] = ((Contact) i.next());
			j++;
		}
		lv = (ListView) findViewById(android.R.id.list);

		lv.setAdapter(new ContactListAdapter(this,
				R.id.activityContactListTextView, contactsSearchResult));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(context, MyTabActivity.class);
				i.putExtra("contactId", contactsSearchResult.get(position).getId());
				startActivity(i);
			}

		});
		// lv.setAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.countries)));

		editTextSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				contactsSearchResult.clear();
				for (int i = 0; i < lv_arr.length; i++) {
					if (lv_arr[i]
							.getName()
							.toLowerCase()
							.contains(
									editTextSearch.getText().toString()
											.toLowerCase())
							|| lv_arr[i]
									.getCompany()
									.toLowerCase()
									.contains(
											editTextSearch.getText().toString()
													.toLowerCase())
							|| lv_arr[i]
									.getTitle()
									.toLowerCase()
									.contains(
											editTextSearch.getText().toString()
													.toLowerCase())) {
						contactsSearchResult.add(contacts.get(i));
					}
				}
				lv.setAdapter(new ContactListAdapter(ContactListActivity.this,
						R.id.activityContactListTextView, contactsSearchResult));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		button.setOnClickListener(new View.OnClickListener() {
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

	private class ContactListAdapter extends ArrayAdapter<Contact> {

		private ArrayList<Contact> items;

		public ContactListAdapter(Context context, int textViewResourceId,
				ArrayList<Contact> contacts) {
			super(context, textViewResourceId, contacts);
			items = contacts;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View row = inflater.inflate(R.layout.activity_contact_list_item,
					parent, false);

			ImageView iv = (ImageView) row
					.findViewById(R.id.activityContactListImageView);
			TextView tv = (TextView) row
					.findViewById(R.id.activityContactListTextView);
			TextView tvContactListTextViewTitle = (TextView) row
					.findViewById(R.id.activityContactListTextViewTitle);
			TextView tvContactListTextViewCompany = (TextView) row
					.findViewById(R.id.activityContactListTextViewCompany);
			Log.i(TAG, "id :" + items.get(position).getId());
			tv.setText(items.get(position).getName());
			tvContactListTextViewTitle.setText(items.get(position).getTitle());
			tvContactListTextViewCompany.setText(items.get(position).getCompany());
			if (items.get(position).getPhoto() != null)
				iv.setImageBitmap(items.get(position).getPhoto());
			else
				iv.setImageResource(R.drawable.face);

			return row;
		}
	}
}
