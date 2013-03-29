package com.example.prolog;

import java.io.FileNotFoundException;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

public class EditContactFragment extends Fragment {

	ContactsDataSource datasource;
	Contact contact;
	long contactId;
	EditText tvName, tvTitle, tvCompany, tvPhone, tvEmail, tvLocation;
	QuickContactBadge quickContactBadge;
	public static final String TAG = EditContactFragment.class.getSimpleName();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		contactId = getArguments().getLong("contactId");

		datasource = new ContactsDataSource(getActivity());
		datasource.open();
		contact = datasource.findContactbyId(contactId);
		quickContactBadge = (QuickContactBadge) getActivity().findViewById(
				R.id.quickContactBadge);
		if (contact.getPhoto() != null)
			quickContactBadge.setImageBitmap(contact.getPhoto());
		else
			quickContactBadge.setImageResource(R.drawable.face);
		quickContactBadge.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 0);
			}
		});

		tvName = (EditText) getActivity().findViewById(R.id.editTextName);
		tvTitle = (EditText) getActivity().findViewById(R.id.editTextTitle);
		tvCompany = (EditText) getActivity().findViewById(R.id.editTextCompany);
		tvPhone = (EditText) getActivity().findViewById(R.id.editTextPhone);
		tvEmail = (EditText) getActivity().findViewById(R.id.editTextEmail);
		tvLocation = (EditText) getActivity().findViewById(
				R.id.editTextLocation);
		Button buttonSave = (Button) getActivity().findViewById(
				R.id.activityAddNewContactSaveButton);
		Button buttonCancel = (Button) getActivity().findViewById(
				R.id.activityAddNewContactCancelButton);
		tvName.setText(contact.getName());
		tvTitle.setText(contact.getTitle());
		tvCompany.setText(contact.getCompany());
		tvPhone.setText(contact.getHome_phone());
		tvEmail.setText(contact.getEmail());
		tvLocation.setText(contact.getLocation());

		buttonSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				contact.setName(tvName.getText().toString());
				contact.setTitle(tvTitle.getText().toString());
				contact.setCompany(tvCompany.getText().toString());
				contact.setHome_phone(tvPhone.getText().toString());
				contact.setEmail(tvEmail.getText().toString());
				contact.setLocation(tvLocation.getText().toString());
				BitmapDrawable bitmapDrawable = ((BitmapDrawable) quickContactBadge
						.getDrawable());
				contact.setPhoto(bitmapDrawable.getBitmap());
				datasource.updateContact(contact);
				Bundle b = new Bundle();
				b.putLong("contactId", contactId);
				ViewContactFragment viewContactFragment = new ViewContactFragment();
				viewContactFragment.setArguments(b);
				FragmentManager fmi = getFragmentManager();
				FragmentTransaction ftu = fmi.beginTransaction();
				ftu.replace(android.R.id.content, viewContactFragment)
						.addToBackStack(null).commit();
			}
		});

		buttonCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putLong("contactId", contactId);
				ViewContactFragment viewContactFragment = new ViewContactFragment();
				viewContactFragment.setArguments(b);
				FragmentManager fmi = getFragmentManager();
				FragmentTransaction ftu = fmi.beginTransaction();
				ftu.replace(android.R.id.content, viewContactFragment)
						.addToBackStack(null).commit();

			}
		});

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		datasource.open();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		datasource.close();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return (LinearLayout) inflater.inflate(
				R.layout.activity_add_new_contact, container, false);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			Uri targetUri = data.getData();
			// textTargetUri.setText(targetUri.toString());
			Bitmap bitmap;
			try {
				bitmap = BitmapFactory.decodeStream(getActivity()
						.getContentResolver().openInputStream(targetUri));
				quickContactBadge.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
				Log.e(TAG, e.getMessage());
			}
		}
	}
}
