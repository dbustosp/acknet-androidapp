package org.twodee.acknet;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs491.acknet.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class Story extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{
	ArrayList<String> storyList;
	String username;
	String type;
	String date;
	String url;
	String lat;
	String lon;
	String alt;
	String body;
	String key_comment;
	TextView txtView_username;
	TextView txtView_body;
	
	
	
	public static final String API_KEY = "AIzaSyCcdRKwLTXED-Lq27t94kI3kPrqmL-c1hk";
	public String VIDEO_ID;
	
	private ImageView imageView;
	LinearLayout mainLayout;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.story);
		
		System.out.println("onCreate -- Story");
		
		//TextView
	    final TextView see_comments = (TextView) findViewById(R.id.see_comments);
	    // Listener for see comments
	    see_comments.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				Intent comments_intent = new Intent(getApplicationContext(), Comments.class);
				startActivityForResult(comments_intent, 0);
			
			}
	    });
	    
		//get Data from Intent
		Bundle extras = getIntent().getExtras(); 		
		username = extras.getString("username");
		type = extras.getString("type");
		date = extras.getString("date");
		url = extras.getString("url");
		lat = extras.getString("lat");
		lon = extras.getString("lon");
		alt = extras.getString("alt");
		body = extras.getString("body");
		key_comment = extras.getString("key");

		if(type.equals("image")){
			// Post image
			setInformationPicture();
			imageView = new ImageView(Story.this);
			mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
		}else if(type.equals("video")){
			setInformationVideo();
		}else{
			// Post only text
			setInformationText();
		}
		
		
		
		
		
		
	}
	
	private void setInformationText(){
		txtView_username = (TextView) findViewById(R.id.textView1);
        txtView_username.setText(username);
        
        txtView_body = (TextView) findViewById(R.id.textView3);
        txtView_body.setText(body);
        
        
	}
	
	
	private void setInformationVideo(){
		
		txtView_username = (TextView) findViewById(R.id.textView1);
        txtView_username.setText(username);
        
        txtView_body = (TextView) findViewById(R.id.textView3);
        txtView_body.setText(body);
		
		
		// Post Video
		System.out.println("It's video!");
		// get Id_VIDEO
		
		VIDEO_ID = url;
		System.out.println("VIDEO_KEY: " + url);
		
		YouTubePlayerView youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtubeplayerview);
        youTubePlayerView.initialize(API_KEY, this);
	}
	
	 private void setInformationPicture(){

         new AsyncTask<Void, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(Void... params) {
				
				Bitmap bmap = null;
				try{
					 URL url_ = new URL(url);
                     URLConnection con = url_.openConnection();
                     InputStream in = con.getInputStream();
                     bmap = BitmapFactory.decodeStream(in);
	             } catch (MalformedURLException e){
	                     e.printStackTrace();       
	             } catch (IOException e) {
	                     e.printStackTrace();
	             }
	             return bmap;
			}
			
			@Override
            protected void onPostExecute(Bitmap bmap){
				imageView.setImageBitmap(bmap);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,300);
	            imageView.setLayoutParams(lp);
	            ImageView img2 = new ImageView(Story.this);
	            img2.setImageBitmap(bmap);
	            ImageView img3 = new ImageView(Story.this);
	            img3.setImageBitmap(bmap);
	            mainLayout.addView(imageView, lp);
	            //mainLayout.addView(img2, lp);
	            //mainLayout.addView(img3, lp);
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
		 Toast.makeText(getApplicationContext(), 
				    "onInitializationFailure()", 
				    Toast.LENGTH_LONG).show();
	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer arg1,
			boolean arg2) {
		if (!arg2) {
			arg1.cueVideo(VIDEO_ID);
	      }
	}

}
