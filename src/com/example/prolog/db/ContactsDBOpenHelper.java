package com.example.prolog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactsDBOpenHelper extends SQLiteOpenHelper {

	private static final String LOGTAG = ContactsDBOpenHelper.class.getSimpleName();
	private static final String DATABASE_NAME = "prolog.db";
	private static final int DATABASE_VERSION = 9; /* change it whenever making changes on db scheleton*/
	
	
	
    /*
     * Note: custom_fields are instance specific instead of table specific. 
     *       This means creating a new custom_field for a contact will not automatically make this field
     *       available for other contacts. For instance, adding customer field "office_phone1" to Jack 
     *       will not make this field "office_phone1" available for Manuel.
     *       
     */
	
	public static final String TABLE_CONTACTS = "contacts";
	
	public static final String COLUMN_ID = "contactId";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_COMPANY = "company";
	public static final String COLUMN_HOME_PHONE = "home_phone";
	public static final String COLUMN_WORK_PHONE = "work_phone";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_CUSTOM_FIELDS = "custom_fields";
	public static final String COLUMN_PHOTO = "photo";

	public static final String TABLE_INTERACTIONS = "interactions";
	public static final String COLUMN_INTERACTIONS_ID = "interactionId";
	public static final String COLUMN_INTERACTIONS_DATE = "date";
	public static final String COLUMN_INTERACTIONS_TEXT = "text";
	public static final String COLUMN_INTERACTIONS_LOCATION = "location";
	public static final String COLUMN_INTERACTIONS_EVENT = "event";
	public static final String COLUMN_INTERACTIONS_TYPE= "type";
	public static final String COLUMN_INTERACTIONS_FOLLOWUP = "followUp";


	public static final String TABLE_GROUPS = "groups";
	public static final String COLUMN_GROUPS_ID = "groupId";
	public static final String COLUMN_GROUPS_NAME = "name";
	
	public static final String TABLE_GROUP_CONTACTS = "group_contacts";
	public static final String COLUMN_GROUP_CONTACTS_CONTACT_ID = "contactId";
	public static final String COLUMN_GROUP_CONTACTS_GROUP_ID = "groupId";
	
	public static final String TABLE_INTERACTION_CONTACTS = "interaction_contacts";
	public static final String COLUMN_INTERACTION_CONTACTS_CONTACT_ID = "contactId";
	public static final String COLUMN_INTERACTION_CONTACTS_INTERACTION_ID = "interactionId";

	public static final String TABLE_FOLLOW_UPS = "followUps";
	public static final String COLUMN_FOLLOW_UPS_ID = "followUpId";
	public static final String COLUMN_FOLLOW_UPS_CONTACT_ID = "contactId";
	public static final String COLUMN_FOLLOW_UPS_TITLE = "title";
	public static final String COLUMN_FOLLOW_UPS_NOTES = "notes";
	public static final String COLUMN_FOLLOW_UPS_DATE = "date";
	
	

	
	
	private static final String TABLE_CREATE =
			"CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + " ( " +
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_NAME + " TEXT, " +
			COLUMN_TITLE + " TEXT, " +
			COLUMN_COMPANY + " TEXT, " +
			COLUMN_HOME_PHONE + " TEXT, " +
			COLUMN_WORK_PHONE + " TEXT, " +
			COLUMN_EMAIL + " TEXT, " +
			COLUMN_LOCATION + " TEXT, " +
			COLUMN_CUSTOM_FIELDS + " BLOB, " +      // added by Jack
			COLUMN_PHOTO + " BLOB" +
			")";
	private static final String TABLE_CREATE_INTERACTIONS =
			"CREATE TABLE IF NOT EXISTS " + TABLE_INTERACTIONS + " ( " +
			COLUMN_INTERACTIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_INTERACTIONS_DATE + " TEXT, " +
			COLUMN_INTERACTIONS_LOCATION + " TEXT, " +
			COLUMN_INTERACTIONS_EVENT + " TEXT, " +
			COLUMN_INTERACTIONS_TYPE + " TEXT, " +
			COLUMN_INTERACTIONS_FOLLOWUP + " INTEGER, " +
			COLUMN_INTERACTIONS_TEXT + " TEXT" +
			")";
	
	private static final String TABLE_CREATE_GROUPS =
			"CREATE TABLE IF NOT EXISTS " + TABLE_GROUPS + " ( " +
			COLUMN_GROUPS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_GROUPS_NAME + " TEXT " +
			")";
	
	private static final String TABLE_CREATE_GROUP_CONTACTS =
			"CREATE TABLE IF NOT EXISTS " + TABLE_GROUP_CONTACTS + " ( " +
			COLUMN_GROUP_CONTACTS_CONTACT_ID + " INTEGER, " +
			COLUMN_GROUP_CONTACTS_GROUP_ID + " INTEGER " +
			")";
	
	
	private static final String TABLE_CREATE_INTERACTION_CONTACTS =
			"CREATE TABLE IF NOT EXISTS " + TABLE_INTERACTION_CONTACTS + " ( " +
			COLUMN_INTERACTION_CONTACTS_CONTACT_ID + " INTEGER, " +
			COLUMN_INTERACTION_CONTACTS_INTERACTION_ID + " INTEGER " +
			")";
	
	private static final String TABLE_CREATE_FOLLOW_UPS =
			"CREATE TABLE IF NOT EXISTS " + TABLE_FOLLOW_UPS + " ( " +
			COLUMN_FOLLOW_UPS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_FOLLOW_UPS_CONTACT_ID + " INTEGER, " +
			COLUMN_FOLLOW_UPS_TITLE + " TEXT, " +
			COLUMN_FOLLOW_UPS_DATE + " TEXT, " + 
			COLUMN_FOLLOW_UPS_NOTES + " TEXT " +
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
		db.execSQL(TABLE_CREATE_INTERACTION_CONTACTS);
		db.execSQL(TABLE_CREATE_FOLLOW_UPS);

		Log.i(LOGTAG, "Tables have been created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERACTIONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP_CONTACTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERACTION_CONTACTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLLOW_UPS);

		onCreate(db);

	}

}
