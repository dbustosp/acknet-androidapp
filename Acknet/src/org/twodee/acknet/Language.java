package org.twodee.acknet;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;

import com.cs491.acknet.R;

public class Language {
	
	String choose_your_language;
	
	
	public static void read_file_and_changelangiage(Context context, String language){
		
		System.out.println("Changing language to : " + language);
		
		
		

    }
		
		/*
		InputStream inputStream = context.getResources().openRawResource(R.raw.languages);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		int ctr;
		try {
		    ctr = inputStream.read();
		    while (ctr != -1) {
		        byteArrayOutputStream.write(ctr);
		        ctr = inputStream.read();
		    }
		    inputStream.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		

		
		try {
			
			// Parse the data into jsonobject to get original data in form of json.
		    JSONObject jObject = new JSONObject(byteArrayOutputStream.toString());
		    
		    JSONObject jObjectLanguages = jObject.getJSONObject("languages");
		    JSONObject jObject_specificLanguage = jObjectLanguages.getJSONObject(language);
		    
		    
		    Log.v("Text Data", jObject_specificLanguage.toString());
		    
		    
		    
		    //for(int i=0;i<jArray.length();i++){
		    	
		    	// Here it's going to be all the messages
		    //Log.v("Text Data", jArray.toString());
		    	
		    	
		    //}
			
			
			
		} catch (Exception e) {
		    e.printStackTrace();
		}*/
	
	
	public void set_strings(Context context){
		
		String choose_your_language_ = context.getString(R.string.choose_your_language);
		
		
		
	}
	

}







