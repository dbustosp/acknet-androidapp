package org.twodee.acknet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.cs491.acknet.R;

public class CustomizedListView extends Activity {
	
	static final String IP = Connection.getInstance().getIp();
	
	
	// All static variables
	static final String URL = IP + "/posts";
	// XML node keys
	static final String KEY_SONG = "song"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_ARTIST = "artist";
	static final String KEY_DURATION = "duration";
	static final String KEY_THUMB_URL = "thumb_url";
	
	ListView list;
    LazyAdapter adapter;
    ArrayList<HashMap<String, String>> songsList;
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
		
		makeRequest();
	}
	
	
	private void makeRequest() {		

				System.out.println("init doInBackground - CustomizedListView");
				
				songsList = new ArrayList<HashMap<String, String>>();
				//Get Post from get request
				HttpClient httpclient = new DefaultHttpClient();
			    HttpResponse response = null;
			    
			    try {
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
			        		System.out.println("Status true");
			        		JSONArray jsonPosts = new JSONArray();
			        		jsonPosts = response_json.getJSONArray("posts");
			        		for(int i=0;i<jsonPosts.length();i++){
			        			HashMap<String, String> map = new HashMap<String, String>();
			        			JSONObject childJSONObject = jsonPosts.getJSONObject(i);
			        			String body = childJSONObject.getString("body");
			        			String username = childJSONObject.getString("user");
			        			map.put(KEY_TITLE, body);
			        			map.put(KEY_ARTIST, username);
			        			// adding HashList to ArrayList
			        			songsList.add(map);
			        		}
			        		
			        		// ListView que ser‡ renderizado
			        		list=(ListView)findViewById(R.id.list);
			        		// Getting adapter by passing xml data ArrayList		        		

			        		adapter=new LazyAdapter(this, songsList);        
			                list.setAdapter(adapter);
			        		
			        		
			        	}else{
			        		System.out.println("Status false");
			        		
			        	
			        	}
						
						
					}else{
						
					}
					
			    } catch (ClientProtocolException e) {
					System.out.println("ClientProtocolException");
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("IOException");
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			    System.out.println("End doInBackground - CustomizedListView");

	}
	
}