package com.example.prolog;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NewFollowUpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("New Follow Up");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_follow_up);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_follow_up, menu);
		return true;
	}

}
