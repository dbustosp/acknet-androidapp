package org.twodee.acknet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cs491.acknet.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

@SuppressLint("NewApi")
public class DashboardActivity extends Activity{
	
    SharedPreferences SP;

    // Variables to logout in GCM
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    
    // Project number
    String SENDER_ID = "194384380675";
    		
    static final String TAG = "ACKNET";
    
    Context context;
    GoogleCloudMessaging gcm;
    
    String regid;
    		
    String token;
    String username;
    String device;
    
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
         
        /**
         * Creating all buttons instances
         * */
        // Dashboard News feed button
        Button btn_newsfeed = (Button) findViewById(R.id.btn_news_feed);
         
        // Dashboard Friends button
        Button btn_friends = (Button) findViewById(R.id.btn_friends);
         
        // Dashboard Messages button
        Button btn_notifications = (Button) findViewById(R.id.btn_notifications);
         
        // Dashboard Places button
        Button btn_posts = (Button) findViewById(R.id.btn_posts);
         
        // Dashboard Events button
        Button btn_events = (Button) findViewById(R.id.btn_events);
         
        // Dashboard Photos button
        Button btn_settings = (Button) findViewById(R.id.btn_settings);
         
        /**
         * Handling all button click events
         * */
         
        // Listening to News Feed button click
        btn_newsfeed.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), TimelineActivity.class);
                startActivity(i);
            }
        });
         
       // Listening Friends button click
       btn_friends.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), FriendsActivity.class);
                startActivity(i);
            }
        });
       
       
       
        // Listening Messages button click
        btn_notifications.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(i);
            }
        });
         
        // Listening to Places button click
        btn_posts.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), PostActivity.class);
                startActivity(i);
            }
        });
         
        // Listening to Events button click
        btn_events.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Call Logout function
            	logout();
            	
            	
            }
        });
         
        // Listening to Photos button click
        btn_settings.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), AndroidListViewActivity.class);
                startActivity(i);
            }
        });  
    }
	
	private void logout(){
		new AsyncTask<Void, Void, HttpResponse>() {

			@Override
			protected HttpResponse doInBackground(Void... params) {
				
				SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				token = SP.getString("token", "null");
				username = SP.getString("username", "null");
				
				final SharedPreferences prefs = getGCMPreferences(context);
				device = prefs.getString(PROPERTY_REG_ID, "");
				
				// Prepare the URL
				String address = Connection.getInstance().getIp() + "/session/" + username + "/" + token; 

				try {
					
					URL url = new URL(address);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("DELETE");
					int responseCode = connection.getResponseCode();

				} catch (MalformedURLException e) {
					// SHOW A MESSAGE 
					
					
					e.printStackTrace();
				}catch(IOException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}
			
			@Override
			protected void onPostExecute(HttpResponse response){
				// Unregister GCM
				
				context = getApplicationContext();
				if (checkPlayServices()) {
					gcm = GoogleCloudMessaging.getInstance(DashboardActivity.this);
		            regid = getRegistrationId(context);

		            if (  !(regid.isEmpty())  ) {
		            	System.out.println("Unregistering in background!");
		            	unregisterInDatabase();
		                unregisterInBackground();
		            }
				}else{
					System.out.println(" !!! Check Play Services ");
		        	Log.i(TAG, "No valid Google Play Services APK found.");
				}
				
				SharedPreferences.Editor editor = SP.edit();
				editor.remove(PROPERTY_REG_ID);
				editor.remove("username");
				editor.remove("token");
				editor.remove(PROPERTY_APP_VERSION);
		        editor.commit();
		        System.out.println("Parameters in Shared preference reseted");
				
		        
		        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
		        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities        	
		        startActivity(i);
			}
		}.execute();
	}
	
	private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }
	
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(DashboardActivity.class.getSimpleName(),
        Context.MODE_PRIVATE);
    }
    
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
	
	
    private boolean checkPlayServices() {
    	System.out.println("init CheckPlayServices");
    	int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
        	System.out.println("resultCode != ConnectionResult.SUCCESS");
        	if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
        		System.out.println("GooglePlayServicesUtil");
        		GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
            	System.out.println("Log");
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    
    private void unregisterInBackground() {
    	new AsyncTask<Object, Object, Object>() {	

			@Override
			protected Object doInBackground(Object... params) {
				
				String msg = "";
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(context);
	                }
	                gcm.unregister();
	                removeRegistrationId(context, null);
	            } catch (IOException ex) {
	                msg = "Error :" + ex.getMessage();
	            }
	            return msg;			
			}
            
			@Override
			protected void onPostExecute(Object msg) {
	           System.out.println("Logout succesfull");
	        }
			
    	}.execute();
    }
    
    private void unregisterInDatabase() {
    	new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				
				// Prepare the URL
				String address = Connection.getInstance().getIp() + "/device/" + device;

				try {
					
					URL url = new URL(address);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("DELETE");
					int responseCode = connection.getResponseCode();

				} catch (MalformedURLException e) {
					// SHOW A MESSAGE 
					e.printStackTrace();
				}catch(IOException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Object msg) {
	            System.out.println("onPostExecute -- unregisterInDatabase");
	        }
    		
    	}.execute();
    }
    
	private void removeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
	
}
