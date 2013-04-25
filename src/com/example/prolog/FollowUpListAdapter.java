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
import com.example.prolog.model.FollowUp;

public class FollowUpListAdapter extends ArrayAdapter<FollowUp> {
	private Context context;
	private ArrayList<FollowUp> items;


	public FollowUpListAdapter(Context context, int textViewResourceId,
			ArrayList<FollowUp> followUps) {
		super(context, textViewResourceId, followUps);
		this.context = context;
		items = followUps;

	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View row = inflater.inflate(R.layout.fragment_view_followups_item,
				parent, false);

		
		TextView textViewTitle = (TextView) row
				.findViewById(R.id.textViewTitle);
		TextView textViewDueDate = (TextView) row
				.findViewById(R.id.textViewDueDate);
		
		textViewTitle.setText(items.get(position).getTitle());
		textViewDueDate.setText(items.get(position).getDate());
		

		return row;
	}
}