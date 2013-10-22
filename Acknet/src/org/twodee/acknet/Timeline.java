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

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Timeline extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
		
		final Button send_post = (Button) findViewById(R.id.send_post);
		
		send_post.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				putNewPost(arg0);
				
			}
		});	
	}
	
	private void putNewPost(final View v) {		
		
		new AsyncTask<Void, Void, HttpResponse>() {
			
			@Override
			protected HttpResponse doInBackground(Void... params) {
				
				//Implementation send request post to /timeline/
				HttpClient httpclient = new DefaultHttpClient();
				String IP = Connection.getInstance().getIp();
				String URL = IP + "/timeline";
				HttpPost httppost = new HttpPost(URL);
			    HttpResponse response = null;
				
			    JSONObject json = new JSONObject();
			    try {
					json.put("title", "myFirstPost");
					json.put("body", "bodyPost");
				    json.put("attached", "attachedFile");
				    json.put("user", null);
				    
				    json = makeRequest(URL, json);
				    
				    System.out.println("JSOOOOON: " + json);
				    
				    
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			    
			    
			    
			    
			    
			    
			    return response;
			}
			
			
			
			@Override
			protected void onPostExecute(HttpResponse response){
				System.out.println("onPostExecute - Init");								
			}
			
		}.execute();
		
	}
	
	
	private JSONObject makeRequest(String url, JSONObject json) {
		
		try{
			
			URI uri = new URI(String.format(url));
			HttpPost post = new HttpPost(uri);
			StringEntity entity = new StringEntity(json.toString());
			
			Log.d("FOO", "sending " + json.toString());
			
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		    post.setEntity(entity);
		    
		    HttpClient client = new DefaultHttpClient();
		    HttpResponse response = client.execute(post);
		    
		    Scanner in = new Scanner(response.getEntity().getContent());
		    in.useDelimiter("\\Z");
		    String body = in.next();
		    in.close();
		      
		    Log.d("Timeline - FOOOOO", body);
		    
		    return new JSONObject(body);
			
			
		}catch (URISyntaxException e) {
		      e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	      e.printStackTrace();
	    } catch (ClientProtocolException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    } catch (JSONException e) {
	      e.printStackTrace();
	    }
		
		return null;
	}
	

}
