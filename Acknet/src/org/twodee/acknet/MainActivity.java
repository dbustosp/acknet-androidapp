package org.twodee.acknet;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
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
	    });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
