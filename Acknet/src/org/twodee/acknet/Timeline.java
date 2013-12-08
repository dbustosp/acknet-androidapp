package org.twodee.acknet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.cs491.acknet.R;

public class Timeline extends Activity{
	private RelativeLayout timelineLayout;
	
	String responseString = null;
	String responseString1 = null;
	static final String URL = Connection.getInstance().getIp() + "/story";
	
	static final String KEY_BODY = "title";
	static final String KEY_USERNAME = "username";
	static final String KEY_DATE = "date";
	static final String KEY_THUMB_URL = "thumb_url";
	
	ListView list;
    LazyAdapter adapter;
	
	ArrayList<HashMap<String, String>> storyList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
		
		
		loadStories();
		    		
			
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
							JSONArray jsonPosts = new JSONArray();
			        		jsonPosts = response_json.getJSONArray("stories");
			        		for(int i=0;i<jsonPosts.length();i++){
			        			HashMap<String, String> map = new HashMap<String, String>();
			        			JSONObject childJSONObject = jsonPosts.getJSONObject(i);
			        			
			        			
			        			
			        			String body = childJSONObject.getString("body");
			        			String username = childJSONObject.getString("username");
			        			
			        			System.out.println(body);
			        			System.out.println(username);
			        			System.out.println();
			        			
			        			
			        			map.put(KEY_BODY, body);
			        			map.put(KEY_USERNAME, username);
			        			// adding HashList to ArrayList
			        			storyList.add(map);
			        		}
			        		// ListView que ser‡ renderizado
			        		list = (ListView) findViewById(R.id.list);
			        		// Getting adapter by passing xml data ArrayList		        		
		
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
