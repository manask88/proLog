package com.example.prolog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import java.util.Date;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;

import android.app.Dialog;
import android.os.Bundle;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ContactListPickerFragment extends DialogFragment {

	private EditText editText1;
	private ArrayList<Contact> contacts, currentContacts;
	private ArrayList<Contact> contactsSearchResult;
	private long[] contactIdsArray;
	private ContactsDataSource datasource;
	private Button buttonAdd, buttonCancel, buttonSave;
	private TextView textView, otherParticipantsTextView;
	private ListView lv;
	private SearchView searchView;
	private long interactionId, contactId;
	private Context context;
	public final static String TAG = ContactListPickerFragment.class
			.getSimpleName();

	public ContactListPickerFragment() {
		interactionId = -1;
		contactId = -1;
	}

	public void setOtherContacts(long[] contactIds) {
		// TODO Auto-generated constructor stub
		contactIdsArray = contactIds;
	}

	void setContext(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	void setOtherParticipantsTextView(TextView otherParticipantsTextView) {
		this.otherParticipantsTextView = otherParticipantsTextView;

	}

	void setInteractionId(long interactionId) {
		// TODO Auto-generated constructor stub
		this.interactionId = interactionId;
	}

	void setContactId(long contactId) {
		// TODO Auto-generated constructor stub
		this.contactId = contactId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.activity_contact_list_with_checkbox, container, false);

		datasource = new ContactsDataSource(context);
		datasource.open();
		textView = (TextView) view.findViewById(R.id.textView);
		textView.setText("Contacts");
		searchView = (SearchView) view.findViewById(R.id.searchView);
		lv = (ListView) view.findViewById(android.R.id.list);
		buttonAdd = (Button) view.findViewById(R.id.buttonAdd);

		// Log.i(TAG, "interactionId: " + interactionId);

		buttonSave = (Button) view.findViewById(R.id.buttonSave);

		buttonCancel = (Button) view.findViewById(R.id.buttonCancel);

		buttonCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		contacts = datasource.findAllContacts();

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
			if (contact.getId() != contactId)
				contactsSearchResult.add(contact);
		}
		Collections.sort(contactsSearchResult, new ContactsCompareByName());

		lv.setAdapter(new ContactListAdapterWithCheckBox(context,
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

				lv.setAdapter(new ContactListAdapter(context,
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
						contactIdsArray[i] = contact.getId();
						i++;
					}
				}

				/*
				 * intent = new Intent(context, NewInteractionActivity.class);
				 * intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				 * intent.putExtra("contactIds", contactIdsArray);
				 */

				if (contactIdsArray == null || contactIdsArray.length <= 0) {
					otherParticipantsTextView.setText("Empty");

				} else {
					otherParticipantsTextView.setText("");
					for (int j = 0; j < contactIdsArray.length; j++) {
						Contact contact = datasource
								.findContactbyId(contactIdsArray[j]);

						if (contact != null) {
							otherParticipantsTextView
									.setText(otherParticipantsTextView
											.getText().toString()
											+ " "
											+ contact.getName());
						}
					}

				}
				if (interactionId < 0)
					((NewInteractionFragment) getTargetFragment())
							.setContactIds(contactIdsArray);
				if (interactionId >= 0)
					((EditInteractionFragment) getTargetFragment())
							.setContactIds(contactIdsArray);

				dismiss();

				/*
				 * intent.putExtra("interactionId", interactionId);
				 * intent.putExtra("calledActivity", TAG);
				 * startActivity(intent);
				 */
				// finish();

			}
		});

		// View tv = v.findViewById(R.id.text);
		// ((TextView)tv).setText("This is an instance of MyDialogFragment");
		return view;
	}
}