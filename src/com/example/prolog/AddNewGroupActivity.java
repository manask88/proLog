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

public class AddNewGroupActivity extends Activity {
	private EditText editTextGroupName;
	private Context context = this;
	private ContactsDataSource datasource;
	private Button buttonSave,buttonCancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setTitle("Add New Contact");
		setContentView(R.layout.activity_add_new_group);
		editTextGroupName = (EditText) findViewById(R.id.editTextGroupName);
		buttonSave = (Button) findViewById(R.id.activityAddNewGroupSaveButton);
		buttonCancel = (Button) findViewById(R.id.activityAddNewGroupCancelButton);
		datasource = new ContactsDataSource(context);
		
		
		buttonSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Group group = new Group();
				group.setName(editTextGroupName.getText().toString());
				datasource.open();
				group = datasource.createGroup(group);
				datasource.close();
				
				
				Intent i = new Intent(context, ViewGroupActivity.class);
				i.putExtra("groupId", group.getId());
				startActivity(i);
				finish();
			}
		});
		
		buttonCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_contact, menu);
		return true;
	}

}
