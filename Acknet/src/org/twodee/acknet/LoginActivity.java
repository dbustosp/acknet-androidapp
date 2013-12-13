package org.twodee.acknet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cs491.acknet.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

@SuppressLint("NewApi")
public class LoginActivity extends Activity {
	
	 EditText login_username;
	 EditText login_password;
	 String username;
	 String password;
	 String token;
	 Boolean status;
	 public static final String PREFS_NAME = "preferences.xml";
	 SharedPreferences SP;
	 final static String IP = Connection.getInstance().getIp();
	 final static String URL = IP + "/session";
	 HttpPost httppost;
	 HttpClient httpclient;
	 HttpResponse response;
	 List<NameValuePair> nameValuePairs;
	 Context context;
	 String regid;
	 
	 String SENDER_ID = "194384380675";
	 GoogleCloudMessaging gcm;
	 AtomicInteger msgId = new AtomicInteger();
	 
	 private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	 public static final String PROPERTY_REG_ID = "registration_id";
	 private static final String PROPERTY_APP_VERSION = "appVersion";
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_login);
	    
	    // Button login
	    final Button btonLogin = (Button) findViewById(R.id.btnLogin);
	    
	    //TextView
	    final TextView register = (TextView) findViewById(R.id.register);
	    
	    register.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				Intent loginIntent = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivityForResult(loginIntent, 0);
			
			}
	    });
	    
	    
	    
	    
	    
	    login_username = (EditText)findViewById(R.id.username);
	    login_password = (EditText)findViewById(R.id.password);
	    	 
	    //final Button signupButtonGo = (Button) findViewById(R.id.signup_go);
	    //login_username = (EditText)findViewById(R.id.username);
	    //login_password = (EditText)findViewById(R.id.password);
	    btonLogin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				System.out.println("Click Login");
				
				username = login_username.getText().toString();
			    password = login_password.getText().toString();
				
			    System.out.println("prueba: " + login_username.getText().toString());
				
				// Check if email and password are not null
				if(username.equals("") || password.equals("")){
					System.out.println("||");
					System.out.println("username" +  username);
					
					//FireMissilesDialogFragment dialog = new FireMissilesDialogFragment();
					//dialog.show(getFragmentManager(), "missiles");
				}else{
					// Check if is a real email
					System.out.println("else ||");
					// Make request background
					httpclient = new DefaultHttpClient();
					httppost = new HttpPost(URL);
				    response = null;
				    nameValuePairs = new ArrayList<NameValuePair>(2);
			        nameValuePairs.add(new BasicNameValuePair("username",username));
			        nameValuePairs.add(new BasicNameValuePair("password",password));
			        makeConnection();
			        init_register_gcm();
			        
				}
			}
	    });
	}
	
	
	private void makeConnection() {		
		new AsyncTask<Void, Void, HttpResponse>() {
			
			@Override
			protected HttpResponse doInBackground(Void... params) {
				
				System.out.println("Init -- doInBackground -- send for login ");
				try{
			    
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			        response = httpclient.execute(httppost);
			        Scanner in = new Scanner(response.getEntity().getContent());
				    in.useDelimiter("\\Z");
				    String body = in.next();
				    in.close();       			        
				    JSONObject json = new JSONObject(body);
			        status = json.getBoolean("success");
			        token = json.getString("token");
				
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
				return response;
			}
			
			
			@Override
			protected void onPostExecute(HttpResponse response){
				
				System.out.println("onPostExecute -- Login ");
				
		        if(status){
		        	// Server returned => True
		        	System.out.println("onPostExecute -- Login -- status ==> True");
		        	
		        	// Change the token value and redirect to menu
		        	SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		        	
		        	Editor edit = SP.edit();
		        	
		        	edit.putString("token", token);
		        	edit.putString("username", username);
		        	
		            edit.commit();
		            
		            String new_token = SP.getString("token", "null");
		            
		            System.out.println("new_token: " + new_token);
		        	
		        	//System.out.println("End shared preferences");
					Intent myIntent = new Intent(getApplicationContext(), DashboardActivity.class);
					startActivityForResult(myIntent, 0);

		        }else{
		        	// Server returned => False
		        	System.out.println("onPostExecute -  status => False");
		        	//FireMissilesDialogFragment dialogo = new FireMissilesDialogFragment();
					//dialogo.show(getFragmentManager(), "missiles");
		        }   
			}
		}.execute();	
	}
	
	public void init_register_gcm(){
			
			System.out.println("Registrando a GCM");
			
			//Instructions for registration in Google Cloud Messaging
			context = getApplicationContext();
			
			if (checkPlayServices()) {
				
				regid = getRegistrationId(context);
				
				if (regid.isEmpty()) {
	            	System.out.println("regid is Empty");
	                registerInBackground();
	            }else{
	            	System.out.println("regid is not Empty: " + regid);
	            }						
			}else{
				System.out.println(" !!! Check Play Services ");
	        	System.out.println("No valid Google Play Services APK found.");
			}
			
			System.out.println("Terminando a GCM");
	}
	
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
	            System.out.println("onPostExecute Register GCM in Background");
	        }
        }.execute();
    }
    
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        System.out.println("Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    
    private void sendRegistrationIdToBackend(String regid) {
        // Your implementation here.
    	String URL = Connection.getInstance().getIp() + "/registerDevice";
    	HttpResponse response = null;
    	JSONObject json = new JSONObject();    	
    	SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	String username = SP.getString("token", this.username);

    	try {
			json.put("regid", regid);
			json.put("username", this.username);
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
                System.out.println("This device is not supported.");
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
        	System.out.println("Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            System.out.println("App version changed.");
            return "";
        }
        return registrationId;
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
    
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(DashboardActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
	
	
}

