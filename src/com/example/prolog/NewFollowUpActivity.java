package com.example.prolog;

import java.util.Calendar;
import java.util.TimeZone;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class NewFollowUpActivity extends Activity {

	private String frequency;
	private Button datePickerButton;
	private EditText editText1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_follow_up);
		
		datePickerButton = (Button) findViewById(R.id.NewFollowUpActivityDatePicker);
		datePickerButton.setOnClickListener(new View.OnClickListener() {


			public void onClick(View v) {
				editText1 = (EditText) findViewById(R.id.newFollowUpActivityDateEditText);
			    DatePickerFragment newFragment = new DatePickerFragment().setEditText(editText1);
			    newFragment.show(getFragmentManager(),"datePicker");
			    }
		});
		
		Button saveButton = (Button) findViewById(R.id.newFollowUpActivitySaveButton);
		saveButton.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Uri eventsUri;
		        if (android.os.Build.VERSION.SDK_INT <= 7) {

		            eventsUri = Uri.parse("content://calendar/events");
		        } else {

		            eventsUri = Uri.parse("content://com.android.calendar/events");
		        }

		        Calendar cal = Calendar.getInstance();
		        cal.set(Integer.parseInt(editText1.getText().toString().split("/")[2]),
		        		Integer.parseInt(editText1.getText().toString().split("/")[0]),
		        		Integer.parseInt(editText1.getText().toString().split("/")[1]));
		        ContentValues event = new ContentValues();
		        event.put("calendar_id", 1);
		        event.put("title", ((EditText) findViewById(R.id.newFollowUpActivityEditTextTitle)).getText().toString());
		        event.put("description", ((EditText) findViewById(R.id.newFollowUpActivityEditTextText)).getText().toString());
		        event.put("eventLocation", "Event Location");
		        event.put("eventTimezone", TimeZone.getDefault().getID());
		        event.put("dtstart",cal.getTimeInMillis());
				event.put("rrule", "FREQ="+frequency +";WKST=SU");
		        event.put("allDay", 0);   // 0 for false, 1 for true
		        event.put("eventStatus", 1);
		        event.put("hasAlarm", 1); // 0 for false, 1 for true
		        event.put("duration","P3600S");
		        Uri url = getContentResolver().insert(eventsUri, event);
			}
		});		
	}
	
	public void onRadioButtonClicked(View view) {
        frequency = new String();
        boolean checked = ((RadioButton) view).isChecked();
        
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.newFollowUpActivityRadioButton2:
                if (checked)
                	frequency = "WEEKLY";
                break;
            case R.id.newFollowUpActivityRadioButton3:
                if (checked)
                	frequency = "MONTHLY";
                break;		            	
        }
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_follow_up, menu);
		return true;
	}

}
