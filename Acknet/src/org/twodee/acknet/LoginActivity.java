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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.cs491.acknet.R;

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
	
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_login);
	    
	    // Button login
	    final Button btonLogin = (Button) findViewById(R.id.btnLogin);
	    
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
					
					FireMissilesDialogFragment dialog = new FireMissilesDialogFragment();
					
					dialog.show(getFragmentManager(), "missiles");
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
					Intent myIntent = new Intent(getApplicationContext(), AndroidDashboardDesignActivity.class);
					startActivityForResult(myIntent, 0);

		        }else{
		        	// Server returned => False
		        	System.out.println("onPostExecute -  status => False");
		        	FireMissilesDialogFragment dialogo = new FireMissilesDialogFragment();
					dialogo.show(getFragmentManager(), "missiles");
		        }
		        
			}
			
		}.execute();	
	}
}

