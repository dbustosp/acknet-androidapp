package org.twodee.acknet;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.cs491.acknet.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;

public class Story extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{
	ArrayList<String> storyList;
	String username;
	String type;
	String date;
	String link;
	String lat;
	String lon;
	String alt;
	String body;
	TextView txtView_username;
	TextView txtView_body;
	
	public static final String API_KEY = "AIzaSyCcdRKwLTXED-Lq27t94kI3kPrqmL-c1hk";
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.story);
		
		System.out.println("onCreate -- Story");
		
		//get Data from Intent
		Bundle extras = getIntent().getExtras(); 		
		username = extras.getString("username");
		type = extras.getString("type");
		date = extras.getString("date");
		link = extras.getString("link");
		lat = extras.getString("lat");
		lon = extras.getString("lon");
		alt = extras.getString("alt");
		body = extras.getString("body");

		if(type.equals("image")){
			// Post image
		}else if(type.equals("video")){
			// Post Video
		}else{
			// Post only text
		}
		
		
		setInformation();
	}
	
	 private void setInformation(){
		 
		  
		 
         new AsyncTask<Void, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(Void... params) {
				
				
				
				return null;
			}
        	 
         }.execute();
	 
         txtView_username = (TextView) findViewById(R.id.textView1);
         txtView_username.setText(username);
         
         txtView_body = (TextView) findViewById(R.id.textView3);
         txtView_body.setText(body);
	 
	 }

	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer arg1,
			boolean arg2) {
		// TODO Auto-generated method stub
		
	}

}
