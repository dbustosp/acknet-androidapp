package org.twodee.acknet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;

import com.cs491.acknet.R;

@SuppressLint("SimpleDateFormat")
public class CommentsActivity extends Activity {
	ArrayList<HashMap<String, String>> commentsList;
	ListView list_comments;
	String key_story;
	JSONArray jsonComments;
	CommentsAdapter adapter;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);

		// Get story_key from intent
		Bundle extras = getIntent().getExtras();
		key_story = extras.getString("key_story");

		// ListView que ser‡ renderizado
		list_comments = (ListView) findViewById(R.id.list);
		String URL = Connection.getInstance().getIp() + "/comment/" + key_story;

		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String user = SP.getString("username", "null");
		String token = SP.getString("token", "null");

		loadComments(user, token, URL);	
	}

	public void loadComments(final String user, final String token, final String URL){

		new AsyncTask<Void, Void, HttpResponse>() {

			@SuppressLint("SimpleDateFormat")
			@Override
			protected HttpResponse doInBackground(Void... params) {

				commentsList = new ArrayList<HashMap<String, String>>();
				//Get Post from get request
				JSONObject json_response = Connection.getInstance().make_get_request(user, token, URL);

				try{

					Boolean status = json_response.getBoolean("success");
					String message = json_response.getString("message");

					if(status){
						jsonComments = new JSONArray();
						jsonComments = json_response.getJSONArray("comments");

						for(int i=0;i<jsonComments.length();i++){

							HashMap<String, String> map = new HashMap<String, String>();
							JSONObject childJSONObject = jsonComments.getJSONObject(i);

							// Get fields
							String key_comment = childJSONObject.getString("_id");
							String body_comment = childJSONObject.getString("body");
							String date_comment = childJSONObject.getString("date");
							String username_comment = childJSONObject.getString("username");

							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
							SimpleDateFormat output = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd ");
							Date d = sdf.parse(date_comment);
							String formattedTime = output.format(d);

							map.put("_id", key_comment);
							map.put("body", body_comment);
							map.put("date", formattedTime);
							map.put("username", username_comment);

							commentsList.add(map);

						}

						adapter = new CommentsAdapter(CommentsActivity.this, commentsList);

						runOnUiThread(new Runnable() {
							public void run() {			        		
								list_comments.setAdapter(adapter);
							}
						});

					}else{
						AlertDialog.Builder builder = new AlertDialog.Builder(CommentsActivity.this);
						builder.setTitle("Sorry. Impossible get the comments.")
						.setMessage(message)
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
					// TODO Auto-generated catch block
					System.out.println("JSONException");
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}


}












