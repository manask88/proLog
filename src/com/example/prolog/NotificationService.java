package com.example.prolog;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.prolog.db.ContactsDataSource;
import com.example.prolog.model.Contact;
import com.example.prolog.model.FollowUp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;



public class NotificationService extends Service{

	

	private  Notifier notifier;
	static final String TAG=NotificationService.class.getSimpleName();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		this.notifier=new Notifier(this);
		this.notifier.start();
		Log.d(TAG,"on Create");
		Toast.makeText(this,"Service created at " + "asdsd", Toast.LENGTH_LONG).show();
		
		

		
		
		
		
		
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		
		Log.d(TAG,"on StartCommand");

		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		this.notifier.interrupt();
		this.notifier=null;
		Log.d(TAG,"on Destroy");
		super.onDestroy();
	}
	private class Notifier extends Thread {

		Context context;
		private ContactsDataSource datasource;
		private ArrayList<FollowUp> followUps;
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		
		
		public Notifier(Context context) {
			this.context=context;
			datasource=new ContactsDataSource(context);	
		}

		public void run() {

		
		
		while (true)
		{
			
			datasource.open();
			
			
			followUps=datasource.findFollowUpsbyDate(month + "/" + day + "/" + year);
			
			
			for (FollowUp followUp:followUps)
			{
			
			Contact contact=datasource.findContactbyId(followUp.getContactId());
				
			Intent intent = new Intent(context, MyTabActivity.class);
			intent.putExtra(Commons.TAB_ID, Commons.TAB_ID_FOLLOWUP_DETAIL);
			intent.putExtra(Commons.FOLLOWUP_ID, followUp.getId());
			intent.putExtra(Commons.CONTACT_ID, contact.getId());
			//intent.putExtra(Commons.FOLLOWUP_ID, followUp.getId());
			PendingIntent pIntent = PendingIntent.getActivity(context, Long.valueOf(followUp.getId()).intValue(), intent, 0);

			// Build notification
			// Actions are just fake
			/*Notification noti = new Notification.Builder(context)
			        .setContentTitle("Follow Up  id:"+followUp.getId()+" -" +  "today"+followUp.getTitle())
			        .setContentText("Subject")
			        .setSmallIcon(R.drawable.face)
			        .setContentIntent(pIntent)
			        .addAction(R.drawable.face, "Call", pIntent)
			        .addAction(R.drawable.face, "More", pIntent)
			        .addAction(R.drawable.face, "And more", pIntent).build();*/
			String contentTitle="Follow Up due: "+followUp.getTitle();
			String contentText= "Contact:"+contact.getName();
	        Notification notifyDetails = new Notification(R.drawable.face, "New Alert!", System.currentTimeMillis());
	        notifyDetails.setLatestEventInfo(getApplicationContext(), contentTitle, contentText, pIntent);

			
			NotificationManager notificationManager = 
			  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

			// Hide the notification after its selected
			notifyDetails.flags |= Notification.FLAG_AUTO_CANCEL;

			notificationManager.notify(Long.valueOf(followUp.getId()).intValue(), notifyDetails); 
			
			
			
			}
			
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
		}	
		
		}
	

	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
