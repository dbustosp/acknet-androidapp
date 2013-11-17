package org.twodee.acknet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class SplashScreen extends Activity {
	 
    String now_playing, earned;
    String token; // This is set in pre request to check if the user is if or not logged
    SharedPreferences SP; // Preferences ==> extract the token if is logged
    Boolean is_logged;
    static final String IP = Connection.getInstance().getIp();
    static final String URL = IP + "/validateToken";
    HttpClient httpclient;
    HttpResponse response; 
    HttpPost httppost;
    List<NameValuePair> nameValuePairs;
    Boolean status;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        System.out.println("Comenzando la aplicacion");
        
        // Set SP
        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        // Ask for my token in my preferences and set a variable
        token = SP.getString("token", "null");
        // If the application has the token ==> is logged
        // else ==> is not logged
        
        System.out.println("Initial token:" + token);
        
        if(token.equals("null")){
        	// Application doesn't have token ==> Login.xml 
        	// Start Activity ==> login view
        	System.out.println("Intent login activity");
        	Intent loginIntent = new Intent(this, MainActivity.class);
			startActivityForResult(loginIntent, 0);
        }
        System.out.println("Pasado el if");
        // Application has token ==> Ask if it is valid by background connection            
        /*
         * Showing splashscreen while making network calls to download necessary
         * data before launching the app Will use AsyncTask to make http call
         */
        new PrefetchData().execute();
    }
 
    /*
     * Async Task to make http call
     */
    private class PrefetchData extends AsyncTask<Void, Void, Void> {
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Before making http calls  
            System.out.println("Init -- pre.execute");
            
            // To prepare variables
            httpclient = new DefaultHttpClient(); 
            httppost = new HttpPost(URL);
            response = null;
            
            // To prepare data to send
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("token", token));
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
        	System.out.println("init -- doInBackground");
        	try {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        response = httpclient.execute(httppost);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	System.out.println("end -- doInBackground");
	        return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            
            System.out.println("init -- onPostExecute");
            
            try {
				Scanner in = new Scanner(response.getEntity().getContent());
				in.useDelimiter("\\Z");
				String body = in.next();
		        in.close();
		        JSONObject json = new JSONObject(body);
		        status = json.getBoolean("success");
		        
		        // If status == true ==> logged true
		        // else ==> User has to login
		        if(status){
		        	// menu or timeline
		        	Intent intent = new Intent(getApplicationContext(), Timeline.class);
					startActivityForResult(intent, 0);
		        }else{
		        	// login
		        	System.out.println("Intent");
		        	Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
					startActivityForResult(intent, 0);
		        }
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            System.out.println("end -- onPostExecute");
            // close this activity
            finish();
        }
    }
}