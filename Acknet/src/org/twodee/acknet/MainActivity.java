package org.twodee.acknet;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
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
	    });
    }

    
}
