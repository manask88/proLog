package com.example.prolog;

import java.io.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
		AsyncTask<ArrayList<ExpandListChild>, Contact, Contact> {

	private Context context;
	private Activity activity;
	private TextView textViewInfo;
	private String out;
	private String imageURL, id;
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
	protected Contact doInBackground(
			ArrayList<ExpandListChild>... contactsLinkedIn) {
		Log.i(TAG, "getContactDetails");

		Contact contact;
		datasource = new ContactsDataSource(context);
		datasource.open();
		for (ExpandListChild contactChild : contactsLinkedIn[0]) {
			if (contactChild.isChecked()) {
				Log.i(TAG, "trying to get Linkedin contact with id: "
						+ contactChild.getName());

				contact = new Contact();
				if (contactChild.getPhotoURL() != null)
					try {
						contact.setPhoto(getImageFromURL(contactChild
								.getPhotoURL()));
					} catch (IOException e) {
						Log.e(TAG, e.getMessage());
					}
				contact.setCompany(contactChild.getCompany());
				contact.setTitle(contactChild.getTitle());
				contact.setName(contactChild.getName());
				datasource.createContact(contact);

			}
		}
		datasource.close();
		Log.i(TAG, "done working on brackground");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Contact contact) {
		Log.e(TAG, "onPostExecute");

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
		} else
			Log.e(TAG, "Response error"
					+ response.getStatusLine().getStatusCode());
		return EntityUtils.toByteArray(entity);
	}

	public static Bitmap getImageFromBlob(byte[] blob) {
		return BitmapFactory.decodeByteArray(blob, 0, blob.length);
	}

	public static Bitmap getImageFromURL(String url) throws IOException {
		return getImageFromBlob(urlToImageBLOB(url));
	}
}