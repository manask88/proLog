package com.example.prolog;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;
import com.example.prolog.model.Group;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditGroupActivity extends Activity {
	private EditText editTextGroupName;
	private Context context = this;
	private ContactsDataSource datasource;
	private Button buttonSave, buttonCancel;
	private long groupId;
	private Group group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setTitle("Add New Contact");
		setContentView(R.layout.activity_add_new_group);
		Bundle b = getIntent().getExtras();
		groupId = b.getLong("groupId");

		editTextGroupName = (EditText) findViewById(R.id.editTextGroupName);
		buttonSave = (Button) findViewById(R.id.activityAddNewGroupSaveButton);
		buttonCancel = (Button) findViewById(R.id.activityAddNewGroupCancelButton);
		datasource = new ContactsDataSource(context);

		
	}

	@Override
	protected void onResume() {
		super.onResume();
		datasource.open();
		group = datasource.findGroupbyId(groupId);
		editTextGroupName.setText(group.getName());
		
		
		buttonSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				String groupName = editTextGroupName.getText().toString();
				if (!groupName.equals(""))

				{
					group.setName(groupName);
					group = datasource.updateGroup(group);

				/*	Intent i = new Intent(context, ViewGroupActivity.class);
					i.putExtra("groupId", group.getId());
					startActivity(i);*/
					finish();
				}

				else {

					Toast.makeText(context, Commons.pleaseEnterNameGroup,
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		buttonCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
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
		getMenuInflater().inflate(R.menu.add_contact, menu);
		return true;
	}

}
