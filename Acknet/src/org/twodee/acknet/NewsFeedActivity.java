package org.twodee.acknet;

import android.app.Activity;
import android.os.Bundle;
import com.cs491.acknet.R;
 
public class NewsFeedActivity extends Activity {
     /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_feed_layout);
    }
}