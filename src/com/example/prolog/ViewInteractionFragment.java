package com.example.prolog;


import java.util.ArrayList;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Interaction;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewInteractionFragment extends Fragment {

	private ContactsDataSource datasource;
	private Button button1;
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		button1 = (Button) getView().findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getActivity().getBaseContext(),NewInteracionActivity.class));
			}
		});
		
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
