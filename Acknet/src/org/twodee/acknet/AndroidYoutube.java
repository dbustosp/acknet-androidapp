package org.twodee.acknet;


import android.os.Bundle;
import android.widget.Toast;

import com.cs491.acknet.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class AndroidYoutube extends YouTubeBaseActivity implements
YouTubePlayer.OnInitializedListener{

	
	public static final String API_KEY = "AIzaSyCcdRKwLTXED-Lq27t94kI3kPrqmL-c1hk";
	
	public static final String VIDEO_ID = "liqVxEbl6GQ";
	

	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_player_view);
        
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtubeplayerview);
        youTubePlayerView.initialize(API_KEY, this);
    }
	
	
	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		
			
		 Toast.makeText(getApplicationContext(), 
				    "onInitializationFailure()", 
				    Toast.LENGTH_LONG).show();
		
		
		
	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer arg1,
			boolean arg2) {
		
			
		if (!arg2) {
			arg1.cueVideo(VIDEO_ID);
	      }
		
		
		
	}

}
