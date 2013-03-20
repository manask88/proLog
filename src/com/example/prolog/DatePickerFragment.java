package com.example.prolog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import android.app.Dialog;
import android.os.Bundle;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	private EditText editText1;

	public DatePickerFragment() {
		// TODO Auto-generated constructor stub
	}

	public DatePickerFragment setEditText(EditText editText1) {
		// TODO Auto-generated constructor stub
		this.editText1 = editText1;
		if (this.editText1 == null)
			Log.i("malav", "null1");
		return this;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		String pattern = "MM/dd/yyyy";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date;
		try {
			date = (Date) format.parse(editText1.getText().toString());
			c.setTime(date);

			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			Log.e(SyncActivity.LOGTAG, "date extracted" + date);
			Log.e(SyncActivity.LOGTAG,
					"motnh extracted" + c.get(Calendar.MONTH));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.e(SyncActivity.LOGTAG, e.getMessage());
		}

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		if (editText1 == null)
			Log.i("malav", "null");
		editText1.setText(Integer.toString(arg2+1) + "/" + Integer.toString(arg3)
				+ "/" + Integer.toString(arg1));
	}
}