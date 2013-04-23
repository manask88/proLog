package com.example.prolog;

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
	private void showNotification() {


		
		
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

		public Notifier(Context context) {
this.context=context;
			
		}

		public void run() {

		
		
		while (true)
		{
			
			
			
			Intent intent = new Intent(context, MainActivity.class);
			PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

			// Build notification
			// Actions are just fake
			Notification noti = new Notification.Builder(context)
			        .setContentTitle("New mail from " + "test@gmail.com")
			        .setContentText("Subject")
			        .setSmallIcon(R.drawable.face)
			        .setContentIntent(pIntent)
			        .addAction(R.drawable.face, "Call", pIntent)
			        .addAction(R.drawable.face, "More", pIntent)
			        .addAction(R.drawable.face, "And more", pIntent).build();
			    
			  
			NotificationManager notificationManager = 
			  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

			// Hide the notification after its selected
			noti.flags |= Notification.FLAG_AUTO_CANCEL;

			notificationManager.notify(0, noti); 
			
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
