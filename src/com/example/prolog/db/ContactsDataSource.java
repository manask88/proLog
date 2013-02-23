package com.example.prolog.db;

import java.util.ArrayList;
import java.util.List;

import com.example.prolog.model.Contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactsDataSource {

	public static final String LOGTAG="EXPLORECA";
	
	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	
	private static final String[] allColumns = {
		DBOpenHelper.COLUMN_ID,
		DBOpenHelper.COLUMN_NAME,
		DBOpenHelper.COLUMN_TITLE,
		DBOpenHelper.COLUMN_COMPANY, 
		DBOpenHelper.COLUMN_HOME_PHONE, 
		DBOpenHelper.COLUMN_WORK_PHONE,
		DBOpenHelper.COLUMN_EMAIL,
		DBOpenHelper.COLUMN_LOCATION
	};
	
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
	
	public Contact findContactbyId(long id) {
	
		Contact contact=null;
		Cursor cursor = database.query(DBOpenHelper.TABLE_CONTACTS, allColumns, 
				DBOpenHelper.COLUMN_ID + "=?", new String[] {Long.toString(id)}, null, null, null);
		
		if (cursor != null)
        {
			
			if (cursor.moveToFirst())
            {
			
			contact=new Contact();
			contact.setName(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_NAME)));
			
			
            }
			cursor.close();
        }
			
		return contact;
		
	}
	
	public ArrayList<Contact> findAll() {
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		
		Cursor cursor = database.query(DBOpenHelper.TABLE_CONTACTS, allColumns, 
				null, null, null, null, null);
		
		Log.i(LOGTAG, "Returned" + cursor.getCount() + " rows" );
		
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Contact contact = new Contact();
				contact.setId(cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COLUMN_ID)));
				contact.setName(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_NAME)));
				contact.setTitle(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_TITLE)));				contact.setName(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_NAME)));
				contact.setCompany(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_COMPANY)));
				contact.setHome_phone(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_HOME_PHONE)));				contact.setHome_phone(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_NAME)));
				contact.setWork_phone(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_WORK_PHONE)));
				contact.setLocation(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_LOCATION)));				contact.setHome_phone(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_NAME)));
			
				contacts.add(contact);

				

				
			}
			
		}
		cursor.close();
		return contacts;
		
		
	}
}
