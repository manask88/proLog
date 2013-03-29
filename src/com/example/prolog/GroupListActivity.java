package com.example.prolog;

import java.util.ArrayList;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;
import com.example.prolog.model.Group;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class GroupListActivity extends Activity {
	
	ContactsDataSource dataSource;
	ArrayList<ExpandListChildGroupListActivity> contacts = new ArrayList<ExpandListChildGroupListActivity>();

	private Button buttonAddGroup;
	private Context context;
	private ListView lv;
	private ArrayList<Group> groups;
	private static final String TAG = GroupListActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("Groups");
		super.onCreate(savedInstanceState);
	    
		
		setContentView(R.layout.activity_group_list);
		dataSource = new ContactsDataSource(this);

		context=this;
		
		dataSource.open();
		
		
		lv = (ListView) findViewById(android.R.id.list);
		

	    buttonAddGroup= (Button) findViewById(R.id.buttonAdd);
		 
	    buttonAddGroup.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(context, AddNewGroupActivity.class));
			}
		});
	    
	}
		
	
	@Override
	protected void onResume() {
		super.onResume();
		dataSource.open();
		groups =dataSource.findAllGroups();
		lv.setAdapter(new GroupListAdapter(this,
				R.id.activityGroupListTextView, groups));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(context, ViewGroupActivity.class);
				i.putExtra("groupId", groups.get(position).getId());
				startActivity(i);
			}

		});
	}
	@Override
	protected void onPause() {
		super.onPause();
		dataSource.close();

	}
	
	
	 
	private class GroupListAdapter extends ArrayAdapter<Group> {

		private ArrayList<Group> items;

		public GroupListAdapter(Context context, int textViewResourceId,
				ArrayList<Group> groups) {
			super(context, textViewResourceId, groups);
			items = groups;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View row = inflater.inflate(R.layout.activity_group_list_item,
					parent, false);

	
			TextView tv = (TextView) row
					.findViewById(R.id.activityGroupListTextView);
			Log.i(TAG, "id :" + items.get(position).getId());
			tv.setText(items.get(position).getName());
			

			return row;
		}
	}
	   
	    
	
	
}
