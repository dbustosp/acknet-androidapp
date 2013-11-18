package org.twodee.acknet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PlacesActivity extends Activity {
	String text_post;
	String IP = Connection.getInstance().getIp();
	String URL = IP + "/timeline";
	String username;
	SharedPreferences SP;
	JSONObject json;
	
	/** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.places_layout);
       
       final EditText post_field = (EditText)findViewById(R.id.boxPost);
       
       final Button post = (Button) findViewById(R.id.post);
       
       post.setOnClickListener(new OnClickListener(){

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				
				SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				
				username = SP.getString("username", "null");
				text_post = post_field.getText().toString();
				
				if(text_post.equals("")){
					FireMissilesDialogFragment dialog = new FireMissilesDialogFragment();
					dialog.show(getFragmentManager(), "missiles");
				}else{
					putNewPost(arg0);
				}				
			}
		}); 
   }
   
   private void putNewPost(final View v) {		
		
		new AsyncTask<Void, Void, HttpResponse>() {

			@Override
			protected HttpResponse doInBackground(Void... params) {
				
				HttpResponse response = null;
				json = new JSONObject();
				
				try{
					json.put("body",text_post);
					json.put("user", username);
					json = makeRequest(URL, json);
					
					
				}catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			}
			
			@Override
			protected void onPostExecute(HttpResponse response){
				System.out.println("onPostExecute - Init");
				
				try {
					Boolean status = json.getBoolean("success");
					String message = json.getString("message");
					
					if(status){
						System.out.println(message);
					}else{
						System.out.println(message);
					}		
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.execute();
   }
   
   private JSONObject makeRequest(String url, JSONObject json) {
	   
	   try{
		URI uri = new URI(String.format(url));
		HttpPost post = new HttpPost(uri);
		StringEntity entity = new StringEntity(json.toString());
		
		entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	    post.setEntity(entity);
	    
	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response = client.execute(post);
	    
	    Scanner in = new Scanner(response.getEntity().getContent());
	    in.useDelimiter("\\Z");
	    String body = in.next();
	    in.close();
	    
	    return new JSONObject(body);
	    
		
	   }catch (URISyntaxException e) {
		      e.printStackTrace();
	   } catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   } catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   } catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
		
	   
	   
	   return json;	   
   }
   
   
   
   
   
}
