package com.example.prolog;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GroupListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("Groups");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_list, menu);
		return true;
	}

}
