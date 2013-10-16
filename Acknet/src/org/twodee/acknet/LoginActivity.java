package org.twodee.acknet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_login);
	
	
	    final Button loginButton = (Button) findViewById(R.id.login_2);
	    final EditText login_username = (EditText)findViewById(R.id.username);
	    final EditText login_password = (EditText)findViewById(R.id.password);
	    
	    loginButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				System.out.println("EditText username: " + login_username.getText().toString());
				System.out.println("EditText username: " + login_password.getText().toString());
				
				//int j = postData(login_username.getText().toString(),login_password.getText().toString());
								
			}
        });
	    
	    
	
	
	
	}
	
	public int postData(String username, String password){
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://arcane-crag-4782.herokuapp.com/user/" + username);
		
		try {
	        // Add your data
	        
			List<NameValuePair> data_login = new ArrayList<NameValuePair>(2);
	        
			data_login.add(new BasicNameValuePair("username", username));
			data_login.add(new BasicNameValuePair("password", password));
	        
	        httppost.setEntity(new UrlEncodedFormEntity(data_login));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        
	        System.out.println("Response: " + response);
	        
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	System.out.println("ClientProtocolException");
	    } catch (IOException e) {
	    	System.out.println("IOEXCEPTION");
	    }
		
		
		return 0;
	}
	
	
	
}
