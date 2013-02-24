package com.example.prolog;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewInteracionActivity extends Activity {

	private Button datePickerButton;
	private EditText editText1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_interacion);
		datePickerButton = (Button) findViewById(R.id.datePicker);
		datePickerButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				editText1 = (EditText) findViewById(R.id.editText1);
			    DatePickerFragment newFragment = new DatePickerFragment().setEditText(editText1);
			    newFragment.show(getFragmentManager(),"datePicker");
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
