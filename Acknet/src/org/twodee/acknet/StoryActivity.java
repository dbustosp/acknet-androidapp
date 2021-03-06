package org.twodee.acknet;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

public class StoryActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{
	ArrayList<String> storyList;
	String username;
	String type;
	String date;
	String url;
	String lat;
	String lon;
	String alt;
	String body;
	String key_story;
	TextView txtView_body;
	JSONObject json;
	JSONObject json_delete;
	Boolean is_owner;
	public static final String API_KEY = "AIzaSyCcdRKwLTXED-Lq27t94kI3kPrqmL-c1hk";
	public String VIDEO_ID;
	private ImageView imageView;
	LinearLayout mainLayout;
	SharedPreferences SP;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story);

		final TextView see_comments = (TextView) findViewById(R.id.see_comments);
		final TextView remove_story = (TextView) findViewById(R.id.remove_story);
		final Button btn_sendComment = (Button) findViewById(R.id.btn_sendComment);
		final EditText edit_comment = (EditText) findViewById(R.id.comment);

		//Get Data from Intent
		Bundle extras = getIntent().getExtras(); 		
		username = extras.getString("username");
		type = extras.getString("type");
		date = extras.getString("date");
		url = extras.getString("url");
		lat = extras.getString("lat");
		lon = extras.getString("lon");
		alt = extras.getString("alt");
		body = extras.getString("body");
		key_story = extras.getString("key_story");
		
		SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String user = SP.getString("username", "null");
		
		// If they are not the same so remove the option to remove the story
		if(!user.equals(username)){
			setInvisibleRemove();
		}
	
		if(type.equals("image")){
			// Post image
			setVideoLayerInvisible();
			setInformationPicture();
			imageView = new ImageView(StoryActivity.this);
			mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
		}else if(type.equals("video")){
			setInformationVideo();
		}else{
			// Post only text
			setVideoLayerInvisible();
			setInformationText();
		}

		remove_story.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// Alert first
				AlertDialog.Builder builder = new AlertDialog.Builder(StoryActivity.this);
				builder.setTitle("Stop!")
				.setMessage("Are you sure that do you want to remove this story?")
				.setCancelable(true)
				.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();						
					}
				})
				
				.setNegativeButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// user
						SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
						String user = SP.getString("username", "null");
						String token = SP.getString("token", "null");
						String URL = Connection.getInstance().getIp() + "/story/" + key_story + "/" + user;
						deleteStory(user, token, URL);
						

					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			
				
				
				
				
			}
		});
		
		btn_sendComment.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {

				String comment = edit_comment.getText().toString();

				if(comment.equals("")){
					AlertDialog.Builder builder = new AlertDialog.Builder(StoryActivity.this);
					builder.setTitle("Your need to write something! ")
					.setMessage("Try again! :) ")
					.setCancelable(false)
					.setNegativeButton("OK",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}else{
					// Send comment
					SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
					String user = SP.getString("username", "null");
					String token = SP.getString("token", "null");
					JSONObject json_put = new JSONObject();

					try {
						json_put.put("username_posting", user);
						json_put.put("key_story", key_story);
						json_put.put("comment", comment);
					} catch (JSONException e) {

						e.printStackTrace();
					}

					sendComment(user, token, json_put);
				}
			}
		});


		see_comments.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent comments_intent = new Intent(getApplicationContext(), CommentsActivity.class);
				comments_intent.putExtra("key_story", key_story );
				comments_intent.putExtra("username_owner", username );
				startActivityForResult(comments_intent, 0);
			}
		});
	}

	public void setInvisibleRemove(){
		TextView remove = (TextView) findViewById(R.id.remove_story);
		remove.setVisibility(View.GONE);
	}
	
	
	public void setVideoLayerInvisible(){
		LinearLayout youtube = (LinearLayout) findViewById(R.id.layout_youtube);
		youtube.setVisibility(View.GONE);
	}
	
	public void deleteStory(final String user, final String token, final String URL){
		new AsyncTask<Void, Void, HttpResponse>() {

			@Override
			protected HttpResponse doInBackground(Void... params) {
				json_delete = new JSONObject();
				json_delete = Connection.getInstance().make_delete_request(user, token, URL);
				return null;
			}
			
			@Override
			protected void onPostExecute(HttpResponse response){
				try {
					if(json_delete.getString("success").equals("true")){
						AlertDialog.Builder builder2 = new AlertDialog.Builder(StoryActivity.this);
						builder2.setTitle("Stop!")
						.setMessage(json_delete.getString("message"))
						.setCancelable(true)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(StoryActivity.this, TimelineActivity.class);		
								startActivity(intent);
							}
						});
						AlertDialog alert2 = builder2.create();
						alert2.show();
					}else{
						AlertDialog.Builder builder3 = new AlertDialog.Builder(StoryActivity.this);
						builder3.setTitle("Stop!")
						.setMessage(json_delete.getString("message"))
						.setCancelable(true)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
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

	public void sendComment(final String user, final String token, final JSONObject json_put){

		new AsyncTask<Void, Void, HttpResponse>() {

			@Override
			protected HttpResponse doInBackground(Void... params) {				
				json = new JSONObject();
				String URL = Connection.getInstance().getIp() + "/comment";				
				json = Connection.getInstance().make_post_request(user, token, URL, json_put);

				return null;
			}


			@Override
			protected void onPostExecute(HttpResponse response){
				System.out.println("onPostExecute - Init");

				try {
					Boolean status = json.getBoolean("success");
					if(status){
						AlertDialog.Builder builder = new AlertDialog.Builder(StoryActivity.this);
						builder.setTitle("Comment sent it succesfully.")

						.setMessage("Post for success!.")
						.setCancelable(false)
						.setNegativeButton("OK",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent comments_intent = new Intent(getApplicationContext(), CommentsActivity.class);
								comments_intent.putExtra("key_story", key_story );
								startActivityForResult(comments_intent, 0);
							}
						});
						AlertDialog alert = builder.create();
						alert.show();
					}else{
						AlertDialog.Builder builder = new AlertDialog.Builder(StoryActivity.this);
						builder.setTitle("Sorry something wrong to send Post.")

						.setMessage("Comment not success!.")
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
					
					e.printStackTrace();
				}
			}
		}.execute();
	}


	private void setInformationText(){        
		txtView_body = (TextView) findViewById(R.id.textView3);
		txtView_body.setText(body);    
	}


	private void setInformationVideo(){

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
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(500,500);
				imageView.setLayoutParams(lp);
				ImageView img2 = new ImageView(StoryActivity.this);
				img2.setImageBitmap(bmap);
				ImageView img3 = new ImageView(StoryActivity.this);
				img3.setImageBitmap(bmap);
				mainLayout.addView(imageView);
				//mainLayout.addView(img2, lp);
				//mainLayout.addView(img3, lp);
			}

		}.execute();

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
