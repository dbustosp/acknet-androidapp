package org.twodee.acknet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import com.cs491.acknet.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

@SuppressLint("NewApi")
public class DemoActivity extends Activity {
	
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    SharedPreferences SP;
    
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
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.gcm);
        mDisplay = (TextView) findViewById(R.id.display);
        
        
        
        context = getApplicationContext();
        
        System.out.println(" getApplicationContent ");
        
        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
        	System.out.println(" Check Play Services ");
        	gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
            	System.out.println("regid is Empty");
                registerInBackground();
            }else{
            	System.out.println("regid is not Empty: " + regid);
            }
            
        } else {
        	System.out.println(" !!! Check Play Services ");
        	Log.i(TAG, "No valid Google Play Services APK found.");
        }
        
    }
    
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
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
    
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(DemoActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
    
    
    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
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
    
    /**
     * @return Application's version code from the {@code PackageManager}.
     */
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
    
    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        
    	new AsyncTask<Object, Object, Object>() {

			@Override
			protected String doInBackground(Object... params) {
				String msg = "";
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(context);
	                }
	                regid = gcm.register(SENDER_ID);
	                msg = "Device registered, registration ID=" + regid;

	                // You should send the registration ID to your server over HTTP,
	                // so it can use GCM/HTTP or CCS to send messages to your app.
	                // The request to your server should be authenticated if your app
	                // is using accounts.
	                System.out.println("Sending...: " + regid );
	                sendRegistrationIdToBackend(regid);

	                // For this demo: we don't need to send it because the device
	                // will send upstream messages to a server that echo back the
	                // message using the 'from' address in the message.

	                // Persist the regID - no need to register again.
	                storeRegistrationId(context, regid);
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
    
    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend(String regid) {
        // Your implementation here.
    	String URL = Connection.getInstance().getIp() + "/registerDevice";
    	HttpResponse response = null;
    	JSONObject json = new JSONObject();    	
    	SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	String username = SP.getString("token", "username");

    	try {
			json.put("regid", regid);
			json.put("username", "usernameAuxiliar");
			System.out.println("Sending reqid");
			json = makeRequest(URL, json);
			System.out.println("Finish reqid");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	private JSONObject makeRequest(String url, JSONObject json) {
			
			try{
				URI uri = new URI(String.format(url));
				HttpPost post = new HttpPost(uri);
				StringEntity entity = new StringEntity(json.toString());			
				entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			    post.setEntity(entity);
			    HttpClient client = new DefaultHttpClient();
			    HttpResponse response = client.execute(post);
			    
			    Scanner in = new Scanner(response.getEntity().getContent());
			    in.useDelimiter("\\Z");
			    String body = in.next();
			    in.close();    		    
			    return new JSONObject(body);
				
				
			}catch (URISyntaxException e) {
			      e.printStackTrace();
		    } catch (UnsupportedEncodingException e) {
		      e.printStackTrace();
		    } catch (ClientProtocolException e) {
		      e.printStackTrace();
		    } catch (IOException e) {
		      e.printStackTrace();
		    } catch (JSONException e) {
		      e.printStackTrace();
		    }
			
			return null;
	}
    
    
    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    
	

}
