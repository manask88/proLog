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
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditInteractionActivity extends Activity {

	private Button buttonSave;
	private ContactsDataSource datasource;
	private Context context = this;
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
	public final static String TAG = EditInteractionActivity.class
			.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("New Interaction");
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		setContentView(R.layout.activity_new_interacion);

		datasource = new ContactsDataSource(context);

		Bundle b = getIntent().getExtras();
		interactionId = b.getLong("interactionId");
		contactId = b.getLong("contactId");
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

		buttonSave = (Button) findViewById(R.id.buttonSave);
		buttonCancel = (Button) findViewById(R.id.buttonCancel);
		checkBoxFollowUp = (CheckBox) findViewById(R.id.checkBoxFollowUp);
		editTextNotes = (EditText) findViewById(R.id.editTextNotes);
		editTextEvent = (EditText) findViewById(R.id.editTextEvent);
		editTextLocation = (EditText) findViewById(R.id.editTextLocation);
		editTextType = (EditText) findViewById(R.id.editTextType);
		editTextDate = (EditText) findViewById(R.id.editTextDate);

		editTextDate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				editTextDate = (EditText) findViewById(R.id.editTextDate);
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
							if (contactIds[i] == contact.getId() || contact.getId()==contactId) {
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

				finish();

			}
		});

		buttonCancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				finish();

			}
		});

		otherParticipants = (TextView) findViewById(R.id.textViewOtherParticipants);

		buttonSelect = (Button) findViewById(R.id.buttonSelect);
		buttonSelect.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent i = new Intent(context,
						ContactListInteractionsAddContactActivity.class);
				i.putExtra("interactionId", interaction.getId());
				i.putExtra("contactId", contactId);
				if (contactIds != null)
					i.putExtra("contactIds", contactIds);
				i.putExtra("callingActivity", TAG);
				startActivity(i);

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
	protected void onResume() {
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

	@Override
	protected void onNewIntent(Intent intent) {

		datasource.open();

		Bundle b = intent.getExtras();

		contactIds = b.getLongArray("contactIds");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_interacion, menu);
		return true;
	}

	@Override
	protected void onPause() {
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
