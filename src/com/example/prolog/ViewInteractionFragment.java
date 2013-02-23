package com.example.prolog;


import java.util.ArrayList;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Interaction;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ViewInteractionFragment extends Fragment {

	ContactsDataSource datasource;
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		datasource=new ContactsDataSource(getActivity());
		datasource.open();
		/*Interaction interaction=new Interaction();
		interaction.setText("some stuff");  
		datasource.createInteraction(interaction);
		ArrayList<Interaction> list=new ArrayList<Interaction>();
		list=datasource.findInteractionsbyContactId(0);
		 for (Interaction inter : list)
		      Toast.makeText(getActivity(), inter.getText(), Toast.LENGTH_LONG).show();*/ //created for test purposes
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return (RelativeLayout) inflater.inflate(R.layout.fragment_view_interactions, container, false);
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

}
