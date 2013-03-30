package com.example.prolog;

import java.util.ArrayList;
import java.util.Collections;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;
import com.example.prolog.model.Group;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class GroupListActivity extends Activity {

	ContactsDataSource dataSource;
	ArrayList<ExpandListChildGroupListActivity> contacts = new ArrayList<ExpandListChildGroupListActivity>();

	private Button buttonAddGroup;
	private Context context;
	private SearchView searchView;
	private TextView textView;

	private ListView lv;
	private ArrayList<Group> groups, groupsSearchResult;
	private static final String TAG = GroupListActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//setTitle("Groups");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_contact_list);
		dataSource = new ContactsDataSource(this);

		context = this;

		dataSource.open();

		lv = (ListView) findViewById(android.R.id.list);
		textView = (TextView) findViewById(R.id.textView);
		textView.setText("Groups");
		searchView = (SearchView) findViewById(R.id.searchView);

		buttonAddGroup = (Button) findViewById(R.id.buttonAdd);

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
		groups = dataSource.findAllGroups();

		groupsSearchResult = new ArrayList<Group>();

		for (Group group : groups) {
			groupsSearchResult.add(group);
		}

		Collections.sort(groupsSearchResult, new GroupCompareByName());

		lv.setAdapter(new GroupListAdapter(this,
				R.id.activityGroupListTextView, groupsSearchResult));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(context, ViewGroupActivity.class);
				i.putExtra("groupId", groups.get(position).getId());
				startActivity(i);
			}

		});

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String newText) {

				groupsSearchResult.clear();
				for (int i = 0; i < groups.size(); i++) {
					if (groups.get(i).getName().toLowerCase()
							.contains(newText.toLowerCase())) {
						groupsSearchResult.add(groups.get(i));
					}
				}

				Collections.sort(groupsSearchResult, new GroupCompareByName());

				lv.setAdapter(new GroupListAdapter(GroupListActivity.this,
						R.id.activityContactListTextView, groupsSearchResult));
				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				// Do something
				return true;
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
