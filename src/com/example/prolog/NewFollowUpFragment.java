package com.example.prolog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.FollowUp;
import com.example.prolog.model.Group;

import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class NewFollowUpFragment extends Fragment {
	private ContactsDataSource datasource;

	private String frequency;
	private Button datePickerButton;
	private EditText editTextDate;
	int year;
	int month;
	int day;
	long contactId;
	RadioButton radioButtonDay, radioButtonWeek, radioButtonMonth;
	RadioGroup radioGroup;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return (RelativeLayout) inflater.inflate(
				R.layout.activity_new_follow_up, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		contactId = getArguments().getLong("contactId");

		datasource = new ContactsDataSource(getActivity());
		datasource.open();
		super.onCreate(savedInstanceState);
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		editTextDate = (EditText) getView().findViewById(R.id.editTextDate);

		radioGroup = (RadioGroup) getView().findViewById(R.id.radioGroup);
		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup rGroup,
							int checkedId) {

						RadioButton checkedRadioButton = (RadioButton) rGroup
								.findViewById(checkedId);
						boolean isChecked = checkedRadioButton.isChecked();
						Date date = new Date();
						// Check which radio button was clicked
						date.setDate(day);
						date.setMonth(month);
						date.setYear(year);

						Calendar c = Calendar.getInstance();

						switch (checkedId) {
						case R.id.radioButtonDay:
							if (isChecked) {
								// date.
								c.add(Calendar.DAY_OF_MONTH, 1);
								int month = c.get(Calendar.MONTH);
								editTextDate.setText(month + 1 + "/"
										+ c.get(Calendar.DAY_OF_MONTH) + "/"
										+ c.get(Calendar.YEAR));
							}

							break;

						case R.id.radioButtonWeek:
							if (isChecked) {
								frequency = "WEEKLY";
								// date.
								c.add(Calendar.DAY_OF_MONTH, 7);
								int month = c.get(Calendar.MONTH);
								editTextDate.setText(month + 1 + "/"
										+ c.get(Calendar.DAY_OF_MONTH) + "/"
										+ c.get(Calendar.YEAR));
							}

							break;
						case R.id.radioButtonMonth:
							if (isChecked) {
								frequency = "MONTHLY";
								date.setMonth(date.getMonth() + 1);

								c.add(Calendar.MONTH, 1);
								int month = c.get(Calendar.MONTH);
								editTextDate.setText(month + 1 + "/"
										+ c.get(Calendar.DAY_OF_MONTH) + "/"
										+ c.get(Calendar.YEAR));

							}
							break;
						}

					}

				});
		radioButtonDay = (RadioButton) getView().findViewById(
				R.id.radioButtonDay);
		radioButtonWeek = (RadioButton) getView().findViewById(
				R.id.radioButtonWeek);
		radioButtonMonth = (RadioButton) getView().findViewById(
				R.id.radioButtonMonth);

		// editText1.setText((month + 1) + "/" + day + "/" + year);
		editTextDate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				radioButtonDay.setChecked(false);
				radioButtonWeek.setChecked(false);
				radioButtonMonth.setChecked(false);

				DatePickerFragment newFragment = new DatePickerFragment()
						.setEditText(editTextDate);
				newFragment.show(getFragmentManager(), "datePicker");
			}
		});

		Button buttonSave = (Button) getView().findViewById(R.id.buttonSave);
		Button buttonCancel = (Button) getView()
				.findViewById(R.id.buttonCancel);
		buttonCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				FragmentManager fmi = getFragmentManager();
				fmi.popBackStack();
			}
		});

		buttonSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				EditText editTextTitle = (EditText) getView().findViewById(
						R.id.editTextTitle);
				String stringTitle = editTextTitle.getText().toString();
				if (!stringTitle.equals(""))

				{

					FollowUp followUp = new FollowUp();

					EditText editTextNotes = (EditText) getView().findViewById(
							R.id.editTextNotes);

					followUp.setContactId(contactId);
					followUp.setTitle(editTextTitle.getText().toString());
					followUp.setNotes(editTextNotes.getText().toString());
					followUp.setDate(editTextDate.getText().toString());

					followUp = datasource.createFollowUp(followUp);

					FragmentManager fmi = getFragmentManager();
					fmi.popBackStack();
				} else {

					Toast.makeText(getActivity(),
							Commons.pleaseEnterTitleFollowUp,
							Toast.LENGTH_SHORT).show();
				}

				/*
				 * Bundle b = new Bundle(); b.putLong(Commons.CONTACT_ID,
				 * contactId); NewFollowUpFragment newFollowUpFragment = new
				 * NewFollowUpFragment(); newFollowUpFragment.setArguments(b);
				 * FragmentManager fmi = getFragmentManager();
				 * FragmentTransaction ftu = fmi.beginTransaction();
				 * ftu.replace(android.R.id.content, newFollowUpFragment)
				 * .addToBackStack(null).commit();
				 */
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		datasource.close();
	}

}
