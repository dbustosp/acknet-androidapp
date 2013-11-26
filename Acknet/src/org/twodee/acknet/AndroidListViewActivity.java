package org.twodee.acknet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import com.cs491.acknet.R;

public class AndroidListViewActivity extends ListActivity {
	private static final int RESULT_LOAD_IMAGE = 1;
	 private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	    public static final int MEDIA_TYPE_IMAGE = 1;
	    public static final int MEDIA_TYPE_VIDEO = 2;
	    Uri picUri;
	    String imgPath;
	    ImageView imgUser;
	    
	    
	    private static final int CAMERA_IMAGE_CAPTURE = 0;
	    
	    
	    // directory name to store captured images and videos
	    private static final String IMAGE_DIRECTORY_NAME = "Acknet";
	    
	    private Uri fileUri; // file url to store image/video
	
	
	    private String selectedImagePath = "";
	    final private int PICK_IMAGE = 1;
	    final private int CAPTURE_IMAGE = 2;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
        // storing string resources into Array
        String[] adobe_products = getResources().getStringArray(R.array.adobe_products);
         
        // Binding resources Array to ListAdapter
        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.label, adobe_products));
         
        ListView lv = getListView();
 
        // listening to single list item on click
        lv.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
               
              // selected item 
              
        	  //String product = ((TextView) view).getText().toString();
               
              switch(position){
	              case 0: viewPictures(); break;
	              case 1: pickFromGallery(); break;
	              case 2: {
	            	  Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
	            	  fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
	            	  cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
	            	  startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	              }; 
	              break;
	              case 3: videos(); break;
	              }
              }
            	 
              
              
              
              // Launching new Activity on selecting single List Item
              //Intent i = new Intent(getApplicationContext(), SingleListItem.class);
              // sending data to new activity
              //i.putExtra("product", product);
              //startActivity(i);
        });
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		
		System.out.println("Init onActivityResult");
		
		String path = fileUri.getPath();
		putNewImage(path);
		
		System.out.println("path: " + path);
		
		
		
		
		System.out.println("End onActivityResult");
	}
	
	public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

	public String getAbsolutePath(Uri uri) {
        String[] projection = { MediaColumns.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
	
	
	private static Uri getOutputMediaFileUri(int type) {
	    return Uri.fromFile(getOutputMediaFile(type));
	}
	
	private static File getOutputMediaFile(int type){

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CrowdSnapCymru");

	    if(!mediaStorageDir.exists()){
	        if(! mediaStorageDir.mkdirs()){
	            Log.d("CrowdSnapCymru", "failed to create photo directory");
	            return null;
	        }
	    }

	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if(type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
	    }
	    else{
	        return null;
	    }

	    return mediaFile;

	}
 	    
 	    
	public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }


    public String getImagePath() {
        return imgPath;
    }
	
	private void pickFromGallery() {
		Intent i = new Intent(
		Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}
	
	private void captureImage() {
		
	}
	 	

	
	
	private void videos(){
		System.out.println("Videos");
		Intent myIntent = new Intent(getApplicationContext(), AndroidYoutube.class);
    	startActivityForResult(myIntent, 0);		
	}
	
	private void viewPictures() {
		// TODO Auto-generated method stub
		
	}
	
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null); 
        cursor.moveToFirst(); 
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
        return cursor.getString(idx); 
    }

	

	
	private void putNewImage(final String url){
		new AsyncTask<Void, Void, HttpResponse>() {

			@Override
			protected HttpResponse doInBackground(Void... params) {
				
				System.out.println("Init background doInBackground");
				
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
				
				SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				String username = SP.getString("username", "null");
				
				nameValuePairs.add(new BasicNameValuePair("image",image_str));
				nameValuePairs.add(new BasicNameValuePair("username",username));
				
				json = makeRequest(URL, nameValuePairs);			    
				
				return null;
			}
			
		}.execute();
	}
	
	private JSONObject makeRequest(String url, ArrayList<NameValuePair> nameValuePairs) {
		System.out.println("Init makeRequest");
		
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
		
		System.out.println("End makeRequest");		
		return null;
		
	}
	
	
}