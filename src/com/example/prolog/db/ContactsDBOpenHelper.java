package com.example.prolog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactsDBOpenHelper extends SQLiteOpenHelper {

	private static final String LOGTAG = "EXPLORECA";
	private static final String DATABASE_NAME = "prolog.db";
	private static final int DATABASE_VERSION = 2; /* change it whenever making changes on db scheleton*/
	
	public static final String TABLE_CONTACTS = "contacts";
	
	public static final String COLUMN_ID = "contactId";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_COMPANY = "company";
	public static final String COLUMN_HOME_PHONE = "home_phone";
	public static final String COLUMN_WORK_PHONE = "work_phone";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_LOCATION = "location";
	public static final String TABLE_INTERACTIONS = "interactions";
	public static final String COLUMN_INTERACTIONS_ID = "interactionId";
	public static final String COLUMN_INTERACTIONS_CONTACT_ID = "contactid";
	public static final String COLUMN_INTERACTIONS_DATE = "date";
	public static final String COLUMN_INTERACTIONS_FOLLOW_UP = "followUp";
	public static final String COLUMN_INTERACTIONS_TEXT = "text";



	
	
	private static final String TABLE_CREATE =
			"CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + " ( " +
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_NAME + " TEXT, " +
			COLUMN_TITLE + " TEXT, " +
			COLUMN_COMPANY + " TEXT, " +
			COLUMN_HOME_PHONE + " TEXT, " +
			COLUMN_WORK_PHONE + " TEXT, " +
			COLUMN_EMAIL + " TEXT, " +
			COLUMN_LOCATION + " TEXT" +
			")";
	private static final String TABLE_CREATE_INTERACTIONS =
			"CREATE TABLE IF NOT EXISTS " + TABLE_INTERACTIONS + " ( " +
			COLUMN_INTERACTIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_INTERACTIONS_CONTACT_ID + " INTEGER, " +
			COLUMN_INTERACTIONS_DATE + " TEXT, " +
			COLUMN_INTERACTIONS_FOLLOW_UP + " INTEGER, " +
			COLUMN_INTERACTIONS_TEXT + " TEXT" +
			")";
	
	
	
	
	
	public ContactsDBOpenHelper(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
		db.execSQL(TABLE_CREATE_INTERACTIONS);

		Log.i(LOGTAG, "Tables have been created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERACTIONS);

		onCreate(db);

	}

}
