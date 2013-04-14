package com.example.prolog;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prolog.model.Contact;

public class ContactListAdapterWithCheckBox extends ArrayAdapter<Contact> {
	private Context context;
	private ArrayList<Contact> items;

	public ContactListAdapterWithCheckBox(Context context,
			int textViewResourceId, ArrayList<Contact> contacts) {
		super(context, textViewResourceId, contacts);
		this.context = context;
		items = contacts;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View row = inflater.inflate(
				R.layout.activity_contact_list_item_with_checkbox, parent,
				false);

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

		CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkBox);
		checkBox.setChecked(items.get(position).isSelected());
		checkBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (items.get(position).isSelected()) {
					items.get(position).setSelected(false);
				} else {
					items.get(position).setSelected(true);
				}
				
			}
		});
		return row;
	}
}