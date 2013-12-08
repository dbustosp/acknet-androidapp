package org.twodee.acknet;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.cs491.acknet.R;

public class AndroidDashboardDesignActivity extends Activity{
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
         
        /**
         * Creating all buttons instances
         * */
        // Dashboard News feed button
        Button btn_newsfeed = (Button) findViewById(R.id.btn_news_feed);
         
        // Dashboard Friends button
        Button btn_friends = (Button) findViewById(R.id.btn_friends);
         
        // Dashboard Messages button
        Button btn_messages = (Button) findViewById(R.id.btn_messages);
         
        // Dashboard Places button
        Button btn_posts = (Button) findViewById(R.id.btn_posts);
         
        // Dashboard Events button
        Button btn_events = (Button) findViewById(R.id.btn_events);
         
        // Dashboard Photos button
        Button btn_photos = (Button) findViewById(R.id.btn_photos);
         
        /**
         * Handling all button click events
         * */
         
        // Listening to News Feed button click
        btn_newsfeed.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), Timeline.class);
                startActivity(i);
            }
        });
         
       // Listening Friends button click
       btn_friends.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), FriendsActivity.class);
                startActivity(i);
            }
        });
       
       
       
        // Listening Messages button click
        btn_messages.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), MessagesActivity.class);
                startActivity(i);
            }
        });
         
        // Listening to Places button click
        btn_posts.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), PostActivity.class);
                startActivity(i);
            }
        });
         
        // Listening to Events button click
        btn_events.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Call Logout function
            	logout();
            	
            	
            }
        });
         
        // Listening to Photos button click
        btn_photos.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), AndroidListViewActivity.class);
                startActivity(i);
            }
        });  
    }
	
	private void logout(){
		new AsyncTask<Void, Void, HttpResponse>() {

			@Override
			protected HttpResponse doInBackground(Void... params) {
				
				SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				String token = SP.getString("token", "null");
				String username = SP.getString("username", "null");
				
				// Prepare the URL
				String address = Connection.getInstance().getIp() + "/session/" + username + "/" + token; 

				try {
					
					URL url = new URL(address);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("DELETE");
					int responseCode = connection.getResponseCode();

				} catch (MalformedURLException e) {
					// SHOW A MESSAGE 
					
					
					e.printStackTrace();
				}catch(IOException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}
			
			@Override
			protected void onPostExecute(HttpResponse response){
				Intent i = new Intent(getApplicationContext(), SplashScreen.class);
                startActivity(i);
			}
			
			
			
		}.execute();
	}
	
	
}
