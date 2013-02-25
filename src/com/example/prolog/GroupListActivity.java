package com.example.prolog;

import java.util.ArrayList;

import com.example.prolog.db.ContactsDataSource;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ExpandableListView;

public class GroupListActivity extends Activity {
	
	ContactsDataSource contactsDataSource;
	ArrayList<ExpandListChildGroupListActivity> contacts = new ArrayList<ExpandListChildGroupListActivity>();
	private ExpandListAdapterGroupListActivity ExpAdapter;
	private ArrayList<ExpandListGroupGroupListActivity> ExpListItems;
	private ExpandableListView ExpandList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		contactsDataSource = new ContactsDataSource(this);
		setTitle("Groups");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_list);
		ExpandList = (ExpandableListView) findViewById(R.id.activityGroupListExpList);
	    ExpListItems = SetStandardGroups();
	    ExpAdapter = new ExpandListAdapterGroupListActivity(GroupListActivity.this, ExpListItems);
	    ExpandList.setAdapter(ExpAdapter);
	    
	}
		
	
	@Override
	protected void onResume() {
		super.onResume();
		contactsDataSource.open();
	}
	@Override
	protected void onPause() {
		super.onPause();
		contactsDataSource.close();

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_list, menu);
		return true;
	}
	
	   public ArrayList<ExpandListGroupGroupListActivity> SetStandardGroups() {
	    	ArrayList<ExpandListGroupGroupListActivity> list = new ArrayList<ExpandListGroupGroupListActivity>();
	    	ArrayList<ExpandListChildGroupListActivity> list1 = new ArrayList<ExpandListChildGroupListActivity>();
	    	ArrayList<ExpandListChildGroupListActivity> list2 = new ArrayList<ExpandListChildGroupListActivity>();
	        
	    	
	    	ExpandListGroupGroupListActivity gru1 = new ExpandListGroupGroupListActivity();
	        gru1.setName("Family");
	       
	        ExpandListChildGroupListActivity ch1_1 = new ExpandListChildGroupListActivity();
	        ch1_1.setName("Devika Nair");
	        ch1_1.setTag(null);
	        list1.add(ch1_1);  
	        
	        ExpandListChildGroupListActivity ch1_2 = new ExpandListChildGroupListActivity();
	        ch1_2.setName("Michael Curd");
	        ch1_2.setTag(null);
	        list1.add(ch1_2);    
	        
	        ExpandListChildGroupListActivity ch1_3 = new ExpandListChildGroupListActivity();

	        ch1_3.setName("Dooyum Malu");
	        ch1_3.setTag(null);
	        list1.add(ch1_3);
	        
	        gru1.setItems(list1);
	        list.add(gru1);
	        
	        
	        
	        ExpandListGroupGroupListActivity gru2 = new ExpandListGroupGroupListActivity();
	        gru2.setName("Friends");
	        
	        ExpandListChildGroupListActivity ch2_1 = new ExpandListChildGroupListActivity();
	        ch2_1.setName("Ruchir Patwa");
	        ch2_1.setTag(null);
	        list2.add(ch2_1);
	        
	        ExpandListChildGroupListActivity ch2_2 = new ExpandListChildGroupListActivity();
	        ch2_2.setName("Malav Bhavsar");
	        ch2_2.setTag(null);
	        list2.add(ch2_2);
	        
	        ExpandListChildGroupListActivity ch2_3 = new ExpandListChildGroupListActivity();
	        ch2_3.setName("Jack Lam");
	        ch2_3.setTag(null);
	        list2.add(ch2_3);
	        
	        ExpandListChildGroupListActivity ch2_4 = new ExpandListChildGroupListActivity();
	        ch2_4.setName("Julianne Harty");
	        ch2_4.setTag(null);
	        list2.add(ch2_4);
	        
	        gru2.setItems(list2);
	        list.add(gru2);
	        
	        return list;
	    }

	   
	    
	
	
}
