package com.example.prolog;

import java.util.HashMap;
import java.util.Set;

import com.example.prolog.ViewGroupActivity.MyAlertDialogFragment;
import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;
import com.example.prolog.model.FollowUp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ViewFollowUpDetailsFragment extends Fragment {

	ContactsDataSource datasource;
	Contact contact;
	private ImageButton imageButtonEdit, imageButtonDelete;

	public final static String TAG = ViewFollowUpDetailsFragment.class.getSimpleName();
	long followUpId;
	TextView textViewTitle, textViewNotes, textViewDate;
	FollowUp followUp;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		followUpId = getArguments().getLong(Commons.FOLLOWUP_ID);
		Log.i(TAG,"followUpId: "+ followUpId );

		datasource = new ContactsDataSource(getActivity());
		datasource.open();
		followUp=datasource.findFollowUpbyId(followUpId);
		
	
		
		textViewTitle= (TextView) getView().findViewById(
				R.id.textViewTitle);
		textViewNotes= (TextView) getView().findViewById(
				R.id.textViewNotes);
		textViewDate= (TextView) getView().findViewById(
				R.id.textViewDueDate);
		textViewTitle.setText(followUp.getTitle());
		textViewNotes.setText(followUp.getNotes());
		textViewDate.setText(followUp.getDate());

		 imageButtonEdit = (ImageButton) getActivity().findViewById(
				R.id.imageButtonEdit);
		 imageButtonDelete = (ImageButton) getActivity()
				.findViewById(R.id.imageButtonDelete);


		/*
		ImageButton imagebuttonEdit = (ImageButton) getActivity().findViewById(
				R.id.imageButtonEdit);
		ImageButton imagebuttonDelete = (ImageButton) getActivity()
				.findViewById(R.id.imageButtonDelete);

		tvName.setText(contact.getName());
		tvTitle.setText(contact.getTitle());
		tvCompany.setText(contact.getCompany());
		tvPhone.setText(contact.getHome_phone());
		tvEmail.setText(contact.getEmail());
		tvLocation.setText(contact.getLocation());
		
	
		

		imagebuttonEdit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putLong("contactId", contactId);
				EditContactFragment editContactFragment = new EditContactFragment();
				editContactFragment.setArguments(b);
				FragmentManager fmi = getFragmentManager();
				FragmentTransaction ftu = fmi.beginTransaction();
				ftu.replace(android.R.id.content, editContactFragment)
						.addToBackStack(null).commit();
			}
		});

		imagebuttonDelete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DialogFragment newFragment = MyAlertDialogFragment
						.newInstance(contactId);
				newFragment.show(getFragmentManager(), "dialog");

			}
		});
*/
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		datasource.open();
		
		
		imageButtonEdit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putLong(Commons.FOLLOWUP_ID, followUpId);
				EditFollowUpFragment editFollowUpFragment = new EditFollowUpFragment();
				editFollowUpFragment.setArguments(b);
				FragmentManager fmi = getFragmentManager();
				FragmentTransaction ftu = fmi.beginTransaction();
				ftu.replace(android.R.id.content, editFollowUpFragment)
						.addToBackStack(null).commit();
			}
		});

		imageButtonDelete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DialogFragment newFragment = MyAlertDialogFragment
						.newInstance(followUpId);
				newFragment.show(getFragmentManager(), "dialog");

			}
		});
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		datasource.close();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return (RelativeLayout) inflater.inflate(R.layout.fragment_view_followups_detail,
				container, false);
	}

	public static class MyAlertDialogFragment extends DialogFragment {

		public static MyAlertDialogFragment newInstance(long followUpId) {
			MyAlertDialogFragment frag = new MyAlertDialogFragment();
			Bundle args = new Bundle();
			args.putLong(Commons.FOLLOWUP_ID, followUpId);
			frag.setArguments(args);
			return frag;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final long followUpId = getArguments().getLong(Commons.FOLLOWUP_ID);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					getActivity()).setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Are you sure you want to delete this Follow Up?");
			alertDialogBuilder.setPositiveButton(R.string.OK,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							ContactsDataSource datasource = new ContactsDataSource(
									getActivity());
							
							datasource.open();
						
							datasource.deleteFollowUpById(followUpId);
							datasource.close();
							FragmentManager fmi = getFragmentManager();
							fmi.popBackStack();
							/*getActivity().finish();
							startActivity(new Intent(getActivity(),
									ContactListActivity.class));*/

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
