package com.example.prolog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactsDBOpenHelper extends SQLiteOpenHelper {

	private static final String LOGTAG = "EXPLORECA";
	private static final String DATABASE_NAME = "prolog.db";
	private static final int DATABASE_VERSION = 3; /* change it whenever making changes on db scheleton*/
	
	public static final String TABLE_CONTACTS = "contacts";
	
	public static final String COLUMN_ID = "contactId";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_COMPANY = "company";
	public static final String COLUMN_HOME_PHONE = "home_phone";
	public static final String COLUMN_WORK_PHONE = "work_phone";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_LOCATION = "location";
	public static final String COLUMN_PHOTO = "photo";

	public static final String TABLE_INTERACTIONS = "interactions";
	public static final String COLUMN_INTERACTIONS_ID = "interactionId";
	public static final String COLUMN_INTERACTIONS_CONTACT_ID = "contactid";
	public static final String COLUMN_INTERACTIONS_DATE = "date";
	public static final String COLUMN_INTERACTIONS_TEXT = "text";

	public static final String TABLE_GROUPS = "groups";
	public static final String COLUMN_GROUPS_ID = "groupId";
	public static final String COLUMN_GROUPS_NAME = "name";
	
	public static final String TABLE_GROUP_CONTACTS = "group_contacts";
	public static final String COLUMN_GROUP_CONTACT_ID = "contactId";
	public static final String COLUMN_GROUP_GROUP_ID = "groupId";


	
	private static final String TABLE_CREATE =
			"CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + " ( " +
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_NAME + " TEXT, " +
			COLUMN_TITLE + " TEXT, " +
			COLUMN_COMPANY + " TEXT, " +
			COLUMN_HOME_PHONE + " TEXT, " +
			COLUMN_WORK_PHONE + " TEXT, " +
			COLUMN_EMAIL + " TEXT, " +
			COLUMN_LOCATION + " TEXT," +
			COLUMN_PHOTO + " BLOB" +
			")";
	private static final String TABLE_CREATE_INTERACTIONS =
			"CREATE TABLE IF NOT EXISTS " + TABLE_INTERACTIONS + " ( " +
			COLUMN_INTERACTIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_INTERACTIONS_CONTACT_ID + " INTEGER, " +
			COLUMN_INTERACTIONS_DATE + " TEXT, " +
			COLUMN_INTERACTIONS_TEXT + " TEXT" +
			")";
	
	private static final String TABLE_CREATE_GROUPS =
			"CREATE TABLE IF NOT EXISTS " + TABLE_GROUPS + " ( " +
			COLUMN_GROUPS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_GROUPS_NAME + " TEXT " +
			")";
	
	private static final String TABLE_CREATE_GROUP_CONTACTS =
			"CREATE TABLE IF NOT EXISTS " + TABLE_GROUP_CONTACTS + " ( " +
			COLUMN_GROUP_CONTACT_ID + " INTEGER, " +
			COLUMN_GROUP_GROUP_ID + " INTEGER " +
			")";
	
	
	
	
	public ContactsDBOpenHelper(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
		db.execSQL(TABLE_CREATE_INTERACTIONS);
		db.execSQL(TABLE_CREATE_GROUPS);
		db.execSQL(TABLE_CREATE_GROUP_CONTACTS);
		Log.i(LOGTAG, "Tables have been created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERACTIONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP_CONTACTS);
		onCreate(db);

	}

}
