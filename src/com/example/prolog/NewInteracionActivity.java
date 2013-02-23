package com.example.prolog;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NewInteracionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_interacion);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_interacion, menu);
		return true;
	}

}
