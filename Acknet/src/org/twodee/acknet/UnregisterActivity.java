package org.twodee.acknet;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.cs491.acknet.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class UnregisterActivity extends Activity{
	
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "194384380675";
    
    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCMDemo";
    
    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;

    String regid;
    
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.gcm);
        mDisplay = (TextView) findViewById(R.id.display);
        
        System.out.println("mDisplay = (TextView) findViewById(R.id.display);");
        
        context = getApplicationContext();
        
        System.out.println("getApplicationContext");
        
        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
        	
        	System.out.println("CheckPlayServices");
        	
        	gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if ( !(regid.isEmpty()) ) {
            	System.out.println("Unregistering in background!");
                unregisterInBackground();
            }
            
        } else {
        	System.out.println(" !!! Check Play Services ");
        	Log.i(TAG, "No valid Google Play Services APK found.");
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
    
    @SuppressLint("NewApi")
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
        return getSharedPreferences(DemoActivity.class.getSimpleName(),
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
    
	private void unregisterInBackground() {
	        
	    	new AsyncTask<Object, Object, Object>() {
	
				@Override
				protected String doInBackground(Object... params) {
					String msg = "";
		            try {
		                if (gcm == null) {
		                    gcm = GoogleCloudMessaging.getInstance(context);
		                }
		                gcm.unregister();
	
		                
		                //sendRegistrationIdToBackend(regid);
	
		                // For this demo: we don't need to send it because the device
		                // will send upstream messages to a server that echo back the
		                // message using the 'from' address in the message.
	
		                // Persist the regID - no need to register again.
		                   removeRegistrationId(context, null);
		            } catch (IOException ex) {
		                msg = "Error :" + ex.getMessage();
		                // If there is an error, don't just keep trying to register.
		                // Require the user to click a button again, or perform
		                // exponential back-off.
		            }
		            return msg;			
				}
	            
				@Override
				protected void onPostExecute(Object msg) {
		            mDisplay.append(msg + "\n");
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
