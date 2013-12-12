package org.twodee.acknet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Configuration;
import android.os.AsyncTask;

public final class Connection {
	
	// Variables
	private String IP;
	
	private static Connection instance = new Connection();
	
	
	private Connection(){
		IP = "http://137.28.135.30:3000";	
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
	
		public JSONObject make_post_request(final String user, final String token, final String URL, JSONObject object){				
			System.out.println("Making post -request");
			try{
				DefaultHttpClient httpclient = new DefaultHttpClient();
				CredentialsProvider credProvider = new BasicCredentialsProvider();
			    
			    credProvider.setCredentials(
			    		new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
			    		new UsernamePasswordCredentials(user, token)
			    );
			    httpclient.setCredentialsProvider(credProvider);
			    
			    URI uri = new URI(String.format(URL));
				HttpPost post = new HttpPost(uri);
				
				StringEntity entity = new StringEntity(object.toString());
				
				
				entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			    post.setEntity(entity);
			    
			    HttpResponse response = httpclient.execute(post);
			    
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
			
			return null;
		}

		
}
