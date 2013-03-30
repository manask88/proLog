package com.example.prolog;

import java.io.FileNotFoundException;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

public class AddNewContactActivity extends Activity {

	private ContactsDataSource datasource;
	private Context context = this;
	public static final String TAG = AddNewContactActivity.class
			.getSimpleName();
	QuickContactBadge quickContactBadge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setTitle("Add New Contact");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_contact);

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

		Button button = (Button) findViewById(R.id.activityAddNewContactSaveButton);

		button.setOnClickListener(new View.OnClickListener() {

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
					BitmapDrawable bitmapDrawable = ((BitmapDrawable) quickContactBadge
							.getDrawable());
					c.setPhoto(bitmapDrawable.getBitmap());
					c = datasource.createContact(c);
					datasource.close();

					Intent i = new Intent(context, MyTabActivity.class);
					i.putExtra("contactId", c.getId());
					startActivity(i);
				} else {
					Toast.makeText(context, Commons.pleaseEnterNameContact,
							Toast.LENGTH_SHORT).show();

				}
			}
		});

		Button button2 = (Button) findViewById(R.id.activityAddNewContactCancelButton);

		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
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
