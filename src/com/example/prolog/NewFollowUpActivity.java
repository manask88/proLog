package com.example.prolog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;
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
	int year ;
	int month;
	int day;
	RadioButton radioButtonDay,radioButtonWeek,radioButtonMonth;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("New Follow Up");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_follow_up);
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		editText1 = (EditText) findViewById(R.id.newFollowUpActivityDateEditText);
		
		radioButtonDay=(RadioButton) findViewById(R.id.radioButtonDay);
		radioButtonWeek=(RadioButton) findViewById(R.id.radioButtonWeek);
		radioButtonMonth=(RadioButton) findViewById(R.id.radioButtonMonth);

		//editText1.setText((month + 1) + "/" + day + "/" + year);
		datePickerButton = (Button) findViewById(R.id.NewFollowUpActivityDatePicker);
		datePickerButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				radioButtonDay.setChecked(false);
				radioButtonWeek.setChecked(false);
				radioButtonMonth.setChecked(false);
			}
		});

		Button saveButton = (Button) findViewById(R.id.newFollowUpActivitySaveButton);
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*Uri eventsUri;
				if (android.os.Build.VERSION.SDK_INT <= 7) {

					eventsUri = Uri.parse("content://calendar/events");
				} else {

					eventsUri = Uri
							.parse("content://com.android.calendar/events");
				}

				Calendar cal = Calendar.getInstance();
				cal.set(Integer.parseInt(editText1.getText().toString()
						.split("/")[2]),
						Integer.parseInt(editText1.getText().toString()
								.split("/")[0]),
						Integer.parseInt(editText1.getText().toString()
								.split("/")[1]));
				ContentValues event = new ContentValues();
				event.put("calendar_id", 1);
				event.put(
						"title",
						((EditText) findViewById(R.id.newFollowUpActivityEditTextTitle))
								.getText().toString());
				event.put(
						"description",
						((EditText) findViewById(R.id.newFollowUpActivityEditTextText))
								.getText().toString());
				event.put("eventLocation", "Event Location");
				event.put("eventTimezone", TimeZone.getDefault().getID());
				event.put("dtstart", cal.getTimeInMillis());
				event.put("rrule", "FREQ=" + frequency + ";WKST=SU");
				event.put("allDay", 0); // 0 for false, 1 for true
				event.put("eventStatus", 1);
				event.put("hasAlarm", 1); // 0 for false, 1 for true
				event.put("duration", "P3600S");
				Uri url = getContentResolver().insert(eventsUri, event);
				*/
				/*
				Intent intent = new Intent(Intent.ACTION_EDIT );
				intent.setType("vnd.android.cursor.item/event");
				intent.putExtra(Events.TITLE, "Learn Android");
				intent.putExtra(Events.EVENT_LOCATION, "Home suit home");
				intent.putExtra(Events.DESCRIPTION, "Download Examples");

				// Setting dates
				GregorianCalendar calDate = new GregorianCalendar(2013, 04, 15);
				intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
				  calDate.getTimeInMillis());
				intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
				  calDate.getTimeInMillis());

				// Make it a full day event
				intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

				// Make it a recurring Event
				intent.putExtra(Events.RRULE, "FREQ=WEEKLY;COUNT=11;WKST=SU;BYDAY=TU,TH");

				// Making it private and shown as busy
				intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
				intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY); 
				startActivity(intent); */
			}
		});
	}

	public void onRadioButtonClicked(View view) {
		frequency = new String();
		boolean checked = ((RadioButton) view).isChecked();
		Date date= new Date();
		// Check which radio button was clicked
	date.setDate(day);
	date.setMonth(month);
	date.setYear(year);
	
	Calendar c = Calendar.getInstance();
		
		switch (view.getId()) {
		case R.id.radioButtonDay:
			if (checked)
			{		
			//date.
			c.add(Calendar.DAY_OF_MONTH, 1);
			int month = c.get(Calendar.MONTH);
			editText1.setText( month+1+ "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR) );
			}
			
			break;
		
		case R.id.radioButtonWeek:
			if (checked)
			{		frequency = "WEEKLY";
			//date.
			c.add(Calendar.DAY_OF_MONTH, 7);
			int month = c.get(Calendar.MONTH);
			editText1.setText( month+1+ "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR) );
			}
			
			break;
		case R.id.radioButtonMonth:
			if (checked)
			{	frequency = "MONTHLY";
			date.setMonth(date.getMonth()+1);
		
			c.add(Calendar.MONTH, 1);
			int month = c.get(Calendar.MONTH);
			editText1.setText( month+1+ "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR) );
			
			
			}
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
