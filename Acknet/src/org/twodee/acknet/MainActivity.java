package org.twodee.acknet;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
<<<<<<< HEAD
import android.view.Menu;
import android.view.MotionEvent;
=======
import android.util.Log;
>>>>>>> Added request /login/, file timeline.xml
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
<<<<<<< HEAD
        final Button login = (Button) findViewById(R.id.login);
        
        login.setOnTouchListener(new OnTouchListener(){

        	@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
        		 Intent i = new Intent(MainActivity.this, LoginActivity.class);     
                 startActivity(i);
                 finish(); //should use the finish if you need to preserve memory
                           //other wise don't use it.
				return false;
			}
=======
        System.out.println("Hoooola");
        Log.i("info", "holaaaaaa");
        
        final Button signupButton = (Button) findViewById(R.id.signup);
        
        signupButton.setOnClickListener(new OnClickListener() {
	        
        	@Override
	        public void onClick(View view) {
	        	
        		Log.i("MainActivity", "holaaaaaa");
        		
	        	Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
	        	startActivityForResult(myIntent, 0);
	   
	        }
>>>>>>> Added request /login/, file timeline.xml
	    });
        
        final Button signup = (Button) findViewById(R.id.signUp);
        
        signup.setOnTouchListener(new OnTouchListener(){

        	@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
        		 Intent i = new Intent(MainActivity.this, SignUpActivity.class);     
                 startActivity(i);
                 finish(); //should use the finish if you need to preserve memory
                           //other wise don't use it.
				return false;
			}
	    });
    }
    
}
