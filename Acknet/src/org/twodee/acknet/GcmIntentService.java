package org.twodee.acknet;

import org.apache.http.HttpResponse;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cs491.acknet.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
	
	public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    
    
	@Override
	protected void onHandleIntent(Intent intent) {
		System.out.println("Init -- onHandleIntent");
		
		Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);		
		
		
		System.out.println("messageType: " + messageType);
		
			
        if (!extras.isEmpty()) {
        	String action = intent.getAction();
        	if (action.equals("com.google.android.c2dm.intent.REGISTRATION")) {
                handleRegistration(intent);
            } else if (action.equals("com.google.android.c2dm.intent.RECEIVE")) {
                handleMessage(intent, extras);
                
            }    	
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
	}
	
	private void handleRegistration(Intent intent){
		System.out.println("handleRegistration");
	}
	
	private void handleMessage(Intent intent, Bundle extras){
		System.out.println("handleMessage");
		String type = extras.getString("type");
		
		if(type.equals("0")){
			String username = extras.getString("username");
			System.out.println("type: " + type + "   username: " + username);  
    		sendNotification(username);
		}	
	}
	
	// Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DemoActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        //.setSmallIcon(R.drawable.ic_stat_gcm)
        .setContentTitle("GCM Notification")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

	
	
	
}
