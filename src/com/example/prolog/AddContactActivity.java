package com.example.prolog;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddContactActivity extends Activity {
	private TextView textView1;
	private Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(context,AddNewContactActivity.class));
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_contact, menu);
		return true;
	}

}
