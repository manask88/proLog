package com.example.prolog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;
import com.example.prolog.model.TypeValue;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientException;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Connections;
import com.google.code.linkedinapi.schema.Person;

import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.Profile;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

import android.view.View;
import android.widget.Button;

import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SyncActivity extends Activity {

	public static final String CONSUMER_KEY = "l6cisqdmlmmb";
	public static final String CONSUMER_SECRET = "zWq2Ilqx117tdSXd";
	// defines permissions to linkedin API r_fullprofile is for own profile
	private String scopeParams = "r_network";
	public static final String OAUTH_PREF = "LIKEDIN_OAUTH";
	public static final String PREF_TOKEN = "token";
	public static final String PREF_TOKENSECRET = "tokenSecret";
	public static final String PREF_REQTOKENSECRET = "requestTokenSecret";

	public static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-linkedin";
	public static final String OAUTH_CALLBACK_HOST = "linkedinApiTestCallback";
	public static final String OAUTH_CALLBACK_URL = String.format("%s://%s",
			OAUTH_CALLBACK_SCHEME, OAUTH_CALLBACK_HOST);
	public static final String OAUTH_QUERY_TOKEN = "oauth_token";
	public static final String OAUTH_QUERY_VERIFIER = "oauth_verifier";
	public static final String OAUTH_QUERY_PROBLEM = "oauth_problem";

	final LinkedInOAuthService oAuthService = LinkedInOAuthServiceFactory
			.getInstance().createLinkedInOAuthService(CONSUMER_KEY,
					CONSUMER_SECRET, scopeParams);
	public static final LinkedInApiClientFactory factory = LinkedInApiClientFactory
			.newInstance(CONSUMER_KEY, CONSUMER_SECRET);
	LinkedInRequestToken liToken;
	public static final String TAG = SyncActivity.class.getSimpleName();

	ArrayList<ExpandListChild> contacts;
	ArrayList<ExpandListChild> contactsLinkedIn = new ArrayList<ExpandListChild>();

	ArrayList<Contact> contactsList = new ArrayList<Contact>();

	private ExpandListAdapter ExpAdapter;
	private ArrayList<ExpandListGroup> ExpListItems;
	private ExpandableListView ExpandList;
	private Button buttonSkip;
	private Button buttonSync;
	Activity activity = this;
	LinkedInAccessToken accessToken;
	
	private Context context = this;
	ContactsDataSource datasource;
	Contact contactTemp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		
		
        startService(new Intent(this,NotificationService.class));

		
		final SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		boolean isFirstTime=prefs.getBoolean("isFirstTime", true);
		
		if (!isFirstTime)
		{
			
			startActivity(new Intent(context, MainActivity.class));
			finish();
			
		}
		else
		{
			Editor edit=prefs.edit();
			edit.putBoolean("isFirstTime", false);
			edit.commit();
			
		}
		setContentView(R.layout.activity_sync);
		datasource = new ContactsDataSource(this);
		datasource.open();

		buttonSkip = (Button) findViewById(R.id.buttonSyncSkip);
		buttonSkip.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(context, MainActivity.class));
			}
		});

		ExpandList = (ExpandableListView) findViewById(R.id.ExpList);

	
		
		// linkedIn begins
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		Log.i(TAG, "1 is checked");

		final SharedPreferences pref = getSharedPreferences(OAUTH_PREF,
				MODE_PRIVATE);
		final String token = pref.getString(PREF_TOKEN, null);
		final String tokenSecret = pref.getString(PREF_TOKENSECRET, null);
		
		
		try {
			if (token == null || tokenSecret == null) {
				authenticationStart();
			} else {
				accessToken = new LinkedInAccessToken(token, tokenSecret);
				// showCurrentUser(accessToken);
				showConnections(accessToken);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
		}
		// linkedIn ends
		 
		 
		
		buttonSync = (Button) findViewById(R.id.buttonSyncSync);
		buttonSync.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			
				new QueryContactInformationAsyncTask(activity)
				.execute(contactsLinkedIn,contacts);
			}
		});
	}

	private void queryDetailsForContactLinkedIn(final String linkedInId,
			final LinkedInAccessToken accessToken) {

		new Thread() {

			@Override
			public void run() {
				Looper.prepare();
				Log.i(TAG, "getContactDetails");
				final LinkedInApiClient client = factory
						.createLinkedInApiClient(accessToken);
				try {

					final Person profile = client.getProfileById(linkedInId);
					// /////////////////////////////////////////////////////////
					// here you can do client API calls ...
					// client.postComment(arg0, arg1);
					// client.updateCurrentStatus(arg0);
					// or any other API call (this sample only check for current
					// user
					// and shows it in TextView)
					// /////////////////////////////////////////////////////////
					runOnUiThread(new Runnable() {// updating UI thread from
													// different thread not a
													// good idea...
						public void run() {
							contactTemp.setName(profile.getFirstName() + " "
									+ profile.getLastName());
						}
					});
					// or use Toast

				} catch (LinkedInApiClientException ex) {
					clearTokens();
					Log.e(TAG, ex.getMessage());

					Toast.makeText(
							getApplicationContext(),
							"Application down due LinkedInApiClientException: "
									+ ex.getMessage()
									+ " Authokens cleared - try run application again.",
							Toast.LENGTH_LONG).show();

					finish();
				}
				Looper.loop();
			}
		}.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
		datasource.open();
		contacts=queryAllRawContacts();
		
		
			Collections.sort(contacts, new ContactsChildCompareByName());
		
		
		ExpListItems = SetStandardGroups();
		ExpAdapter = new ExpandListAdapter(SyncActivity.this, ExpListItems);

		ExpandList.setAdapter(ExpAdapter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");

		// datasource.close();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.i(TAG, "onNewIntent");
		finishAuthenticate(intent.getData());
	}

	/* creates contact in internal DB */
	protected void createContact(Contact contact) {
		contact = datasource.createContact(contact);

	}

	/*
	 * adds contacts to contacts (used to display on expandableList) and
	 * contactsList (used to sync contacts to DB)
	 */

	private ArrayList<ExpandListChild> queryAllRawContacts() {

		ArrayList<ExpandListChild> contacts = new ArrayList<ExpandListChild>();
		String[] mProjection = new String[] { Profile._ID,
				Profile.DISPLAY_NAME_PRIMARY, };

		String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
				+ ("1") + "'";
		Cursor people = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, mProjection, selection,
				null, null);

		while (people.moveToNext()) {
			int nameIndex = people.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			int idIndex = people.getColumnIndex(PhoneLookup._ID);
			String name = people.getString(nameIndex);
			long id = people.getLong(idIndex);
			ExpandListChild ch2_1 = new ExpandListChild();

			ch2_1.setName(name);
			ch2_1.setTag(null);
			// ch2_1.setChecked(false);
			ch2_1.setId(id);
			contacts.add(ch2_1);
			/*
			 * contact = new Contact(); contact.setContactManagerId(contactId);
			 * contactsList.add(contact);
			 */

		}
		return contacts;
		
		
	

	}

	

	public String queryAllPhoneNumbersForContact(long contactId) {
		final String[] projection = new String[] { Phone.NUMBER, Phone.TYPE, };
		String phonenumber = "";
		final Cursor phone = managedQuery(Phone.CONTENT_URI, projection,
				Data.CONTACT_ID + "=?",
				new String[] { String.valueOf(contactId) }, null);
		if (phone.moveToFirst()) {
			final int contactNumberColumnIndex = phone
					.getColumnIndex(Phone.NUMBER);
			final int contactTypeColumnIndex = phone.getColumnIndex(Phone.TYPE);

			while (!phone.isAfterLast()) {
				final String number = phone.getString(contactNumberColumnIndex);
				final int type = phone.getInt(contactTypeColumnIndex);

				phonenumber = number;
				phone.moveToNext();
			}

		}
		phone.close();

		return phonenumber;
	}

	public ArrayList<TypeValue> queryAllEmailsForContact(long contactId) {
		ArrayList<TypeValue> mails = new ArrayList<TypeValue>();

		final String[] projection = new String[] { Email.DATA, // use
																// Email.ADDRESS
																// for API-Level
																// 11+
				Email.TYPE };
		final Cursor email = managedQuery(Email.CONTENT_URI, projection,
				Data.CONTACT_ID + "=?",
				new String[] { String.valueOf(contactId) }, null);

		if (email.moveToFirst()) {
			final int contactEmailColumnIndex = email
					.getColumnIndex(Email.DATA);
			final int contactTypeColumnIndex = email.getColumnIndex(Email.TYPE);

			while (!email.isAfterLast()) {
				final String address = email.getString(contactEmailColumnIndex);
				final int type = email.getInt(contactTypeColumnIndex);
				mails.add(new TypeValue("", address));
				// content.add(new ListViewEntry(address,
				// Email.getTypeLabelResource(type),R.string.type_email));

				email.moveToNext();
			}

		}

		email.close();

		if (mails.size() > 0)
			return mails;
		else
			return null;
	}

	protected Contact queryDetailsForContactEntry(long contactId) {
		Log.i(TAG, "queryDetailsForContactEntry");
		final String[] projection = new String[] { Contacts.DISPLAY_NAME, // the
				// name
				// of
				// the
				// contact
				Contacts.PHOTO_ID /* , */// the id of the column in the data table
										// for the image
		/* Email.DATA */
		};

		final Cursor cursorContact = managedQuery(Contacts.CONTENT_URI,
				projection, Contacts._ID + "=?", // filter entries on the basis
													// of the contact id
				new String[] { String.valueOf(contactId) }, // the parameter to
															// which the contact
															// id column is
															// compared to
				null);

		if (cursorContact.moveToFirst()) {
			String name = cursorContact.getString(cursorContact
					.getColumnIndex(Contacts.DISPLAY_NAME));

			String photoId = cursorContact.getString(cursorContact
					.getColumnIndex(Contacts.PHOTO_ID));

			Contact contact = new Contact();
			contact.setName(name);
			contact.setContactManagerId(contactId);

			if (photoId != null) {
				Log.i(TAG, "photoid found");
				contact.setPhoto(queryContactBitmap(photoId));
			}
			else
			{
				Log.i(TAG, "photoid not found");
				
			}

			cursorContact.close();

			;
			return contact;
		}
		cursorContact.close();
		return null;
	}

	private Bitmap queryContactBitmap(String photoId) {
		final Cursor photo = managedQuery(Data.CONTENT_URI,
				new String[] { Photo.PHOTO }, // column where the blob is stored
				Data._ID + "=?", // select row by id
				new String[] { photoId }, // filter by the given photoId
				null);

		final Bitmap photoBitmap;
		if (photo.moveToFirst()) {
			byte[] photoBlob = photo.getBlob(photo.getColumnIndex(Photo.PHOTO));
			photoBitmap = BitmapFactory.decodeByteArray(photoBlob, 0,
					photoBlob.length);
			Log.i(TAG, "bitmap found");
		} else {
			photoBitmap = null;
			Log.i(TAG, "bitmap not found");

		}
		photo.close();
		return photoBitmap;
	}

	public ArrayList<ExpandListGroup> SetStandardGroups() {
		ArrayList<ExpandListGroup> list = new ArrayList<ExpandListGroup>();
		ArrayList<ExpandListChild> list2 = new ArrayList<ExpandListChild>();
		ExpandListGroup gru1 = new ExpandListGroup();
		gru1.setName("Phone Contacts");
		// TODO
	
		// queryAllRawContacts();
		
		
		
		gru1.setItems(contacts);
		gru1.setChecked(false);
		list2 = new ArrayList<ExpandListChild>();

		ExpandListGroup gru2 = new ExpandListGroup();
		gru2.setName("LinkedIn");
		ExpandListChild ch2_1 = new ExpandListChild();
		ch2_1.setName("Cont1");
		ch2_1.setTag(null);
		list2.add(ch2_1);
		ExpandListChild ch2_2 = new ExpandListChild();
		ch2_2.setName("Cont2");
		ch2_2.setTag(null);
		list2.add(ch2_2);
		ExpandListChild ch2_3 = new ExpandListChild();
		ch2_3.setName("Cont3");
		ch2_3.setTag(null);
		list2.add(ch2_3);
		
		Collections.sort(contactsLinkedIn, new ContactsChildCompareByName());
		gru2.setItems(contactsLinkedIn);
		gru2.setChecked(true);
		// gru2.setItems(list2);
		// gru1.setItems(list2);
		list.add(gru1);
		list.add(gru2);

		return list;
	}

	void authenticationStart() {
		final LinkedInRequestToken liToken = oAuthService
				.getOAuthRequestToken(OAUTH_CALLBACK_URL);
		final String uri = liToken.getAuthorizationUrl();
		getSharedPreferences(OAUTH_PREF, MODE_PRIVATE).edit()
				.putString(PREF_REQTOKENSECRET, liToken.getTokenSecret())
				.commit();
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		startActivity(i);
	}

	void finishAuthenticate(final Uri uri) {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				if (uri != null
						&& uri.getScheme().equals(OAUTH_CALLBACK_SCHEME)) {
					final String problem = uri
							.getQueryParameter(OAUTH_QUERY_PROBLEM);
					if (problem == null) {
						final SharedPreferences pref = getSharedPreferences(
								OAUTH_PREF, MODE_PRIVATE);
						final String request_token_secret = pref.getString(
								PREF_REQTOKENSECRET, null);
						final String query_token = uri
								.getQueryParameter(OAUTH_QUERY_TOKEN);
						final LinkedInRequestToken request_token = new LinkedInRequestToken(
								query_token, request_token_secret);
						final LinkedInAccessToken accessToken = oAuthService
								.getOAuthAccessToken(
										request_token,
										uri.getQueryParameter(OAUTH_QUERY_VERIFIER));
						SharedPreferences.Editor editor = pref.edit();
						editor.putString(PREF_TOKEN, accessToken.getToken());
						editor.putString(PREF_TOKENSECRET,
								accessToken.getTokenSecret());
						editor.remove(PREF_REQTOKENSECRET);
						editor.commit();
						// showCurrentUser(accessToken);
						showConnections(accessToken);

						Log.d(TAG, "finishAuthenticate");

					} else {
						Toast.makeText(
								getApplicationContext(),
								"Application down due OAuth problem: "
										+ problem, Toast.LENGTH_LONG).show();
						finish();
					}
				}
				Looper.loop();
			}
		}.start();
	}// end method

	void clearTokens() {
		getSharedPreferences(OAUTH_PREF, MODE_PRIVATE).edit()
				.remove(PREF_TOKEN).remove(PREF_TOKENSECRET)
				.remove(PREF_REQTOKENSECRET).commit();
	}// end method

	void showCurrentUser(final LinkedInAccessToken accessToken) {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Log.i(TAG, "showCurrentUser");

				final LinkedInApiClient client = factory
						.createLinkedInApiClient(accessToken);
				try {
					final Person p = client.getProfileForCurrentUser();
					// /////////////////////////////////////////////////////////
					// here you can do client API calls ...
					// client.postComment(arg0, arg1);
					// client.updateCurrentStatus(arg0);
					// or any other API call (this sample only check for current
					// user
					// and shows it in TextView)
					// /////////////////////////////////////////////////////////
					runOnUiThread(new Runnable() {// updating UI thread from
													// different thread not a
													// good idea...
						public void run() {
							// tv.setText(p.getLastName() + ", " +
							// p.getFirstName());
						}
					});
					// or use Toast
					Log.d(TAG, "Lastname:: " + p.getLastName()
							+ ", First name: " + p.getFirstName());
					Toast.makeText(
							getApplicationContext(),
							"Lastname:: " + p.getLastName() + ", First name: "
									+ p.getFirstName(), 1).show();
				} catch (LinkedInApiClientException ex) {
					clearTokens();
					Toast.makeText(
							getApplicationContext(),
							"Application down due LinkedInApiClientException: "
									+ ex.getMessage()
									+ " Authokens cleared - try run application again.",
							Toast.LENGTH_LONG).show();
					finish();
				}
				Looper.loop();
			}
		}.start();
	}// end method

	void showConnections(final LinkedInAccessToken accessToken) {
		new Thread() {

			@Override
			public void run() {
				Looper.prepare();
				Log.i(TAG, "showConnections");
				final LinkedInApiClient client = factory
						.createLinkedInApiClient(accessToken);
				try {

					Connections connections = client
							.getConnectionsForCurrentUser();
					// here you can do client API calls ...
					// /////////////////////////////////////////////////////////
					// client.postComment(arg0, arg1);
					// client.updateCurrentStatus(arg0);
					// or any other API call (this sample only check for current
					// user
					// and shows it in TextView)
					// /////////////////////////////////////////////////////////
					runOnUiThread(new Runnable() {// updating UI thread from
													// different thread not a
													// good idea...
						public void run() {
							// tv.setText(p.getLastName() + ", " +
							// p.getFirstName());
						}
					});
					// or use Toast

					Log.i(TAG,
							"Total connections fetched:"
									+ connections.getTotal());
					for (Person person : connections.getPersonList()) {
						/*Log.i(TAG,
								person.getId() + ":" + person.getFirstName()
										+ " " + person.getLastName() + ":"
										+ person.getHeadline());*/

						ExpandListChild childPerson = new ExpandListChild();

						childPerson.setName(person.getFirstName() + " "
								+ person.getLastName());
						childPerson.setTag(null);
						childPerson.setChecked(true);
						childPerson.setPhotoURL(person.getPictureUrl());
						if (person.getHeadline() != null) {
							String[] fields = person.getHeadline()
									.split(" at ");// for english
							if (fields.length < 2)
								fields = person.getHeadline().split(" en ");// for
																			// spanish
							if (fields.length < 2)
								fields = person.getHeadline().split(" na ");// for
																			// portuguese
							if (fields.length > 0)
								childPerson.setTitle(fields[0]);
							if (fields.length > 1)
								childPerson.setCompany(fields[1]);

						}
						contactsLinkedIn.add(childPerson);
					}

				} catch (LinkedInApiClientException ex) {
					clearTokens();
					Log.e(TAG, ex.getMessage());

					Toast.makeText(
							getApplicationContext(),
							"Application down due LinkedInApiClientException: "
									+ ex.getMessage()
									+ " Authokens cleared - try run application again.",
							Toast.LENGTH_LONG).show();

					finish();
				}
				Looper.loop();
				Collections.sort(contactsLinkedIn, new ContactsChildCompareByName());

			}
		}.start();
	}// end method
	
	
	
	
	/*private void queryAllRawContacts() {
	Log.i(TAG, "queryAllRawContacts");

	final String[] projection = new String[] { RawContacts.CONTACT_ID, // the
																		// contact
																		// id
																		// column
			RawContacts.DELETED // column if this contact is deleted
	};

	final Cursor rawContacts = managedQuery(RawContacts.CONTENT_URI, // the
																		// uri
																		// for
																		// raw
																		// contact
																		// provider
			projection, null, // selection = null, retrieve all entries
			null, // not required because selection does not contain
					// parameters
			null); // do not order

	final int contactIdColumnIndex = rawContacts
			.getColumnIndex(RawContacts.CONTACT_ID);
	final int deletedColumnIndex = rawContacts
			.getColumnIndex(RawContacts.DELETED);
	Contact contact;
	int cont = 0;
	if (rawContacts.moveToFirst()) { // move the cursor to the first entry
		while (!rawContacts.isAfterLast()) { // still a valid entry left?
			final int contactId = rawContacts.getInt(contactIdColumnIndex);
			final boolean deleted = (rawContacts.getInt(deletedColumnIndex) == 1);

			if (!deleted) {

				ExpandListChild ch2_1 = new ExpandListChild();
				ch2_1.setName(queryDetailsForContactEntry(contactId)
						.getName());
				ch2_1.setTag(null);
				contacts.add(ch2_1);
				contact = new Contact();
				contact.setContactManagerId(contactId);
				contactsList.add(contact);
			}
			rawContacts.moveToNext(); // move to the next entry
			if (cont == 100)
				break; // hardcoded to get out of this loop if there are
						// many contacts
			cont++;
		}
	}

	rawContacts.close();
}*/
}
