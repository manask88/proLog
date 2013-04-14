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

		interaction = new Interaction();
		interactionContacts = new ArrayList<Contact>();
		Bundle b = getIntent().getExtras();
		contactId = b.getLong("contactId");

		setContentView(R.layout.activity_new_interacion);
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		otherParticipants = (TextView) findViewById(R.id.textViewOtherParticipants);

		buttonSelect = (Button) findViewById(R.id.buttonSelect);
		buttonSelect.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent i = new Intent(context,
						ContactListInteractionsAddContactActivity.class);
				i.putExtra(Commons.callingActivity, TAG);
				i.putExtra("interactionId", (long) -1);
				startActivity(i);

			}
		});

		etDate = (EditText) findViewById(R.id.editTextDate);
		etDate.setText((month + 1) + "/" + day + "/" + year);
		etDate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				etDate = (EditText) findViewById(R.id.editTextDate);
				DatePickerFragment newFragment = new DatePickerFragment()
						.setEditText(etDate);

				newFragment.show(getFragmentManager(), "datePicker");
			}
		});

		buttonSave = (Button) findViewById(R.id.buttonSave);
		buttonSave.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				CheckBox cbFollowUp = (CheckBox) findViewById(R.id.checkBoxFollowUp);
				EditText editTextNotes = (EditText) findViewById(R.id.editTextNotes);
				EditText editTextEvent = (EditText) findViewById(R.id.editTextEvent);
				EditText editTextLocation = (EditText) findViewById(R.id.editTextLocation);
				EditText editTextType = (EditText) findViewById(R.id.editTextType);
				interaction.setDate(etDate.getText().toString());
				interaction.setNotes(editTextNotes.getText().toString());
				interaction.setEvent(editTextEvent.getText().toString());
				interaction.setLocation(editTextLocation.getText().toString());
				interaction.setType(editTextType.getText().toString());
				interaction.setFollowUp(cbFollowUp.isChecked());
				
				
				interaction = datasource.createInteraction(interaction);
				datasource.createInteractionContacts(interaction.getId(),
						contactId);

				for (Contact contact : interactionContacts)

				{
					datasource.createInteractionContacts(interaction.getId(),
							contact.getId());
				}

				Intent i = new Intent(context, MyTabActivity.class);
				i.putExtra("contactId", contactId);
				startActivity(i);
				finish();
			}
		});

		buttonCancel = (Button) findViewById(R.id.buttonCancel);
		buttonCancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		datasource.open();
		Log.i(TAG, "onResume");

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

	@Override
	protected void onNewIntent(Intent intent) {
		Log.i(TAG, "onNewIntent");
		datasource.open();
		Bundle b = intent.getExtras();
		String calledActivity = b.getString("calledActivity");

		if (calledActivity != null
				&& calledActivity
						.equals(ContactListInteractionsAddContactActivity.TAG)) {
			long[] contactIds = b.getLongArray("contactIds");
			interactionContacts = new ArrayList<Contact>();

			for (int i = 0; i < contactIds.length; i++)
				interactionContacts.add(datasource
						.findContactbyId(contactIds[i]));

			if (interactionContacts.size() > 0)
				otherParticipants.setText("");
			else
				otherParticipants.setText("Empty");

			for (Contact contact : interactionContacts)

			{

				otherParticipants.setText(otherParticipants.getText()
						.toString() + " " + contact.getName());
				Log.i(TAG, "other contacts" + contact.getName());

			}
		}

	}

}
