package com.example.prolog;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNewContactActivity extends Activity {
	
	private ContactsDataSource datasource;
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_contact);
		
		Button button = (Button) findViewById(R.id.saveButton);
		
		button.setOnClickListener( new View.OnClickListener() {
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText etName= (EditText) findViewById(R.id.editTextDate);
				EditText etTitle= (EditText) findViewById(R.id.editTextText);
				EditText etCompany= (EditText) findViewById(R.id.editTextCompany);
				EditText etPhone= (EditText) findViewById(R.id.editTextPhone);
				EditText etEmail= (EditText) findViewById(R.id.editTextEmail);
				EditText etLocation= (EditText) findViewById(R.id.editTextLocation);
				
				datasource=new ContactsDataSource(context);
				datasource.open();
				Contact c = new Contact();
				c.setName(etName.getText().toString());
				c.setTitle(etTitle.getText().toString());
				c.setCompany(etCompany.getText().toString());
				c.setHome_phone(etPhone.getText().toString());
				c.setWork_phone(etPhone.getText().toString());
				c.setEmail(etEmail.getText().toString());
				c.setLocation(etLocation.getText().toString());
				datasource.createContact(c);
				datasource.close();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new_contact, menu);
		return true;
	}

}
