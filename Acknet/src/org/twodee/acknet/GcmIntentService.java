package org.twodee.acknet;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
	
	public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    
    public String URL = Connection.getInstance().getIp() + "/notification";

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
	    	String message =  "User " + username + " has been registered in ACKNET.";
    		
	    	handle_post_request(message);
    		
    		System.out.println(message); 
    		sendNotification(username);
		}	
	}
	
	public void handle_post_request (final String message){
			
			new AsyncTask<Void, Void, HttpResponse>() {

				@Override
				protected HttpResponse doInBackground(Void... params) {
					
					SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				    String user = SP.getString("username", "");
				    String token = SP.getString("token", "");
				    
				    JSONObject response_json = new JSONObject();
				    try {
						response_json.put("message", message);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					response_json = Connection.getInstance().make_post_request(user, token, URL,response_json);
					
					
					
					return null;
				}
				
				
				
			}.execute();
	}
	

	
	
	
	// Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DashboardActivity.class), 0);

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
