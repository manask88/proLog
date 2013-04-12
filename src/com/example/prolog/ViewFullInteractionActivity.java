package com.example.prolog;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.prolog.ViewGroupActivity.MyAlertDialogFragment;
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
import android.widget.ImageButton;
import android.widget.TextView;

public class ViewFullInteractionActivity extends Activity {

	private EditText etDate;
	private ImageButton imageButtonEdit;
	private ContactsDataSource datasource;
	private Context context = this;
	private Button buttonCancel, buttonSelect;
	private ArrayList<Contact> interactionContacts;
	private TextView otherParticipants;
	private Interaction interaction;
	private long interactionId,contactId;
	public final static String TAG = ViewFullInteractionActivity.class
			.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("New Interaction");
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		datasource = new ContactsDataSource(context);
		

		Bundle b = getIntent().getExtras();
		interactionId = b.getLong("interactionId");
		contactId = b.getLong("contactId");

		
		interactionContacts = new ArrayList<Contact>();
		

		setContentView(R.layout.activity_view_full_interacion);
		
		otherParticipants = (TextView) findViewById(R.id.newInteractionActivityOtherParticipants);
		imageButtonEdit = (ImageButton) findViewById(R.id.imageButtonEdit);
		imageButtonEdit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				Intent i = new Intent(context, EditInteractionActivity.class);
				i.putExtra("interactionId", interactionId);
				i.putExtra("contactId", contactId);
				startActivity(i);
		
				
			}

		});

		etDate = (EditText) findViewById(R.id.newInteractionActivityEditTextDate);
		
		

		

		
	}

	@Override
	protected void onResume() {
		super.onResume();

		datasource.open();
		Log.i(TAG, "onResume");
		
		
		interaction=datasource.findInteractionbyId(interactionId);
		etDate.setText(interaction.getDate());
		
		
		otherParticipants.setText("Empty");
		
		interactionContacts=datasource.findContactsbyInteractionId(interaction.getId());

		if (interactionContacts.size()>1)
			otherParticipants.setText("");
		else
			otherParticipants.setText("Empty");
		
		
		for (Contact contact : interactionContacts)

		{
		
			
			Contact contactData = datasource.findContactbyId(contact.getId());
			if (contactData != null && contactData.getId()!=contactId) {
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
