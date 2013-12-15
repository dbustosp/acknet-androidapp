package org.twodee.acknet;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.cs491.acknet.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class RegisterActivity extends Activity{
	String username;
	String email;
	String password;
	
	List<NameValuePair> nameValuePairs;
	HttpPost httppost;
	HttpResponse response;
	HttpClient httpclient;
	Boolean status;
	Context context;
	String regid;
	SharedPreferences SP;
	
	SharedPreferences prefs;
	
	public static final String PROPERTY_REG_ID = "registration_id";
    
    String SENDER_ID = "194384380675";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    
    JSONObject response_json;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		final Button loginButton = (Button) findViewById(R.id.loginButton);
		
		// Register Button
		final Button registerButton = (Button) findViewById(R.id.registerButton);

		final EditText register_username = (EditText)findViewById(R.id.editText1);
		final EditText register_email = (EditText)findViewById(R.id.editText2);
		final EditText register_password = (EditText)findViewById(R.id.editText3);

		gcm = GoogleCloudMessaging.getInstance(this);

		loginButton.setOnClickListener(new OnClickListener(){

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {		
				
				Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
	        	startActivityForResult(myIntent, 0);		
			}
		});
		
		

		// Listener register Button
		registerButton.setOnClickListener(new OnClickListener(){

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				username = register_username.getText().toString();
				email = register_email.getText().toString();
				password = register_password.getText().toString();
				
				if(username.equals("")){
					// Username null
					AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
					String miss_username = getString(R.string.you_need_username_to_register);
					builder.setTitle(miss_username)
			        .setMessage(miss_username)
			        .setCancelable(false)
			        .setNegativeButton("OK",new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			            }
			        });
			        AlertDialog alert = builder.create();
			        alert.show();					
				}else if(email.equals("")){
					// Email null
					AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
			        builder.setTitle("Email is required")
			        .setMessage("You need an email to register. Thanks. ")
			        .setCancelable(false)
			        .setNegativeButton("OK",new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			            }
			        });
			        AlertDialog alert = builder.create();
			        alert.show();
				}else if(password.equals("")){
					// Password null
					AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
			        builder.setTitle("Password is required")
			        .setMessage("You need a password to register. Thanks. ")
			        .setCancelable(false)
			        .setNegativeButton("OK",new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			            }
			        });
			        AlertDialog alert = builder.create();
			        alert.show();	
				}else{
					// All OK
					// Verify if is an email or not
	
					// Method to send to database for registration
					// Method to send to database for registration
					JSONObject json_put = new JSONObject();
					try {
						json_put.put("username", username);
						json_put.put("email", email);
						json_put.put("password", password);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// set user null because we will not need it
					// set token null because we will not used it
					String user = "user";
					String token = "toke";
					
					// Set url 
					String URL = Connection.getInstance().getIp() + "/user";
			        // Call method registration with information
			        handle_post_request(user, token, json_put, URL);	
				}	
			}
		});
	}
	
	public void handle_post_request(final String user, final String token, final JSONObject json_put, final String URL ) {
		
		new AsyncTask<Void, Void, HttpResponse>() {

			@Override
			protected HttpResponse doInBackground(Void... params) {

				response_json = new JSONObject();
				response_json = Connection.getInstance().make_post_request(user, token, URL,json_put);
				
				return null;
			}
			
			@Override
			protected void onPostExecute(HttpResponse response){				
				try {	
					Boolean success = response_json.getBoolean("success");
					String message = response_json.getString("message");
					
					if(success){	
						// alert registration Success				
						AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				        builder.setTitle("Registrarion successful!")
				        .setMessage(message)
				        .setCancelable(false)
				        .setNegativeButton("OK",new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int id) {
								Intent login = new Intent(getApplicationContext(), LoginActivity.class);
						        startActivity(login);
				            }
				        });
				        AlertDialog alert = builder.create();
				        alert.show();	
					}else{
						// alert registration Success				
						AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				        builder.setTitle("Something wrong happened.")
				        .setMessage(message)
				        .setCancelable(false)
				        .setNegativeButton("OK",new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int id) {
								Intent login = new Intent(getApplicationContext(), LoginActivity.class);
						        startActivity(login);
				            }
				        });
				        AlertDialog alert = builder.create();
				        alert.show();	
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.execute();
	}
}
