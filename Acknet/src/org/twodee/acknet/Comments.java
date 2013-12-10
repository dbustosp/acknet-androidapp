package org.twodee.acknet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;

import com.cs491.acknet.R;

public class Comments extends Activity {
	ArrayList<HashMap<String, String>> commentsList;
	ListView list_comments;
	String key_story;
	JSONArray jsonComments;
	LazyAdapter adapter;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		
		// Get story_key from intent
		Bundle extras = getIntent().getExtras();
		key_story = extras.getString("key_story");
		
		System.out.println("key_story: " + key_story);
		
		// ListView que ser‡ renderizado
		list_comments = (ListView) findViewById(R.id.list);		
		loadComments();
		
		
		
		
		
		
	}
	
	public void loadComments(){
			
			new AsyncTask<Void, Void, HttpResponse>() {

				@Override
				protected HttpResponse doInBackground(Void... params) {
					
					commentsList = new ArrayList<HashMap<String, String>>();
					//Get Post from get request
					HttpClient httpclient = new DefaultHttpClient();
				    HttpResponse response = null;
				    
				    try{
				    	String URL = Connection.getInstance().getIp() + "/comment/" + key_story; 
				    	
				    	response = httpclient.execute(new HttpGet(URL));
				    	StatusLine statusLine = response.getStatusLine();
				    	
				    	if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				    		ByteArrayOutputStream out = new ByteArrayOutputStream();
							response.getEntity().writeTo(out);
							out.close();
							String responseString = out.toString();
							
							JSONObject response_json = new JSONObject(responseString);
							Boolean status = response_json.getBoolean("success");
							
							if(status){
								jsonComments = new JSONArray();
				        		jsonComments = response_json.getJSONArray("comments");
				        		
				        		for(int i=0;i<jsonComments.length();i++){
				        			
				        			HashMap<String, String> map = new HashMap<String, String>();
				        			JSONObject childJSONObject = jsonComments.getJSONObject(i);
				        			
				        			// Get fields
				        			String key_comment = childJSONObject.getString("_id");
				        			String body_comment = childJSONObject.getString("body");
				        			String date_comment = childJSONObject.getString("date");
				        			String username_comment = childJSONObject.getString("username");
				        			
				        			map.put("_id", key_comment);
				        			map.put("body", body_comment);
				        			map.put("date", date_comment);
				        			map.put("username", username_comment);
				        			
				        			commentsList.add(map);
				        			
				        		}
				        		
				        		adapter = new LazyAdapter(Comments.this, commentsList);
				        		
				        		runOnUiThread(new Runnable() {
				        		     public void run() {			        		
				        		    	list_comments.setAdapter(adapter);
				        		    }
				        		});
				        		
							}else{
								AlertDialog.Builder builder = new AlertDialog.Builder(Comments.this);
						        builder.setTitle("success: FALSE!")
						        .setMessage("Show message here :) ")
						        .setCancelable(false)
						        .setNegativeButton("OK",new DialogInterface.OnClickListener() {
						            public void onClick(DialogInterface dialog, int id) {
						                dialog.cancel();
						            }
						        });
						        AlertDialog alert = builder.create();
						        alert.show();	
							}
							
				    	}
			        	
	
				    	
				    }catch (ClientProtocolException e) {
						System.out.println("ClientProtocolException");
						e.printStackTrace();
					} catch (IOException e) {
						System.out.println("IOException");
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						System.out.println("JSONException");
						e.printStackTrace();
					}
										
					
					
					return null;
				}
				
				
				
			}.execute();
	}
	

}












