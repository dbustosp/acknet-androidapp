package org.twodee.acknet;

import java.io.IOException;
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
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
	 EditText login_username;
	 EditText login_password;
	 String token;
	 Boolean status;
	 public static final String PREFS_NAME = "preferences.xml";
	 SharedPreferences SP;
	
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_login);
	    
	    Log.i("info", "holaaaaaa");
	
	    final Button signupButtonGo = (Button) findViewById(R.id.signup_go);
	    login_username = (EditText)findViewById(R.id.username);
	    login_password = (EditText)findViewById(R.id.password);
	    
	    	    
	    signupButtonGo.setOnClickListener(new OnClickListener(){
	    
	    	
	    	
			@Override
			public void onClick(View arg0) {
				Log.i("info", "Llamando makeConnection");
				
				makeConnection(arg0);
				
				Log.i("info", "Fin makeConnection");
				
			}
	    });
	
	}
	
	private void makeConnection(final View v) {
		
		
		
		
		new AsyncTask<Void, Void, HttpResponse>() {
			
			
			
			@Override
			protected HttpResponse doInBackground(Void... params) {
				
				
				
				System.out.println("HAhaHAHA");
				Log.i("info", "Init doInBackGround ");
				HttpClient httpclient = new DefaultHttpClient();
				String IP = Connection.getInstance().getIp();
				String URL = IP + "/session/" + login_username.getText().toString();
				HttpPost httppost = new HttpPost(URL);
			    HttpResponse response = null;
			   			    
			    try {
			        // Add your data
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			        nameValuePairs.add(new BasicNameValuePair("username", login_username.getText().toString()));
			        nameValuePairs.add(new BasicNameValuePair("password", login_password.getText().toString()));
			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			        Log.i("info", "After httppost.setEntity");
			        // Execute HTTP Post Request
			        response = httpclient.execute(httppost);
			        
			        Scanner in = new Scanner(response.getEntity().getContent());
			        in.useDelimiter("\\Z");
			        
			        String body = in.next();
			        
			        in.close();
			        
			        System.out.println("BODYY: " + body);
			        
			        JSONObject json = new JSONObject(body);
			        
			        System.out.println("JSOOON: " + json);
			        
			        
			        status = json.getBoolean("success");
			        
			        if(status){
			        	
			        	 //SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			        	
			        	String token_aux = json.getString("token");
			        	System.out.println("token_aux = " + token_aux);
			        	token = token_aux;
			        	token_aux = null;
			        	
			        	
			        	
			        	
			            
			            //Toast.makeText(LoginActivity.this,
			            //        "Reverted string sequence of user name.",
			            //        Toast.LENGTH_LONG).show();
			            
			            
			           
			        	//String token_preference = SP.getString("token", "null");
			        	
			        	//System.out.println("Tooooken preferences: " + token_preference);
			        	
			        	
			        }
			        
			        System.out.println("Status: ===> " + status);

			    } catch (ClientProtocolException e) {
			        // TODO Auto-generated catch block
			    	Log.i("info", "Protocol Exception");
			    } catch (IOException e) {
			        // TODO Auto-generated catch block
			    	Log.i("info", "IOEXCEPTION");
			    	System.out.println("Got an IOException: " + e.getMessage());
			    } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return response;
			}
			
			
			@Override
			protected void onPostExecute(HttpResponse response){
				//use it wherever you want
				Log.i("info", "onPostExecute");
				System.out.println("Status: " + status);
				
		        if(status){
		        	// Server returned => True
		        	System.out.println("Status = True");
		        	System.out.println("Token = " + token);

		        	SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		        	Editor edit = SP.edit();
		        	edit.putString("token", token);
		            edit.commit();
		            
		            
		            // Verify sabe in correct form
		            // Read from preferences.xml
		            //String token_preference = SP.getString("token", "null");
		                	
		        	//System.out.println("TOOOKEN: " + token);
		        	//System.out.println("TOOOKEN_PREFERENCE: " + token_preference);
		  
		        	
		        	//System.out.println("End shared preferences");
					Intent myIntent = new Intent(v.getContext(), Timeline.class);
					startActivityForResult(myIntent, 0);

		        }else{
		        	// Server returned => False
		        	System.out.println("onPostExecute -  status => False");
		        }
		        
			}
			
		}.execute();
	
	
        
		
	}
	
	
	
}
