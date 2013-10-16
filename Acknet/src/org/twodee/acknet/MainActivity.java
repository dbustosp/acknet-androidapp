package org.twodee.acknet;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final Button signupButton = (Button) findViewById(R.id.signup);
        
        signupButton.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View view) {
	        	
	        	Intent myIntent = new Intent(view.getContext(), LoginActivity.class); /** Class name here */
	        	startActivityForResult(myIntent, 0);
	        	
	        }
	    });
    }

    
}
