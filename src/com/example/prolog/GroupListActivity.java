package com.example.prolog;

import java.util.ArrayList;
import java.util.Collections;

import com.example.prolog.ViewGroupActivity.MyAlertDialogFragment;
import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;
import com.example.prolog.model.Group;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

public class GroupListActivity extends Activity {

	ContactsDataSource dataSource;
	ArrayList<ExpandListChildGroupListActivity> contacts = new ArrayList<ExpandListChildGroupListActivity>();

	private Button buttonAddGroup;
	private Context context;
	private SearchView searchView;
	private TextView textView;
	GroupListAdapter groupListAdapter;
	private ListView lv;
	private ArrayList<Group> groups, groupsSearchResult;
	private static final String TAG = GroupListActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setTitle("Groups");
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

	}

	private void refreshListView()
	{
		groups = dataSource.findAllGroups();

		groupsSearchResult = new ArrayList<Group>();

		for (Group group : groups) {
			groupsSearchResult.add(group);
		}

		Collections.sort(groupsSearchResult, new GroupCompareByName());

		TextView empty = (TextView) findViewById(R.id.empty);
		// lv.setAnimationCacheEnabled(false);
		// lv.setScrollingCacheEnabled(false);
		lv.setEmptyView(empty);
		groupListAdapter = new GroupListAdapter(this,
				R.id.activityGroupListTextView, groupsSearchResult);
		lv.setAdapter(groupListAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(context, ViewGroupActivity.class);
				i.putExtra("groupId", groupsSearchResult.get(position).getId());
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
		
		//groupListAdapter.notifyDataSetChanged();

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		dataSource.open();
		refreshListView();
		buttonAddGroup.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				CreateGroupDialog editNameDialog = new CreateGroupDialog();
				editNameDialog.show(fm, "fragment_edit_name");
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
		public View getView(int position, View row, ViewGroup parent) {
			if (row == null)

			{
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				row = inflater.inflate(R.layout.activity_group_list_item,
						parent, false);

				TextView tv = (TextView) row
						.findViewById(R.id.activityGroupListTextView);
				Log.i(TAG, "id :" + items.get(position).getId());
				tv.setText(items.get(position).getName());
			}
			return row;
		}
	}

	public static class CreateGroupDialog extends DialogFragment {

		private Button buttonSave, buttonCancel;
		private EditText editTextGroupName;
		private GroupListActivity activity;
		public CreateGroupDialog() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_add_new_group,
					container);
			buttonSave = (Button) view
					.findViewById(R.id.activityAddNewGroupSaveButton);
			buttonCancel = (Button) view
					.findViewById(R.id.activityAddNewGroupCancelButton);
			editTextGroupName = (EditText) view
					.findViewById(R.id.editTextGroupName);

			buttonSave.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					String groupName = editTextGroupName.getText().toString();
					if (!groupName.equals(""))

					{
						activity=(GroupListActivity) getActivity();
						Group group = new Group();
						group.setName(groupName);
						group = activity.dataSource.createGroup(group);

					
						activity.refreshListView();
						dismiss();
					}

					else {

						Toast.makeText(((GroupListActivity) getActivity()),
								Commons.pleaseEnterNameGroup,
								Toast.LENGTH_SHORT).show();
					}
				}
			});

			buttonCancel.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					dismiss();

				}
			});

			getDialog().setTitle("Hello");

			return view;
		}
	}

}
