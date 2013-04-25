package com.example.prolog;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;
import com.example.prolog.model.Interaction;
import com.example.prolog.model.InteractionContact;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditInteractionFragment extends Fragment {

	private Button buttonSave;
	private ContactsDataSource datasource;
	private Context context;
	private Button buttonCancel, buttonSelect;
	private TextView otherParticipants;
	private Interaction interaction;
	private long interactionId, contactId;
	private long[] contactIds;

	CheckBox checkBoxFollowUp;
	EditText editTextNotes;
	EditText editTextEvent;
	EditText editTextLocation;
	EditText editTextType;
	EditText editTextDate;
	public final static String TAG = EditInteractionFragment.class
			.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return (RelativeLayout) inflater.inflate(
				R.layout.activity_new_interacion, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		context = getActivity();
		Log.i(TAG, "onCreate");

		datasource = new ContactsDataSource(context);

		interactionId = getArguments().getLong("interactionId");
		contactId = getArguments().getLong("contactId");
		datasource.open();

		ArrayList<Contact> contacts = datasource
				.findContactsbyInteractionId(interactionId);

		contactIds = new long[contacts.size()];
		int i = 0;
		for (Contact contact : contacts) {

			if (contact.getId() != contactId) {
				contactIds[i] = contact.getId();
				i++;
			}
		}

		buttonSave = (Button) getView().findViewById(R.id.buttonSave);
		buttonCancel = (Button) getView().findViewById(R.id.buttonCancel);
		checkBoxFollowUp = (CheckBox) getView().findViewById(R.id.checkBoxFollowUp);
		editTextNotes = (EditText) getView().findViewById(R.id.editTextNotes);
		editTextEvent = (EditText) getView().findViewById(R.id.editTextEvent);
		editTextLocation = (EditText) getView().findViewById(R.id.editTextLocation);
		editTextType = (EditText) getView().findViewById(R.id.editTextType);
		editTextDate = (EditText)getView(). findViewById(R.id.editTextDate);

		editTextDate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				editTextDate = (EditText) getView().findViewById(R.id.editTextDate);
				DatePickerFragment newFragment = new DatePickerFragment()
						.setEditText(editTextDate);

				newFragment.show(getFragmentManager(), "datePicker");
			}
		});

		buttonSave.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				interaction.setDate(editTextDate.getText().toString());
				interaction.setNotes(editTextNotes.getText().toString());
				interaction.setEvent(editTextEvent.getText().toString());
				interaction.setLocation(editTextLocation.getText().toString());
				interaction.setType(editTextType.getText().toString());
				interaction.setFollowUp(checkBoxFollowUp.isChecked());
				interaction.setId(interactionId);
				datasource.updateInteractionByInteractionId(interaction);

				if (contactIds != null)

				{
					for (Contact contact : datasource.findAllContacts()) {

						for (int i = 0; i < contactIds.length; i++) {
							if (contactIds[i] == contact.getId()
									|| contact.getId() == contactId) {
								datasource.createInteractionContacts_r(
										interactionId, contact.getId());
								break;
							} else {
								datasource.deleteInteractionContacts_r(
										interactionId, contact.getId());
							}
						}
					}
				}
				FragmentManager fmi = getFragmentManager();
				fmi.popBackStack();
				//finish();

			}
		});

		buttonCancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				//finish();
				FragmentManager fmi = getFragmentManager();
				fmi.popBackStack();
			}
		});

		otherParticipants = (TextView) getView().findViewById(R.id.textViewOtherParticipants);

		buttonSelect = (Button) getView().findViewById(R.id.buttonSelect);
		buttonSelect.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

			/*	Intent i = new Intent(context,
						ContactListInteractionsAddContactActivity.class);
				i.putExtra("interactionId", interaction.getId());
				i.putExtra("contactId", contactId);
				if (contactIds != null)
					i.putExtra("contactIds", contactIds);
				i.putExtra("callingActivity", TAG);
				startActivity(i);
*/
				ContactListPickerFragment newFragment = new ContactListPickerFragment();
				newFragment.setContext(context);
				newFragment.setContactId(contactId);
				newFragment.setInteractionId(interactionId);
				newFragment.setOtherContacts(contactIds);
				newFragment.setOtherParticipantsTextView(otherParticipants);
				newFragment.setTargetFragment(EditInteractionFragment.this,0);
				newFragment.show(getFragmentManager(), "datePicker");
				
				
				
				
				
			}
		});

		/*
		 * for (Contact contact : interactionContacts)
		 * 
		 * {
		 * 
		 * Contact contactData = datasource.findContactbyId(contact.getId()); if
		 * (contactData != null && contactData.getId() != contactId) {
		 * otherParticipants.setText(otherParticipants.getText() .toString() +
		 * " " + contact.getName()); Log.i(TAG, "other contacts" +
		 * contact.getName()); }
		 * 
		 * }
		 */

	}

	@Override
	public void onResume() {
		super.onResume();

		datasource.open();
		Log.i(TAG, "onResume");

		Log.i(TAG, "interactionId: " + interactionId);
		interaction = datasource.findInteractionbyId(interactionId);
		checkBoxFollowUp.setChecked(interaction.isFollowUp());
		editTextNotes.setText(interaction.getNotes());
		editTextEvent.setText(interaction.getEvent());
		editTextLocation.setText(interaction.getLocation());
		editTextType.setText(interaction.getType());
		editTextDate.setText(interaction.getDate());

		// otherParticipants.setText("Empty");

		/*
		 * if (interactionContacts.size() > 1) otherParticipants.setText("");
		 * else otherParticipants.setText("Empty");
		 */
		if (contactIds == null || contactIds.length <= 0)
			otherParticipants.setText("Empty");
		else {
			otherParticipants.setText("");
			for (int i = 0; i < contactIds.length; i++) {
				Log.i(TAG, "contact 1Id " + contactIds[i]);
				Contact contact = datasource.findContactbyId(contactIds[i]);

				if (contact != null) {
					Log.i(TAG, "contact 2Id " + contact.getId());
					String s1 = otherParticipants.getText().toString();
					String s2 = contact.getName();
					otherParticipants.setText(s1 + " " + s2);
				}

			}

		}

	}

	public void setContactIds(long[] contactIds) {
		this.contactIds = contactIds;
	}



	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
		datasource.close();
	}

	/*
	 * public void showDatePickerDialog(View v) { DialogFragment newFragment =
	 * new DatePickerFragment() .setEditText(editText1);
	 * newFragment.show(getFragmentManager(), "datePicker"); }
	 */

}
