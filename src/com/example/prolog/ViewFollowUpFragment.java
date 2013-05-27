package com.example.prolog;



import java.util.ArrayList;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.FollowUp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class ViewFollowUpFragment extends Fragment {

	private ContactsDataSource datasource;
	private Button buttonAdd;
 private ArrayList<FollowUp> followUps;
	private Fragment fragment=this;
	
 private ListView listView;
	private long contactId;
	private Context context;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		context=getActivity();
		buttonAdd = (Button) getView().findViewById(R.id.buttonAdd);
		buttonAdd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
			/*	Intent intent = new Intent(getActivity().getBaseContext(), NewFollowUpActivity.class);
				intent.putExtra("contactId", contactId);
				
				
				startActivity(intent);*/
				
				Bundle b = new Bundle();
				b.putLong(Commons.CONTACT_ID, contactId);
				NewFollowUpFragment newFollowUpFragment = new NewFollowUpFragment();
				newFollowUpFragment.setArguments(b);
				FragmentManager fmi = getFragmentManager();
				FragmentTransaction ftu = fmi.beginTransaction();
				ftu.replace(android.R.id.content, newFollowUpFragment).detach(fragment)
						.addToBackStack(null).commit();
				
							
			}
		});
		
		datasource=new ContactsDataSource(getActivity());
		datasource.open();
		listView = (ListView) getView().findViewById(android.R.id.list);

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

		return (RelativeLayout) inflater.inflate(R.layout.fragment_view_followups, container, false);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		datasource.open();
		
		contactId=getArguments().getLong("contactId");
		followUps=datasource.findFollowUpsbyContactId(contactId);
		
		listView.setAdapter(new FollowUpListAdapter(context,
				R.id.textViewTitle, followUps));
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				Bundle b = new Bundle();
				b.putLong(Commons.FOLLOWUP_ID, followUps.get(position).getId());
				ViewFollowUpDetailsFragment viewFollowUpDetailsFragment = new ViewFollowUpDetailsFragment();
				viewFollowUpDetailsFragment.setArguments(b);
				FragmentManager fmi = getFragmentManager();
				FragmentTransaction ftu = fmi.beginTransaction();
				ftu.replace(android.R.id.content, viewFollowUpDetailsFragment).detach(fragment)
						.addToBackStack(null).commit();
				/*Intent i = new Intent(context, MyTabActivity.class);
				i.putExtra("contactId", contactsSearchResult.get(position)
						.getId());
				startActivity(i);*/
			}

		});
		
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		datasource.close();
	}

}
