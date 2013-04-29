package com.example.prolog;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	private ImageButton addNewContactBtn, addNewInteractionBtn, addFollowUpBtn;
	private ImageButton viewContactListBtn;
	private ImageButton viewGroupListBtn;
	private Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addNewInteractionBtn = (ImageButton) findViewById(R.id.addNewIteraction);
		
		addNewInteractionBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				Intent intent = new Intent(context,ContactListNewInteractionOrFollowUpActivity.class);
				intent.putExtra(Commons.NEXT_ACTION,Commons.NEXT_ACTION_NEW_INTERACTION);
				startActivity(intent);
			
				
				
			}
		});
		
		addFollowUpBtn = (ImageButton) findViewById(R.id.buttonFollowUp);
		addFollowUpBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				Intent intent = new Intent(context,ContactListNewInteractionOrFollowUpActivity.class);
				intent.putExtra(Commons.NEXT_ACTION,Commons.NEXT_ACTION_NEW_FOLLOW_UP);
				startActivity(intent);
			
				
				
			}
		});
		
		
		addNewContactBtn = (ImageButton) findViewById(R.id.add_new_contact);
		addNewContactBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				startActivity(new Intent(context,AddNewContactActivity.class));
				//startActivity(new Intent(context,AddContactActivity.class));
			}
		});
		
		viewContactListBtn = (ImageButton) findViewById(R.id.view_contact_list);
		viewContactListBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(context,ContactListActivity.class));
			}
		});
		
		viewGroupListBtn = (ImageButton) findViewById(R.id.view_group_list);
		viewGroupListBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(context,GroupListActivity.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/*
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if (keyCode == KeyEvent.KEYCODE_BACK) {
	            moveTaskToBack(true);
	            return true;
	        }
	        return super.onKeyDown(keyCode, event);
	 }*/
}
