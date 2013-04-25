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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewFullInteractionFragment extends Fragment {

	private TextView textViewDate,textViewType,textViewLocation,textViewNotes,textViewEvent;
	private ImageButton imageButtonEdit, imagebuttonDelete;
	CheckBox checkBoxFollowUp;
	private ContactsDataSource datasource;
	private Context context;
	private ArrayList<Contact> interactionContacts;
	private TextView otherParticipants;
	private Interaction interaction;
	private long interactionId, contactId;
	public final static String TAG = ViewFullInteractionFragment.class
			.getSimpleName();

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return (RelativeLayout) inflater.inflate(R.layout.activity_view_full_interaction, container, false);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, "onCreate");
		context = getActivity();
		datasource = new ContactsDataSource(context);

		interactionId = getArguments().getLong("interactionId");
		contactId = getArguments().getLong("contactId");

		interactionContacts = new ArrayList<Contact>();


		otherParticipants = (TextView) getView().findViewById(R.id.textViewOtherParticipants);
		imageButtonEdit = (ImageButton) getView().findViewById(R.id.imageButtonEdit);
		imageButtonEdit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				/*Intent i = new Intent(context, EditInteractionActivity.class);
				i.putExtra("interactionId", interactionId);
				i.putExtra("contactId", contactId);
				startActivity(i);*/
				Bundle b = new Bundle();
				b.putLong(Commons.CONTACT_ID, contactId);
				b.putLong(Commons.INTERACTION_ID, interactionId);

				EditInteractionFragment editInteractionFragment = new EditInteractionFragment();
				editInteractionFragment.setArguments(b);
				FragmentManager fmi = getFragmentManager();
				FragmentTransaction ftu = fmi.beginTransaction();
				ftu.replace(android.R.id.content, editInteractionFragment)
						.addToBackStack(null).commit();
			}

		});

		imagebuttonDelete = (ImageButton) getView().findViewById(R.id.imageButtonDelete);
		imagebuttonDelete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DialogFragment newFragment = MyAlertDialogFragment
						.newInstance(interactionId,contactId);
				newFragment.show(getFragmentManager(), "dialog");

			}
		});

		textViewDate = (TextView) getView().findViewById(R.id.textViewDate);
		textViewType = (TextView) getView().findViewById(R.id.textViewType);
		textViewLocation = (TextView) getView().findViewById(R.id.textViewLocation);
		textViewEvent = (TextView) getView().findViewById(R.id.textViewEvent);
		textViewNotes = (TextView) getView().findViewById(R.id.textViewNotes);
		checkBoxFollowUp = (CheckBox) getView().findViewById(R.id.checkBoxFollowUp);

	}

	@Override
	public void onResume() {
		super.onResume();

		datasource.open();
		Log.i(TAG, "onResume");

		interaction = datasource.findInteractionbyId(interactionId);
		textViewDate.setText(interaction.getDate());
		textViewType.setText(interaction.getType());
		textViewLocation.setText(interaction.getLocation());
		textViewEvent.setText(interaction.getEvent());
		textViewNotes.setText(interaction.getNotes());
		checkBoxFollowUp.setChecked(interaction.isFollowUp());
		interactionContacts = datasource
				.findContactsbyInteractionId(interaction.getId());

		if (interactionContacts.size() > 1)
			otherParticipants.setText("");
		else
			otherParticipants.setText("Empty");

		for (Contact contact : interactionContacts)

		{

			Contact contactData = datasource.findContactbyId(contact.getId());
			if (contactData != null && contactData.getId() != contactId) {
				otherParticipants.setText(otherParticipants.getText()
						.toString() + " " + contact.getName());
				Log.i(TAG, "other contacts" + contact.getName());
			}

		}

	}

	

	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
		datasource.close();
	}

	public static class MyAlertDialogFragment extends DialogFragment {

		public static MyAlertDialogFragment newInstance(long interactionId,long contactId) {
			MyAlertDialogFragment frag = new MyAlertDialogFragment();
			Bundle args = new Bundle();
			args.putLong("interactionId", interactionId);
			args.putLong("contactId", contactId);
			frag.setArguments(args);
			return frag;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final long interactionId = getArguments().getLong("interactionId");
			final long contactId = getArguments().getLong("contactId");
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					getActivity())
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(
							"Are you sure you want to delete this interaction?");
			alertDialogBuilder.setPositiveButton(R.string.OK,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							ContactsDataSource datasource = new ContactsDataSource(
									getActivity());
							;
							 datasource.open();
							

							datasource.deleteInteractionContactsByInteractionIdAndContactId(interactionId, contactId);
							
							getActivity().finish();
							/*only deleting relationship interaction contact, not the interaction itself*/

						}
					});
			alertDialogBuilder.setNegativeButton(R.string.Cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

						}
					});

			return alertDialogBuilder.create();
		}
	}

	protected void deleteContact(long contactId2) {

	}

}
