package org.twodee.acknet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	InputStream inputStream;
	private Bitmap bitmap;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        System.out.println("Hoooola");
        Log.i("info", "holaaaaaa");
        
        final Button signupButton = (Button) findViewById(R.id.signup);
        final Button loginButton = (Button) findViewById(R.id.login);
        final Button pickPhoto = (Button) findViewById(R.id.pick_photo);
        
        pickPhoto.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(pickPhoto , 1);
			}
        	
        });
        
        
        loginButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
				imagesFolder.mkdirs(); // <----
				File image = new File(imagesFolder, "image_001.jpg");
				Uri uriSavedImage = Uri.fromFile(image);
				imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
				startActivityForResult(imageIntent,0);
				
			}
        	
        });
        
        
        signupButton.setOnClickListener(new OnClickListener() {
	        
        	@Override
	        public void onClick(View view) {
	        	
        		Log.i("MainActivity", "holaaaaaa");
        		
	        	Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
	        	startActivityForResult(myIntent, 0);
	   
	        }
	    });
    }
    

    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
    	    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
    	    
    	    ImageView imageview = new ImageView(MainActivity.this);
    	    
    	    System.out.println("onActivityResult Init ");
    	    //System.out.println("requesCode: " + requestCode);
    	    switch(requestCode) {
	    	    case 0:
	    	        if(resultCode == RESULT_OK){  
	    	            Uri selectedImage = imageReturnedIntent.getData();
	    	            
	    	            System.out.println("Case 0 RESULT_OK:" + selectedImage);
	    	            
	    	            //imageview.setImageURI(selectedImage);
	    	            
	    	            //System.out.println(selectedImage);
	    	            
	    	        }
	
	    	    break; 
	    	    case 1:
	    	        if(resultCode == RESULT_OK){  
	    	        	
	    	        	System.out.println("esultCode == RESULT_OK case 1");

	    	        	Uri selectedImageUri = imageReturnedIntent.getData();
						System.out.println("selectedImageUri: " + selectedImageUri);
	    	        	
	    	        	 	
	    	        	// CALL THIS METHOD TO GET THE ACTUAL PATH
	    	            //File finalFile = new File(getRealPathFromURI(tempUri));
						System.out.println("path: " + getRealPathFromURI(selectedImageUri));

	    	        	
	    	        
	    	        	
	    	            
						putNewImage(getRealPathFromURI(selectedImageUri));
	    	        	
	    	            //imageview.setImageURI(selectedImage);
	    	            //System.out.println(selectedImage);
	    	            
	    	        }
	    	    break;
    	    }
    	    
    	    
    	    
    	    
    	    System.out.println("onActivityResult End ");
    	}
    
    
    
    
    	private void putNewImage(final String url){
    		new AsyncTask<Void, Void, HttpResponse>() {

				@Override
				protected HttpResponse doInBackground(Void... params) {
					
					
					//Implementation send request post to 
					String IP = Connection.getInstance().getIp();
					String URL = IP + "/upload_image";
				    HttpResponse response = null;
				    
				    
				    JSONObject json = new JSONObject();
				    
				    
				    Bitmap bitmap = BitmapFactory.decodeFile(url);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream); //compress to which format 
					byte [] byte_arr = stream.toByteArray();
					String image_str = Base644.encodeBytes(byte_arr);
					ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("image",image_str));

					System.out.println("Making the request...");
					
					json = makeRequest(URL, nameValuePairs);
					
					System.out.println("JSOOOOON: " + json);
					
					
					
					
					return null;
				}
				
				@Override
				protected void onPostExecute(HttpResponse response){
					System.out.println("onPostExecute - putNewImage");
					
					
					
					
				}
    			
    		}.execute();
    	}
    
    	private JSONObject makeRequest(String url, ArrayList<NameValuePair> nameValuePairs) {
    		
    		try{
                HttpClient httpclient = new DefaultHttpClient();
                
                HttpPost httppost = new HttpPost(Connection.getInstance().getIp() + "/upload_image");
                
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                
                HttpResponse response = httpclient.execute(httppost);
                
                Scanner in = new Scanner(response.getEntity().getContent());
    		    in.useDelimiter("\\Z");
    		    String body = in.next();
    		    in.close();
    		      
    		    Log.d("makeRequest - FOOOOO", body);
                
                System.out.println("body: " + body);
 
                return new JSONObject(body);
            
    		}catch(Exception e){
    			e.printStackTrace();
            }    		
    		    
    		    
    			

    		return null;
    	}
    	
        public Uri getImageUri(Context inContext, Bitmap inImage) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }

        public String getRealPathFromURI(Uri uri) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null); 
            cursor.moveToFirst(); 
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
            return cursor.getString(idx); 
        }
  
}
