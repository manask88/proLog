package com.example.prolog;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<ExpandListGroup> groups;
	private ExpandListAdapter expandListAdapter = this;

	public ExpandListAdapter(Context context, ArrayList<ExpandListGroup> groups) {
		this.context = context;
		this.groups = groups;
	}

	public void addItem(ExpandListChild item, ExpandListGroup group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
		int index = groups.indexOf(group);
		List<ExpandListChild> ch = groups.get(index).getItems();
		ch.add(item);
		groups.get(index).setItems(ch);
	}

	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		List<ExpandListChild> chList = groups.get(groupPosition).getItems();
		return chList.get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {
		final ExpandListChild child = (ExpandListChild) getChild(groupPosition,
				childPosition);

		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.expandlist_child_item, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.tvChild);
		tv.setText(child.getName());
		// tv.setTag(child.getTag());

		CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
		checkBox.setChecked(child.isChecked());
		checkBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (child.isChecked()) {
					child.setChecked(false);
				} else {
					child.setChecked(true);
				}
				/*
				 * child.setName("child position"+childPosition);
				 * List<ExpandListChild> chList =
				 * groups.get(groupPosition).getItems();
				 * chList.set(childPosition, child); ExpandListGroup
				 * expandListGroup = groups.get(groupPosition);
				 * expandListGroup.setItems(chList);
				 * //groups.set(groupPosition,new ExpandListGroup());
				 * groups.set(groupPosition,expandListGroup);
				 */
			}
		});
		return view;
	}

	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		List<ExpandListChild> chList = groups.get(groupPosition).getItems();

		return chList.size();

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

	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		final ExpandListGroup group = (ExpandListGroup) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater inf = (LayoutInflater) context
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.expandlist_group_item, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.tvGroup);
		tv.setText(group.getName());
		CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
		checkBox.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				
				if (group.isChecked())
				group.setChecked(false);
				else
					group.setChecked(true);
				
				for (ExpandListChild child : group.getItems())
					child.setChecked(group.isChecked());
				notifyDataSetChanged();

			}
			
		
			
		});
		// TODO Auto-generated method stub
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
