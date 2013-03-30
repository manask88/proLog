package com.example.prolog;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prolog.model.Contact;

public class ContactListAdapter extends ArrayAdapter<Contact> {
	private Context context;
	private ArrayList<Contact> items;

	public ContactListAdapter(Context context, int textViewResourceId,
			ArrayList<Contact> contacts) {
		super(context, textViewResourceId, contacts);
		this.context = context;
		items = contacts;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View row = inflater.inflate(R.layout.activity_contact_list_item,
				parent, false);

		ImageView iv = (ImageView) row
				.findViewById(R.id.activityContactListImageView);
		TextView tv = (TextView) row
				.findViewById(R.id.activityContactListTextView);
		TextView tvContactListTextViewTitle = (TextView) row
				.findViewById(R.id.activityContactListTextViewTitle);
		TextView tvContactListTextViewCompany = (TextView) row
				.findViewById(R.id.activityContactListTextViewCompany);
		tv.setText(items.get(position).getName());
		tvContactListTextViewTitle.setText(items.get(position).getTitle());
		tvContactListTextViewCompany.setText(items.get(position).getCompany());
		if (items.get(position).getPhoto() != null)
			iv.setImageBitmap(items.get(position).getPhoto());
		else
			iv.setImageResource(R.drawable.face);

		return row;
	}
}