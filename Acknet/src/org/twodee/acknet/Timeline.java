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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.cs491.acknet.R;

public class Timeline extends Activity{
	
	String responseString = null;
	String responseString1 = null;
	static final String URL = Connection.getInstance().getIp() + "/story";
	JSONArray jsonPosts;
	
	static final String KEY_BODY = "body";
	static final String KEY_USERNAME = "username";
	static final String KEY_DATE = "date";
	static final String KEY_THUMB_URL = "thumb_url";
	
	ListView list;
    LazyAdapter adapter;
	
	ArrayList<HashMap<String, String>> storyList;
	ArrayList<String> story_keys;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
		
		// ListView que ser‡ renderizado
		list = (ListView) findViewById(R.id.list);		
		loadStories();
		
		
		// Add Listener List View
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				System.out.println("Position: " + position);
				Intent intent = new Intent(Timeline.this, Story.class);
				intent.putExtra("username", storyList.get(position).get("username") );
				intent.putExtra("date", storyList.get(position).get("date") );
				intent.putExtra("body", storyList.get(position).get("body"));
				intent.putExtra("url", storyList.get(position).get("url"));
				intent.putExtra("type", storyList.get(position).get("type"));
				intent.putExtra("lat", storyList.get(position).get("lat"));
				intent.putExtra("lon", storyList.get(position).get("lon"));
				intent.putExtra("alt", storyList.get(position).get("alt"));
				intent.putExtra("key", storyList.get(position).get("key"));
				startActivity(intent);
			}
			
		});
		
			
	}
	
	
	public void loadStories(){
		
		new AsyncTask<Void, Void, HttpResponse>() {

			@Override
			protected HttpResponse doInBackground(Void... params) {
				storyList = new ArrayList<HashMap<String, String>>();
				//Get Post from get request
				HttpClient httpclient = new DefaultHttpClient();
			    HttpResponse response = null;
			    
			    try{
			    	
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
							jsonPosts = new JSONArray();
			        		jsonPosts = response_json.getJSONArray("stories");
			        		for(int i=0;i<jsonPosts.length();i++){
			        			HashMap<String, String> map = new HashMap<String, String>();
			        			story_keys = new ArrayList();
			        			JSONObject childJSONObject = jsonPosts.getJSONObject(i);
			        						        			
			        			// Get fields
			        			String body = childJSONObject.getString("body");
			        			String username = childJSONObject.getString("username");
			        			String date = childJSONObject.getString("date");
			        			JSONObject attachment = childJSONObject.getJSONObject("attachment");
			        			String type = attachment.getString("type");
			        			String url = attachment.getString("url");
			        			
			        			JSONObject geolocation = childJSONObject.getJSONObject("geolocation");
			        			String lat = geolocation.getString("lat");
			        			String lon = geolocation.getString("lon");
			        			String alt  = geolocation.getString("alt");
			        			
			        			// Get keys
			        			String key = childJSONObject.getString("_id");
			        			story_keys.add(key);
			        			
			   
			        			
			        			System.out.println(body);
			        			System.out.println(username);
			        			System.out.println(date);
			        			System.out.println(type);
			        			System.out.println(url);
			        			System.out.println(key);
			        			
			        					        			
			        			
			        			System.out.println();
			        			
			        			
			        			map.put(KEY_BODY, body);
			        			map.put(KEY_USERNAME, username);
			        			map.put(KEY_DATE, date);
			        			map.put("date", date);
			        			map.put("type", type);
			        			map.put("url", url);
			        			map.put("lat", lat);
			        			map.put("lon", lon);
			        			map.put("alt", alt);
			        			map.put("key", key);			        			
			        			//map.put("link", )
			        			// adding HashList to ArrayList
			        			storyList.add(map);
			        		}
			        			        		
			        		adapter = new LazyAdapter(Timeline.this, storyList);
			        		
			        		runOnUiThread(new Runnable() {
			        		     public void run() {			        		
			        		    	list.setAdapter(adapter);
			        		    }
			        		});

						}else{
							AlertDialog.Builder builder = new AlertDialog.Builder(Timeline.this);
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
