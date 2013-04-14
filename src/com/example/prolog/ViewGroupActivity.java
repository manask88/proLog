package com.example.prolog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;

import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListActivity;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewGroupActivity extends Activity {
	private Context context = this;
	private ContactsDataSource datasource;
	private ArrayList<Contact> contacts;
	private ArrayList<Contact> contactsSearchResult;
	private SearchView searchView;
	private ListView lv;
	private Button buttonAdd;
	private ImageButton imageButtonDelete, imageButtonEdit;
	private TextView textView;
	public static final String TAG = ViewGroupActivity.class.getSimpleName();
	private long groupId;
	int mStackLevel = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setTitle("Contacts");
		setContentView(R.layout.activity_group_view);

		Log.i(TAG, "started ContactListGroupAddContactActivity");
		datasource = new ContactsDataSource(this);

		textView = (TextView) findViewById(R.id.textView);
		imageButtonDelete = (ImageButton) findViewById(R.id.imageButtonDelete);
		imageButtonEdit = (ImageButton) findViewById(R.id.imageButtonEdit);
		imageButtonDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * new MyAlertDialogFragment().show(getFragmentManager(),
				 * "MyDialog");
				 */DialogFragment newFragment = MyAlertDialogFragment
						.newInstance(groupId);
				newFragment.show(getFragmentManager(), "dialog");
			}

		});

		imageButtonEdit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, EditGroupActivity.class);
				i.putExtra("groupId", groupId);
				startActivity(i);
			}

		});

		searchView = (SearchView) findViewById(R.id.searchView);
		lv = (ListView) findViewById(android.R.id.list);
		buttonAdd = (Button) findViewById(R.id.buttonAdd);

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
		datasource.open();
		Bundle b = getIntent().getExtras();
		groupId = b.getLong("groupId");

		textView.setText("Group " + datasource.findGroupbyId(groupId).getName());
		contacts = (ArrayList<Contact>) datasource
				.findContactsbyGroupId(groupId);

		contactsSearchResult = new ArrayList<Contact>();

		for (Contact contact : contacts) {
			contactsSearchResult.add(contact);
		}

		Collections.sort(contactsSearchResult, new ContactsCompareByName());

		TextView empty = (TextView)findViewById(R.id.empty);
		
		lv.setEmptyView(empty);
		lv.setAdapter(new ContactListAdapter(this,
				R.id.activityContactListTextView, contactsSearchResult));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent i = new Intent(context, MyTabActivity.class);
				i.putExtra("contactId", contactsSearchResult.get(position)
						.getId());
				startActivity(i);

			}

		});
		// lv.setAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.countries)));

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextChange(String newText) {
				contactsSearchResult.clear();
				for (int i = 0; i < contacts.size(); i++) {
					if (contacts.get(i).getName().toLowerCase()
							.contains(newText.toString().toLowerCase())
							|| contacts.get(i).getCompany().toLowerCase()
									.contains(newText.toString().toLowerCase())
							|| contacts.get(i).getTitle().toLowerCase()
									.contains(newText.toString().toLowerCase())) {
						contactsSearchResult.add(contacts.get(i));
					}
				}

				Collections.sort(contactsSearchResult,
						new ContactsCompareByName());

				lv.setAdapter(new ContactListAdapter(ViewGroupActivity.this,
						R.id.activityContactListTextView, contactsSearchResult));
				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				return true;
			}
		});

		buttonAdd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(context,
						ContactListGroupAddContactActivity.class);
				i.putExtra("groupId", groupId);
				startActivity(i);
			}
		});
	}

	@Override
	protected void onPause() {

		super.onPause();
		datasource.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_list, menu);
		return true;
	}

	private void deleteGroup(long groupId) {
		datasource.deleteGroupById(groupId);
		finish();
	}

	public static class MyAlertDialogFragment extends DialogFragment {

		public static MyAlertDialogFragment newInstance(long groupId) {
			MyAlertDialogFragment frag = new MyAlertDialogFragment();
			Bundle args = new Bundle();
			args.putLong("groupId", groupId);
			frag.setArguments(args);
			return frag;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final long groupId = getArguments().getLong("groupId");

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					getActivity()).setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Are you sure you want to delete this group?");
			alertDialogBuilder.setPositiveButton(R.string.OK,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							((ViewGroupActivity) getActivity())
									.deleteGroup(groupId);
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

}
