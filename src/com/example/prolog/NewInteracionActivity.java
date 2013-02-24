package com.example.prolog;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;
import com.example.prolog.model.Interaction;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class NewInteracionActivity extends Activity {

	private Button datePickerButton;
	private EditText editText1;
	private Button saveButton;
	private ContactsDataSource datasource;
	private Context context = this;
	private Button cancelButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_interacion);
		datePickerButton = (Button) findViewById(R.id.newInteractionActivityDatePicker);
		datePickerButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				editText1 = (EditText) findViewById(R.id.newInteractionActivityEditTextDate);
			    DatePickerFragment newFragment = new DatePickerFragment().setEditText(editText1);
			    newFragment.show(getFragmentManager(),"datePicker");
			    }
		});
		
		saveButton = (Button) findViewById(R.id.newInteractionActivitySaveButton);
		saveButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				EditText etDate= (EditText) findViewById(R.id.newInteractionActivityEditTextDate);
				CheckBox cbFollowUp= (CheckBox) findViewById(R.id.newInteractionActivityCheckBoxFollowUp);
				EditText etText= (EditText) findViewById(R.id.newInteractionActivityEditTextText);
				
				datasource=new ContactsDataSource(context);
				datasource.open();
				Interaction inter = new Interaction();
				inter.setDate(etDate.getText().toString());
				inter.setText(etText.getText().toString());
				Bundle b = getIntent().getExtras();
				inter.setContactId(b.getLong("contactId"));
				datasource.createInteraction(inter);
				datasource.close();
				
				Intent i = new Intent(context, MyTabActivity.class);
				i.putExtra("contactId", b.getLong("contactId"));
				startActivity(i);
			}
		});
		
		cancelButton = (Button) findViewById(R.id.newInteractionActivityCancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_interacion, menu);
		return true;
	}
	
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment().setEditText(editText1);
	    newFragment.show(getFragmentManager(), "datePicker");
	}

}
