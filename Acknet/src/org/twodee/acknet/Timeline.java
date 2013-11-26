package org.twodee.acknet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.cs491.acknet.R;

public class Timeline extends Activity{
	private RelativeLayout timelineLayout;
	
	String responseString = null;
	String responseString1 = null;
	
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
		
		
		Button loadButton = (Button) findViewById(R.id.loadButton);
		loadButton.setOnClickListener(new OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	System.out.println("Init outside -- load image");
		        loadImage();
		        System.out.println("End outside -- load image");
		      }
		});
		timelineLayout = (RelativeLayout) findViewById(R.id.layout_pictures);		
	}
	
	private void loadImage() {
		
		new AsyncTask<Void, Void, ArrayList<Bitmap>>() {

			@Override
			protected ArrayList<Bitmap> doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
				HttpClient httpclient = new DefaultHttpClient();
			    HttpResponse response;
			    
			    
				
				try {
								       
			        String IP = Connection.getInstance().getIp();
			        String URL = IP + "/get_pictures";			        
			        response = httpclient.execute(new HttpGet(URL));
			        StatusLine statusLine = response.getStatusLine();
			        
			        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
			        	System.out.println("OK");
			        	
			        	ByteArrayOutputStream out = new ByteArrayOutputStream();
				        response.getEntity().writeTo(out);
		                out.close();
		                responseString1 = out.toString();
		                
		                JSONObject jsnobject = null;
						jsnobject = new JSONObject(responseString1);
						JSONArray jsonArray = null;
						jsonArray = jsnobject.getJSONArray("pictures");
						
						Bitmap bmap = null;
						
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject explrObject = null;
							explrObject = jsonArray.getJSONObject(i);
							
							
							
							System.out.println(explrObject.get("path"));
							
							URL url = new URL(explrObject.get("path").toString());
							URLConnection con = url.openConnection();
							InputStream in = con.getInputStream();
							
							bmap = BitmapFactory.decodeStream(in);
							
							bitmaps.add(bmap);					
							
							bmap = null;						
							
		            	}
 
			        }else{
			        	System.out.println("Else");
			        }

			        System.out.println("Timeline ---------------- try end");
				}catch (MalformedURLException e) {
			          e.printStackTrace();
		        } catch (IOException e) {
		          e.printStackTrace();
		        } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        return bitmaps;
			}
			
			@Override
		      protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
				
				//bitmap.
				
				System.out.println("onPostExecute -- Init");
				
				for(Bitmap bmap : bitmaps){
					
					ImageView img = new ImageView(Timeline.this);
					img.setImageBitmap(bmap);
					timelineLayout.addView(img);				
					
				}
				
				System.out.println("onPostExecute -- End");
			}
			
			
		}.execute();
	
	}
	
	
	
	private void putNewPost(final View v) {		
		
		new AsyncTask<Void, Void, HttpResponse>() {
			
			@Override
			protected HttpResponse doInBackground(Void... params) {
				
				//Implementation send request post to 
				String IP = Connection.getInstance().getIp();
				String URL = IP + "/timeline";
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
