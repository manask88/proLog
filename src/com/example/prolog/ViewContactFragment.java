package com.example.prolog;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
		
		contactId= getArguments().getLong("contactId");

		datasource=new ContactsDataSource(getActivity());
		datasource.open();
		contact=datasource.findContactbyId(contactId);
		Toast.makeText(getActivity(), contact.getHome_phone(), Toast.LENGTH_LONG).show();
		TextView tvName= (TextView) getActivity().findViewById(R.id.textViewName);
		TextView tvTitle= (TextView) getActivity().findViewById(R.id.textViewTitle);
		TextView tvCompany= (TextView) getActivity().findViewById(R.id.textViewContactCompany);
		TextView tvPhone= (TextView) getActivity().findViewById(R.id.textViewPhone);
		TextView tvEmail= (TextView) getActivity().findViewById(R.id.textViewEmail);
		TextView tvLocation= (TextView) getActivity().findViewById(R.id.textViewLocation);

		tvName.setText(contact.getName());
		tvTitle.setText(contact.getTitle());
		tvCompany.setText(contact.getCompany());
		tvPhone.setText(contact.getHome_phone());
		tvEmail.setText(contact.getEmail());
		tvLocation.setText(contact.getLocation());


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

		return (LinearLayout) inflater.inflate(R.layout.fragment_view_contact, container, false);
	}

}

