package org.twodee.acknet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public final class Connection {
	
	// Variables
	private String IP;
	
	private static Connection instance = new Connection();
	
	
	private Connection(){
		IP = "http://137.28.132.62:3000";	
	}
	
	public static Connection getInstance(){
		return instance;
	}
	
	public String getIp(){
		return IP;
	}
	
	
	public JSONObject make_get_request(final String user, final String token, final String URL){				
				DefaultHttpClient httpclient = new DefaultHttpClient();
			    HttpResponse response = null;
			    CredentialsProvider credProvider = new BasicCredentialsProvider();
			    
			    credProvider.setCredentials(
			    		new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
			    		new UsernamePasswordCredentials(user, token)
			    );
			    httpclient.setCredentialsProvider(credProvider);
			    
			    try{
			    	
			    	HttpGet connection_get = new HttpGet(URL);			    				    	
			    	response = httpclient.execute(connection_get);
			    	StatusLine statusLine = response.getStatusLine();
			    	
			    	if(statusLine.getStatusCode() == HttpStatus.SC_OK){
			    		ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						String responseString = out.toString();
						JSONObject response_json = new JSONObject(responseString);				
						return response_json;
			    	}
			    	
			    }catch (ClientProtocolException e) {
					System.out.println("ClientProtocolException");
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("IOException");
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					System.out.println("JSONException");
					e.printStackTrace();
				}
			
				return null;
		}		
	
	
}
