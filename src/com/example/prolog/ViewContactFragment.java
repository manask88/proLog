package com.example.prolog;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewContactFragment extends Fragment {

	ContactsDataSource datasource;
	Contact contact;
	long contactId;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		contactId = getArguments().getLong("contactId");

		datasource = new ContactsDataSource(getActivity());
		datasource.open();
		contact = datasource.findContactbyId(contactId);
		TextView tvName = (TextView) getActivity().findViewById(
				R.id.textViewName);
		TextView tvTitle = (TextView) getActivity().findViewById(
				R.id.textViewTitle);
		TextView tvCompany = (TextView) getActivity().findViewById(
				R.id.textViewContactCompany);
		TextView tvPhone = (TextView) getActivity().findViewById(
				R.id.textViewPhone);
		TextView tvEmail = (TextView) getActivity().findViewById(
				R.id.textViewEmail);
		TextView tvLocation = (TextView) getActivity().findViewById(
				R.id.textViewLocation);
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
				datasource.open();
				//TODO should also delete the groups in which the contact is in and anything else?
				datasource.deleteInteractionsByContactId(contactId);
				datasource.deleteContactById(contactId);
				datasource.close();
				startActivity(new Intent(getActivity(),ContactListActivity.class));
				
			}
		});

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		datasource.open();
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

		return (LinearLayout) inflater.inflate(R.layout.fragment_view_contact,
				container, false);
	}

}
