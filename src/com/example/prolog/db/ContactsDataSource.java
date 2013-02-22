package com.example.prolog.db;

import com.example.prolog.model.Contact;

import android.content.ContentValues;
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
	
	public Contact create(Contact contact) {
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.COLUMN_NAME, contact.getName());
		values.put(DBOpenHelper.COLUMN_TITLE, contact.getTitle());
		values.put(DBOpenHelper.COLUMN_COMPANY, contact.getEmail());
		values.put(DBOpenHelper.COLUMN_HOME_PHONE, contact.getHome_phone());
		values.put(DBOpenHelper.COLUMN_WORK_PHONE, contact.getWork_phone());
		values.put(DBOpenHelper.COLUMN_EMAIL, contact.getEmail());
		values.put(DBOpenHelper.COLUMN_LOCATION, contact.getLocation());


		long insertid = database.insert(DBOpenHelper.TABLE_CONTACTS, null, values);
		contact.setId(insertid);
		return contact;
		
	}
}
