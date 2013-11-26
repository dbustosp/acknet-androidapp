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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline2);
		
		// Llenar con objetos Json
		ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

		//Get Post from get request
		HttpClient httpclient = new DefaultHttpClient();
	    HttpResponse response = null;
		try {
			response = httpclient.execute(new HttpGet(URL));
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    StatusLine statusLine = response.getStatusLine();
		
	    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        try {
				response.getEntity().writeTo(out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        String responseString = out.toString();
	        
	        //System.out.println("responseString => : " + responseString);
	        
	        try {
				
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
	        	
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        
	        
	    } else{
	        //Closes the connection.
	        try {
				response.getEntity().getContent().close();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
				throw new IOException(statusLine.getReasonPhrase());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    
		
		/*XMLParser parser = new XMLParser();
		String xml = parser.getXmlFromUrl(URL); // getting XML from URL
		Document doc = parser.getDomElement(xml); // getting DOM element
		
		NodeList nl = doc.getElementsByTagName(KEY_SONG);
		// looping through all song nodes <song>
		for (int i = 0; i < nl.getLength(); i++) {
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			Element e = (Element) nl.item(i);
			// adding each child node to HashMap key => value
			map.put(KEY_ID, parser.getValue(e, KEY_ID));
			map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
			map.put(KEY_ARTIST, parser.getValue(e, KEY_ARTIST));
			map.put(KEY_DURATION, parser.getValue(e, KEY_DURATION));
			map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));

			// adding HashList to ArrayList
			songsList.add(map);
		}
		
		
		// ListView que ser‡ renderizado
		list=(ListView)findViewById(R.id.list);
		
		// Getting adapter by passing xml data ArrayList
        adapter=new LazyAdapter(this, songsList);        
        list.setAdapter(adapter);
        */

        // Click event for single list row
        /*list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
							

			}
		});
		*/		
	}	
}