package com.example.prolog;

import java.io.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.EnumSet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;

import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientException;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.schema.Person;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class QueryContactInformationAsyncTask extends
		AsyncTask<Object, Contact, Contact> {

	private Context context;
	private Activity activity;
	private TextView textViewInfo;
	private String out;
	private String imageURL,id;
	private String accessToken;
	private static final String TAG = QueryContactInformationAsyncTask.class
			.getSimpleName();
	private ContactsDataSource datasource;

	/**
	 * @param context
	 * 
	 */
	public QueryContactInformationAsyncTask(Context context) {
		this.context = context;
		activity = (Activity) context;

	}

	@Override
	protected Contact doInBackground(Object... objs) {
		Log.i(TAG, "getContactDetails");

		Contact contact = null;

		final LinkedInApiClient client = SyncActivity.factory
				.createLinkedInApiClient((LinkedInAccessToken) objs[1]);
		try {
			final Person profile = client.getProfileById((String) objs[0]);
			final EnumSet<ProfileField> ProfileParameters = EnumSet.allOf(ProfileField.class);
			accessToken=((LinkedInAccessToken) objs[1]).getToken();
			id=((String) objs[0]);
			client.getProfileById((String) objs[0], ProfileParameters);
			contact = new Contact();

			contact.setName(profile.getFirstName() + " "
					+ profile.getLastName());

			imageURL = profile.getPictureUrl();
			Log.i(TAG, "imgURL raw"+imageURL);
			Log.i(TAG, "profile parameters"+ProfileParameters);
			String[] fields = profile.getHeadline().split("at");
			contact.setTitle(fields[0]);
			contact.setCompany(fields[1]);
		} catch (LinkedInApiClientException ex) {
			Log.e(TAG, ex.getMessage());
		}

		Log.i(TAG, "done working on brackground");
		return contact;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Contact contact) {
		Log.e(TAG, "onPostExecute");
		if (contact != null) {
			try {
				if (id != null) {

					Log.i(TAG, "imgURL is"+imageURL);
Log.i(TAG,"url"+"http://api.linkedin.com/v1/people/"+id+"/picture-url?oauth2_access_token="+accessToken);
					contact.setPhoto(getImageFromURL("http://api.linkedin.com/v1/people/"+id+"/picture-url?oauth2_access_token=access_token="+accessToken));
				}
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());

			}
			datasource = new ContactsDataSource(context);
			datasource.open();
			datasource.createContact(contact);
			datasource.close();
			Log.i(TAG, "result is not null");

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		// statusText.setText("Opening a server socket");
		Log.i(TAG, "onPreExecute");
	}

	public static byte[] urlToImageBLOB(String url) throws IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpEntity entity = null;
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpGet);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			entity = response.getEntity();
		}
		else
		Log.e(TAG, "Response error"+response.getStatusLine().getStatusCode());
		return EntityUtils.toByteArray(entity);
	}

	public static Bitmap getImageFromBlob(byte[] blob) {
		return BitmapFactory.decodeByteArray(blob, 0, blob.length);
	}

	public static Bitmap getImageFromURL(String url) throws IOException {
		return getImageFromBlob(urlToImageBLOB(url));
	}
}