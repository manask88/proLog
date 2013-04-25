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

public class NewInteractionFragment extends Fragment {

	private EditText etDate;
	private Button buttonSave;
	private ContactsDataSource datasource;
	private Context context = getActivity();
	private Button buttonCancel, buttonSelect;
	private long contactId;
	private long[] contactIds;
	private TextView otherParticipants;
	private Interaction interaction;
	public final static String TAG = NewInteractionFragment.class
			.getSimpleName();

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return (RelativeLayout) inflater.inflate(R.layout.activity_new_interacion, container, false);
	}
	
	
	
	
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		//setTitle("New Interaction");
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		datasource = new ContactsDataSource(context);
		datasource.open();

		interaction = new Interaction();
		
		contactId = getArguments().getLong("contactId");

		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		otherParticipants = (TextView) getView().findViewById(R.id.textViewOtherParticipants);

		buttonSelect = (Button) getView().findViewById(R.id.buttonSelect);
		buttonSelect.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent i = new Intent(context,
						ContactListInteractionsAddContactActivity.class);
				i.putExtra(Commons.callingActivity, TAG);
				i.putExtra("interactionId", (long) -1);
				i.putExtra("contactId", contactId);
				if (contactIds != null)
					i.putExtra("contactIds", contactIds);
				startActivity(i);

			}
		});

		etDate = (EditText) getView().findViewById(R.id.editTextDate);
		etDate.setText((month + 1) + "/" + day + "/" + year);
		etDate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				etDate = (EditText) getView().findViewById(R.id.editTextDate);
				DatePickerFragment newFragment = new DatePickerFragment()
						.setEditText(etDate);

				newFragment.show(getFragmentManager(), "datePicker");
			}
		});

		buttonSave = (Button) getView().findViewById(R.id.buttonSave);
		buttonSave.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				CheckBox cbFollowUp = (CheckBox) getView().findViewById(R.id.checkBoxFollowUp);
				EditText editTextNotes = (EditText) getView().findViewById(R.id.editTextNotes);
				EditText editTextEvent = (EditText) getView().findViewById(R.id.editTextEvent);
				EditText editTextLocation = (EditText) getView().findViewById(R.id.editTextLocation);
				EditText editTextType = (EditText) getView().findViewById(R.id.editTextType);
				interaction.setDate(etDate.getText().toString());
				interaction.setNotes(editTextNotes.getText().toString());
				interaction.setEvent(editTextEvent.getText().toString());
				interaction.setLocation(editTextLocation.getText().toString());
				interaction.setType(editTextType.getText().toString());
				interaction.setFollowUp(cbFollowUp.isChecked());

				interaction = datasource.createInteraction(interaction);
				datasource.createInteractionContacts(interaction.getId(),
						contactId);

				if (contactIds != null)

				{
					for (int i = 0; i < contactIds.length; i++) {
						datasource.createInteractionContacts(
								interaction.getId(), contactIds[i]);
					}
				}

				Intent i = new Intent(context, MyTabActivity.class);
				i.putExtra("contactId", contactId);
				startActivity(i);
				FragmentManager fmi = getFragmentManager();
				fmi.popBackStack();
			}
		});

		buttonCancel = (Button) getView().findViewById(R.id.buttonCancel);
		buttonCancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				FragmentManager fmi = getFragmentManager();
				fmi.popBackStack();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();

		datasource.open();
		Log.i(TAG, "onResume");

	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
		datasource.close();
	}

	//@Override
	public void onNewIntent(Intent intent) {
		Log.i(TAG, "onNewIntent");
		datasource.open();
		Bundle b = intent.getExtras();
		String calledActivity = b.getString("calledActivity");

		if (calledActivity != null
				&& calledActivity
						.equals(ContactListInteractionsAddContactActivity.TAG)) {
			contactIds = b.getLongArray("contactIds");

			if (contactIds == null || contactIds.length <= 0)
				otherParticipants.setText("Empty");
			else {
				otherParticipants.setText("");
				for (int i = 0; i < contactIds.length; i++) {
					Contact contact = datasource.findContactbyId(contactIds[i]);

					if (contact != null) {
						otherParticipants.setText(otherParticipants.getText()
								.toString() + " " + contact.getName());
					}
				}

			}

			/*
			 * if (interactionContacts.size() > 0)
			 * otherParticipants.setText(""); else
			 * otherParticipants.setText("Empty");
			 * 
			 * for (Contact contact : interactionContacts)
			 * 
			 * {
			 * 
			 * otherParticipants.setText(otherParticipants.getText() .toString()
			 * + " " + contact.getName()); Log.i(TAG, "other contacts" +
			 * contact.getName());
			 * 
			 * }
			 */
		}

	}

}
