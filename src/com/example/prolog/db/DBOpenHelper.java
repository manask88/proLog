package com.example.prolog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String LOGTAG = "EXPLORECA";
	private static final String DATABASE_NAME = "prolog.db";
	private static final int DATABASE_VERSION = 1; /* change it whenever making changes on db scheleton*/
	
	public static final String TABLE_CONTACTS = "contacts";
	public static final String COLUMN_ID = "contactId";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_COMPANY = "company";
	public static final String COLUMN_HOME_PHONE = "home_phone";
	public static final String COLUMN_WORK_PHONE = "work_phone";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_LOCATION = "location";


	
	
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

	
	
	
	
	
	public DBOpenHelper(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(LOGTAG, "testx");
		db.execSQL(TABLE_CREATE);
		Log.i(LOGTAG, "Table has been created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		onCreate(db);

	}

}
