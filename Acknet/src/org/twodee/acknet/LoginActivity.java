package org.twodee.acknet;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_login);
	    
	    final Button login = (Button) findViewById(R.id.login);
        
        login.setOnTouchListener(new OnTouchListener(){

        	@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
        		
        		EditText usernameText   = (EditText)findViewById(R.id.username);
        		EditText passwordText   = (EditText)findViewById(R.id.password);
				String username = usernameText.getText().toString();
				String password = passwordText.getText().toString();
				User user = new User(username, password);
				JSONObject json = user.toJSON();
				
				//TODO need to know how to connect to node.js server for this
				//json = makeRequest("arcane-crag-4782.herokuapp.com", json);
				
				//json example
				/*if (json.has("memories")) {
					JSONArray memoriesJSON = json.getJSONArray("memories");
					
					for (int i = 0; i < memoriesJSON.length(); ++i) {
						
						JSONObject memoryJSON = memoriesJSON.getJSONObject(i);
						memories.add(new Memory(memoryJSON.getInt("id"),
						memoryJSON.getInt("year"),
						memoryJSON.getInt("month"),
						memoryJSON.getInt("day"),
						memoryJSON.getString("log")));
					
					}
			  }*/
				        		
				Intent i = new Intent(LoginActivity.this, WallActivity.class);     
				startActivity(i);
				finish(); //should use the finish if you need to preserve memory
				   //other wise don't use it.
				return false;
			}
	    });
	}

}
