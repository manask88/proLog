package com.example.prolog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactsDataSource {

	public static final String LOGTAG="EXPLORECA";
	
	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	
	public ContactsDataSource(Context context) {
		dbhelper = new DBOpenHelper(context);
		
	}
	
	public void open () {
		Log.i(LOGTAG, "Database opened");
		database = dbhelper.getWritableDatabase();
		
	}
	
	
	public void close() {
		Log.i(LOGTAG, "Database closed");
		dbhelper.close();
		
	}
}
