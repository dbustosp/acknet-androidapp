package org.twodee.acknet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.cs491.acknet.R;

@SuppressLint("SimpleDateFormat")
public class CommentsActivity extends Activity {
	ArrayList<HashMap<String, String>> commentsList;
	ListView list_comments;
	String key_story;
	JSONArray jsonComments;
	CommentsAdapter adapter;
	String user;
	String username_owner;
	String token;
	Boolean is_owner;
	TextView remove_comment;
	JSONObject response_delete;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		
		
		
		// Get story_key from intent
		Bundle extras = getIntent().getExtras();
		key_story = extras.getString("key_story");
		username_owner = extras.getString("username_owner");
		
		// ListView que ser‡ renderizado
		list_comments = (ListView) findViewById(R.id.list);
		String URL = Connection.getInstance().getIp() + "/comment/" + key_story;

		
		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		user = SP.getString("username", "null");
		token = SP.getString("token", "null");
		
		System.out.println("User Preference: " + user);
		System.out.println("username_owner: " + username_owner);
		
		if(user.equals(username_owner)){
			is_owner = true;
		}else{
			is_owner = false;
		}
			
		loadComments(user, token, URL);	
		
		// Add Listener List View
		list_comments.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(CommentsActivity.this);
		        
				builder.setTitle(" Stop! ")
		        .setMessage("Are you sure that do you want to remove this comment ? ")
		        .setCancelable(false)
		        .setNegativeButton("No",new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		            }
		        })
		        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		                String URL = Connection.getInstance().getIp() + "/comment/" + commentsList.get(position).get("_id");
		                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		        		String user = SP.getString("username", "null");
		        		String token = SP.getString("token", "null");
		        		deleteComment(user, token, URL);
		            }
		        });
		        
		        AlertDialog alert = builder.create();
		        alert.show();
				
				
			}	
		});
	}

	public void deleteComment(final String user, final String token, final String URL){
		new AsyncTask<Void, Void, HttpResponse>() {

			@Override
			protected HttpResponse doInBackground(Void... params) {
				response_delete = new JSONObject();
				response_delete = Connection.getInstance().make_delete_request(user, token, URL);
				return null;
			}
			
			@Override
			protected void onPostExecute(HttpResponse response){
				try {
					if(response_delete.getString("success").equals("true")){
						AlertDialog.Builder builder2 = new AlertDialog.Builder(CommentsActivity.this);
				        builder2.setTitle(" Great! ")
				        .setMessage(response_delete.getString("message"))
				        .setCancelable(false)
				        .setNegativeButton("OK",new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int id) {
				            	Intent intent = new Intent(CommentsActivity.this, TimelineActivity.class);
				                startActivity(intent);
				            }
				        });
				        AlertDialog alert2 = builder2.create();
				        alert2.show();   
					}else{
						AlertDialog.Builder builder3 = new AlertDialog.Builder(CommentsActivity.this);
				        builder3.setTitle(" Bad! ")
				        .setMessage(response_delete.getString("message"))
				        .setCancelable(false)
				        .setNegativeButton("OK",new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int id) {
				            	dialog.cancel();
				            }
				        });
				        AlertDialog alert3 = builder3.create();
				        alert3.show();		
					}
				} catch (JSONException e) {
					
					e.printStackTrace();
				}	
			}
		}.execute();
	}
	
	public void loadComments(final String user, final String token, final String URL){

		new AsyncTask<Void, Void, HttpResponse>() {

			@SuppressLint("SimpleDateFormat")
			@Override
			protected HttpResponse doInBackground(Void... params) {

				commentsList = new ArrayList<HashMap<String, String>>();
				//Get Post from get request
				JSONObject json_response = Connection.getInstance().make_get_request(user, token, URL);

				try{

					Boolean status = json_response.getBoolean("success");
					String message = json_response.getString("message");

					if(status){
						jsonComments = new JSONArray();
						jsonComments = json_response.getJSONArray("comments");

						for(int i=0;i<jsonComments.length();i++){

							HashMap<String, String> map = new HashMap<String, String>();
							JSONObject childJSONObject = jsonComments.getJSONObject(i);

							// Get fields
							String key_comment = childJSONObject.getString("_id");
							System.out.println("key_comment: " + key_comment);
							String body_comment = childJSONObject.getString("body");
							String date_comment = childJSONObject.getString("date");
							String username_comment = childJSONObject.getString("username");

							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
							SimpleDateFormat output = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd ");
							Date d = sdf.parse(date_comment);
							String formattedTime = output.format(d);

							map.put("_id", key_comment);
							map.put("body", body_comment);
							map.put("date", formattedTime);
							map.put("username", username_comment);
							
							
							
							if(is_owner){
								System.out.println("is_owner   : " + user + " == " + username_owner + " ? " );
								map.put("can_remove", "true");
							}else{
								if(user.equals(username_comment)){
									System.out.println("user.equals:  " + user + " == " + username_comment + " ? " );
									map.put("can_remove", "true");
								}else{
									System.out.println(user + " == " + username_comment + " ? " );
									map.put("can_remove", "false");
								}
							}
							
							commentsList.add(map);
						}

						adapter = new CommentsAdapter(CommentsActivity.this, commentsList);

						runOnUiThread(new Runnable() {
							public void run() {			        		
								list_comments.setAdapter(adapter);
							}
						});

					}else{
						AlertDialog.Builder builder = new AlertDialog.Builder(CommentsActivity.this);
						builder.setTitle("Sorry. Impossible get the comments.")
						.setMessage(message)
						.setCancelable(false)
						.setNegativeButton("OK",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
						AlertDialog alert = builder.create();
						alert.show();	
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					System.out.println("JSONException");
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}


}












