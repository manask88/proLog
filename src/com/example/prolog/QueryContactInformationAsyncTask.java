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
import com.example.prolog.model.TypeValue;

import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientException;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.schema.Person;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class QueryContactInformationAsyncTask extends
		AsyncTask<ArrayList<ExpandListChild>, Integer, Contact> {

	private Context context;
	private Activity activity;
	private TextView textViewInfo;
	private String out;
	private String imageURL, id;
	private String accessToken;
	private int progressInt, total;
	private static final String TAG = QueryContactInformationAsyncTask.class
			.getSimpleName();
	private ContactsDataSource datasource;
	ProgressDialog progressDialog;

	/**
	 * @param context
	 * 
	 */
	public QueryContactInformationAsyncTask(Context context) {
		this.context = context;
		activity = (Activity) context;

	}

	@Override
	protected void onPreExecute() {
		// statusText.setText("Opening a server socket");
		Log.i(TAG, "onPreExecute");
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("Synchronizing");
		progressDialog.setMessage("pelase wait");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setIndeterminate(false);

		progressDialog.show();
		progressInt = 0;
	}

	@Override
	protected Contact doInBackground(ArrayList<ExpandListChild>... contacts) {
		Log.i(TAG, "getContactDetails");

		Contact contact;
		datasource = new ContactsDataSource(context);
		datasource.open();
		total = contacts[0].size() + contacts[1].size();

		/* LinkedIn begins */
		for (ExpandListChild contactChild : contacts[0]) {
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
				publishProgress(progressInt++);
			}

		}
		/* LinkedIn ends */

		/* phone contacts begins */
		for (ExpandListChild contactChild : contacts[1]) {
			if (contactChild.isChecked()) {

				contact = ((SyncActivity) activity)
						.queryDetailsForContactEntry(contactChild.getId());
				contact.setHome_phone(((SyncActivity) activity)
						.queryAllPhoneNumbersForContact(contactChild.getId()));
				ArrayList<TypeValue> mails = ((SyncActivity) activity)
						.queryAllEmailsForContact(contactChild.getId());
				if (mails != null)
					contact.setEmail(mails.get(0).getValue());
				contact.setWork_phone("another test");
				datasource.createContact(contact);
				publishProgress(progressInt++);
			}
		}

		/* phone contacts ends */

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
		Log.i(TAG, "onPostExecute");
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		activity.finish();
		context.startActivity(new Intent(context, MainActivity.class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */

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

	protected void onProgressUpdate(Integer... progress) {
		int percentage = (100 * progress[0]) / total;
		progressDialog.setProgress(percentage);
		progressDialog.show();
	}

	public static Bitmap getImageFromURL(String url) throws IOException {
		return Commons.getImageFromBlob(urlToImageBLOB(url));
	}

}