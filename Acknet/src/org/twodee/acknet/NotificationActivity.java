package org.twodee.acknet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;

import com.cs491.acknet.R;

public class NotificationActivity extends Activity {
	
	ArrayList<HashMap<String, String>> notificationsList;
	StoriesAdapter adapter;
	ListView list;
	Boolean is_not = false;
	public String URL = Connection.getInstance().getIp() + "/notification";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        
        notificationsList = new ArrayList<HashMap<String, String>>();
        list = (ListView) findViewById(R.id.list_notifications);
        
        handle_get_request();
        
    }
    
    
	public void handle_get_request (){
		
		new AsyncTask<Void, Void, HttpResponse>() {

			@Override
			protected HttpResponse doInBackground(Void... params) {
				
				SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			    String user = SP.getString("username", "");
			    String token = SP.getString("token", "");
				
				JSONObject response_json = new JSONObject();
				response_json = Connection.getInstance().make_get_request(user, token, URL);
				
				JSONArray jsonNotifications = new JSONArray();
				try {
					jsonNotifications = response_json.getJSONArray("notifications");
					
					System.out.println(response_json);
					System.out.println();
					System.out.println();
					System.out.println(jsonNotifications.length());
					
					
					
					for(int i=0;i<jsonNotifications.length();i++){
						
						HashMap<String, String> map = new HashMap<String, String>();
	        			JSONObject childJSONObject = jsonNotifications.getJSONObject(i);
						
	        			System.out.println("Child:");
	        			System.out.println(childJSONObject);
	        			
	        			
	        			map.put("body", childJSONObject.getString("message"));
						map.put("date", childJSONObject.getString("date"));
						map.put("username", Integer.toString(i+1));
						notificationsList.add(map);
					}
					
					adapter = new StoriesAdapter(NotificationActivity.this, notificationsList);
	        		
	        		runOnUiThread(new Runnable() {
	        		     public void run() {			        		
	        		    	list.setAdapter(adapter);
	        		    }
	        		});
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				return null;
			}
			
			
			
		}.execute();
}
    
    
    
}
