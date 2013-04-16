package com.example.prolog;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class AddNewContactActivity extends Activity {

	private ContactsDataSource datasource;
	private Context context = this;
	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
	public static final String TAG = AddNewContactActivity.class
			.getSimpleName();
	QuickContactBadge quickContactBadge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setTitle("Add New Contact");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_contact);

		// Hashmap for custom field
		final HashMap<Integer, Integer> customFieldIDs = new HashMap<Integer, Integer>();
		// final HashMap<String, String> customFields = new HashMap<String,
		// String>();

		// button to add field
		Button buttAddField = (Button) findViewById(R.id.activityAddNewFieldButton);
		buttAddField.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					TableLayout tl = (TableLayout) findViewById(R.id.add_new_contact_lo);
					TableRow.LayoutParams lparams = new TableRow.LayoutParams(
							TableRow.LayoutParams.WRAP_CONTENT,
							TableRow.LayoutParams.WRAP_CONTENT);
					EditText editNewFieldName = new EditText(
							AddNewContactActivity.this);
					editNewFieldName.setLayoutParams(lparams);
					editNewFieldName.setTextColor(Color.WHITE);
					editNewFieldName.setHint("New field");
					editNewFieldName.requestFocus();
					editNewFieldName.setEms(4);

					EditText editNewFieldValue = new EditText(
							AddNewContactActivity.this);
					editNewFieldValue.setLayoutParams(lparams);
					editNewFieldValue.setTextColor(Color.WHITE);
					editNewFieldValue.setHint("Value");
					editNewFieldValue.setEms(10);

					int idNameField = generateViewId();
					int idValueField = generateViewId();

					customFieldIDs.put(idNameField, idValueField);
					editNewFieldName.setId(idNameField);
					editNewFieldValue.setId(idValueField);

					// create a new row for label and edit text field
					TableRow.LayoutParams tparams = new TableRow.LayoutParams(
							TableRow.LayoutParams.MATCH_PARENT,
							TableRow.LayoutParams.MATCH_PARENT);
					TableRow tr = new TableRow(AddNewContactActivity.this);
					tr.setLayoutParams(tparams);

					tr.addView(editNewFieldName);
					tr.addView(editNewFieldValue);

					tl.addView(tr);
				} catch (Exception e) {
					Log.d("test", e.toString());
				}

			}
		});

		quickContactBadge = (QuickContactBadge) findViewById(R.id.quickContactBadge);
		quickContactBadge.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 0);
				Log.d(TAG, "trying to go to media store");
			}
		});

		Button saveButton = (Button) findViewById(R.id.activityAddNewContactSaveButton);

		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText etName = (EditText) findViewById(R.id.editTextName);

				if (!etName.getText().toString().equals(""))

				{
					EditText etTitle = (EditText) findViewById(R.id.editTextTitle);
					EditText etCompany = (EditText) findViewById(R.id.editTextCompany);
					EditText etPhone = (EditText) findViewById(R.id.editTextPhone);
					EditText etEmail = (EditText) findViewById(R.id.editTextEmail);
					EditText etLocation = (EditText) findViewById(R.id.editTextLocation);

					datasource = new ContactsDataSource(context);
					datasource.open();
					Contact c = new Contact();
					c.setName(etName.getText().toString());
					c.setTitle(etTitle.getText().toString());
					c.setCompany(etCompany.getText().toString());
					c.setHome_phone(etPhone.getText().toString());
					c.setWork_phone(etPhone.getText().toString());
					c.setEmail(etEmail.getText().toString());
					c.setLocation(etLocation.getText().toString());

					// Save custom fields as well
					Set<Integer> keys = customFieldIDs.keySet();
					for (Integer integer : keys) {
						// get the field name
						String fieldName = ((EditText) findViewById(integer
								.intValue())).getText().toString();
						String fieldValue = ((EditText) findViewById(customFieldIDs
								.get(integer).intValue())).getText().toString();
						c.setCustomField(fieldName, fieldValue);
					}

					BitmapDrawable bitmapDrawable = ((BitmapDrawable) quickContactBadge
							.getDrawable());
					c.setPhoto(bitmapDrawable.getBitmap());
					c = datasource.createContact(c);
					datasource.close();

					Intent i = new Intent(context, MyTabActivity.class);
					i.putExtra("contactId", c.getId());
					startActivity(i);
					finish();
				} else {
					Toast.makeText(context, Commons.pleaseEnterNameContact,
							Toast.LENGTH_SHORT).show();

				}
			}
		});

		Button cancelButton = (Button) findViewById(R.id.activityAddNewContactCancelButton);

		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	/**
	 * Generate a value suitable for use in {@link #setId(int)}. This value will
	 * not collide with ID values generated at build time by aapt for R.id.
	 * 
	 * @return a generated ID value
	 */
	private static int generateViewId() {
		for (;;) {
			final int result = sNextGeneratedId.get();
			// aapt-generated IDs have the high byte nonzero; clamp to the range
			// under that.
			int newValue = result + 1;
			if (newValue > 0x00FFFFFF)
				newValue = 1; // Roll over to 1, not 0.
			if (sNextGeneratedId.compareAndSet(result, newValue)) {
				return result;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new_contact, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "was able to get into media store");

		if (resultCode == RESULT_OK) {
			Uri targetUri = data.getData();
			// textTargetUri.setText(targetUri.toString());
			Bitmap bitmap;
			try {
				bitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(targetUri));
				quickContactBadge.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
				Log.e(TAG, e.getMessage());
			}
		}
	}

}
