package com.example.prolog;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandListAdapterFragmentInteractions extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<ExpandListGroupFragmentInteractions> groups;
	public ExpandListAdapterFragmentInteractions(Context context, ArrayList<ExpandListGroupFragmentInteractions> groups) {
		this.context = context;
		this.groups = groups;
	}
	
	/*public void addItem(ExpandListChild item, ExpandListGroupFragmentInteractions group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
		int index = groups.indexOf(group);
		List<ExpandListChild> ch = groups.get(index).getItems();
		ch.add(item);
		groups.get(index).setItems(ch);
	}*/
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		ExpandListChildFragmentInteractions chList = groups.get(groupPosition).getChildInteraction();
		return chList;
	}

	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		ExpandListChildFragmentInteractions child = (ExpandListChildFragmentInteractions) getChild(groupPosition, childPosition);
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.fragment_interactions_expandlist_child_item, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.textViewFragmentInteractionsNotes);
		tv.setText(child.getName().toString());
		tv.setTag(child.getTag());
		// TODO Auto-generated method stub
		return view;
	}

	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		/*List<ExpandListChild> chList = groups.get(groupPosition).getItems();
		return chList.size();*/
		boolean condition=groups.get(groupPosition).getChildInteraction()!=null;
		if (condition) return 1;
		else return 0;

	}

	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groups.get(groupPosition);
	}

	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.size();
	}

	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	public View getGroupView(final int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		final ExpandListGroupFragmentInteractions group = (ExpandListGroupFragmentInteractions) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.fragment_interactions_expandlist_group_item, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.tvGroup);
		tv.setText(group.getName());

	
		ImageButton imageButton = (ImageButton) view.findViewById(R.id.imageButton);
		imageButton.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {

				
				Activity activity=(Activity) context;
				Bundle b = 	activity.getIntent().getExtras();
				long contactId = b.getLong("contactId");
				/*
				Intent i = new Intent(context, ViewFullInteractionActivity.class);
				i.putExtra("interactionId",group.getId());
				i.putExtra("contactId",	contactId);
				context.startActivity(i);
				*/
				
				 b = new Bundle();
				 b.putLong("interactionId", group.getId());
				b.putLong(Commons.CONTACT_ID, contactId);
				ViewFullInteractionFragment viewFullInteractionFragment = new ViewFullInteractionFragment();
				viewFullInteractionFragment.setArguments(b);
				FragmentManager fmi = activity.getFragmentManager();
				FragmentTransaction ftu = fmi.beginTransaction();
				ftu.replace(android.R.id.content, viewFullInteractionFragment)
						.addToBackStack(null).commit();
				
				
			}
		});
		return view;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

}


