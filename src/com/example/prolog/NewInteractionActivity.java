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

public class NewInteractionActivity extends Activity {

	private EditText etDate;
	private Button buttonSave;
	private ContactsDataSource datasource;
	private Context context = this;
	private Button buttonCancel, buttonSelect;
	private ArrayList<Contact> interactionContacts;
	private long contactId;
	private TextView otherParticipants;
	private Interaction interaction;
	public final static String TAG = NewInteractionActivity.class
			.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("New Interaction");
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		datasource = new ContactsDataSource(context);
		datasource.open();

		interaction = datasource.createInteraction(new Interaction());

		Bundle b = getIntent().getExtras();
		contactId = b.getLong("contactId");

		interactionContacts = new ArrayList<Contact>();
		

		setContentView(R.layout.activity_new_interacion);
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		otherParticipants = (TextView) findViewById(R.id.newInteractionActivityOtherParticipants);

		buttonSelect = (Button) findViewById(R.id.newInteractionActivitybuttonSelect);
		buttonSelect.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent i = new Intent(context,
						ContactListInteractionsAddContactActivity.class);
				i.putExtra("interactionId", interaction.getId());
				startActivity(i);

			}
		});

		etDate = (EditText) findViewById(R.id.newInteractionActivityEditTextDate);
		etDate.setText((month + 1) + "/" + day + "/" + year);
		etDate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				etDate = (EditText) findViewById(R.id.newInteractionActivityEditTextDate);
				DatePickerFragment newFragment = new DatePickerFragment()
						.setEditText(etDate);

				newFragment.show(getFragmentManager(), "datePicker");
			}
		});

		buttonSave = (Button) findViewById(R.id.newInteractionActivityButtonSave);
		buttonSave.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				CheckBox cbFollowUp = (CheckBox) findViewById(R.id.newInteractionActivityCheckBoxFollowUp);
				EditText etText = (EditText) findViewById(R.id.newInteractionActivityEditTextText);

				interaction.setDate(etDate.getText().toString());
				interaction.setText(etText.getText().toString());
				interaction = datasource
						.updateInteractionByInteractionId(interaction);
				datasource.createInteractionContacts(interaction.getId(),
						contactId);
				/*for (Contact contact : interactionContacts)

				{
					datasource.createInteractionContacts(interaction.getId(),
							contact.getId());
				}*/

				Intent i = new Intent(context, MyTabActivity.class);
				i.putExtra("contactId", contactId);
				startActivity(i);
				finish();
			}
		});

		buttonCancel = (Button) findViewById(R.id.newInteractionActivityButtonCancel);
		buttonCancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				datasource.deleteInteractionByInteractionId(interaction.getId());
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		datasource.open();
		Log.i(TAG, "onResume");
		otherParticipants.setText("Empty");
		
		interactionContacts=datasource.findContactsbyInteractionId(interaction.getId());

		if (interactionContacts.size()>0)
			otherParticipants.setText("");
		else
			otherParticipants.setText("Empty");
		
		
		for (Contact contact : interactionContacts)

		{
		
			
			Contact contactData = datasource.findContactbyId(contact.getId());
			if (contactData != null) {
				otherParticipants.setText(otherParticipants.getText()
						.toString() + " " + contact.getName());
				Log.i(TAG, "other contacts" + contact.getName());
			}

		}

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
