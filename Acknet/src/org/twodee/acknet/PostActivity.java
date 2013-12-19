package org.twodee.acknet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cs491.acknet.R;

public class PostActivity extends Activity {
	String text_post;
	String IP = Connection.getInstance().getIp();
	String URL = IP + "/story";
	String username;
	SharedPreferences SP;
	JSONObject json;
	
	Boolean status;
	
	private ProgressDialog Dialog;
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int RESULT_LOAD_IMAGE = 1;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	
	private Uri fileUri; // file url to store image/video
	
	String real_path;
	String real_video;
	String body;
	
	
	public static final int POST_IMAGE = 0;
	public static final int POST_VIDEO = 1;
	
	public static final int FROM_GALLERY = 0;
	public static final int FROM_CAMERA = 1;
	
	public Boolean camera = false;
	
	
	
	public int KIND_OF_POST = 100;
	
	
	
	int kind_post;
	
	
	final static int SUCCESS_PICK_PICTURE = 1;	
	
	/** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.layout_posts);
       
       final Button btn_library = (Button) findViewById(R.id.btn_library);
       final Button btn_camera = (Button) findViewById(R.id.btn_camera);
       final Button btn_video = (Button) findViewById(R.id.btn_video);
       final Button btn_send_post = (Button) findViewById (R.id.send_post);
       
       Dialog = new ProgressDialog(PostActivity.this);
       
       btn_send_post.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			
			//Implementation
			EditText edit_body = (EditText)findViewById(R.id.editText1);
			body = edit_body.getText().toString();
			putNewPost(arg0);		
		}
    	   
       });
       
       btn_library.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				//Implementation choose picture from library
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				//fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
          	  	
          	  	//i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
          	  	startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);          	  	
			}
       });
       
       btn_camera.setOnClickListener(new View.OnClickListener() {
    	   
	   		@Override
	   		public void onClick(View arg0) {
	   			//Implementation choose picture from camera
	   			camera = true;
	   			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
	          	fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
	          	cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
	          	startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	   		}
        });
       
       
       btn_video.setOnClickListener(new View.OnClickListener() {
    	   
			@Override
			public void onClick(View arg0) {
				
				final EditText input = new EditText(PostActivity.this);
				final AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
				input.setInputType(InputType.TYPE_CLASS_TEXT);
				
				
				
				//builder.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				//builder.setView(input);
				
				//Implementation record video
				builder.setTitle("Put video link please").setMessage("You need put link from Youtube. Thanks. ")
		        	.setCancelable(false)
		        	.setView(input)
		        	.setNegativeButton("Send",new DialogInterface.OnClickListener() {
		        		public void onClick(DialogInterface dialog, int id) {
		        		
		        			// Save link in String in a variable class
		        			real_video = input.getText().toString();
		        			TextView info = (TextView) findViewById(R.id.info_attach);
		        			info.setText("You have attachment video now: " + real_video);
		        			KIND_OF_POST = 1;
		        		}		      		        					
		        	});
					AlertDialog alert = builder.create();
			        alert.show();	
			}
      });
   }
   
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(camera){
			//Picture from camera
			String path = fileUri.getPath();
			real_path = path;
		}else{
			// Picture from gallery
			System.out.println("from Gallery");
			Uri selectedImage = data.getData();
			System.out.println("selectedImage: " + selectedImage);
			real_path = getRealPathFromURI3(PostActivity.this, selectedImage);
			System.out.println("real Path: " + getRealPathFromURI3(PostActivity.this, selectedImage));
		}
		
		
		if(real_path == null){
			// Alert and tell user he shoul choose a image from his gallery
			AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
	        builder.setTitle("Something is wrong with your image.")
	        .setMessage("You need choose an image from the gallery.")
	        .setCancelable(false)
	        .setNegativeButton("OK",new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                dialog.cancel();
	            }
	        });
	        AlertDialog alert = builder.create();
	        alert.show();	
		}
		
		System.out.println("Pasando onActivity Result! ");
		TextView info = (TextView) findViewById(R.id.info_attach);
		info.setText("You have attachment file now: " + real_path);
		KIND_OF_POST = 0;	
	}
   
   public String getRealPathFromURI(Uri uri) {
       Cursor cursor = getContentResolver().query(uri, null, null, null, null); 
       cursor.moveToFirst(); 
       int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
       return cursor.getString(idx); 
   }
   
   
   
   
   public String getRealPathFromURI3(Context context, Uri contentUri) {
	   Cursor cursor = null;
	   try { 
	     String[] proj = { MediaStore.Images.Media.DATA };
	     cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
	     int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	     cursor.moveToFirst();
	     return cursor.getString(column_index);
	   } finally {
	     if (cursor != null) {
	       cursor.close();
	     }
	   }
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
   
   private void putNewPost(final View v) {		
		
		new AsyncTask<Void, Void, HttpResponse>() {

			@Override
			protected HttpResponse doInBackground(Void... params) {
				json = new JSONObject();
				try{
					
					SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
					username = SP.getString("username", "null");
					
					// Push in json username and body
					json.put("username",username);
					json.put("body", body);
					
					JSONObject attachment = new JSONObject();
					// Create json for attachment
					if(KIND_OF_POST == 0){
						
						// Kind post image
						// Code the image
						System.out.println("Readl_path: " + real_path);
						
						if(real_path != null){
							Bitmap bitmap = BitmapFactory.decodeFile(real_path);
							ByteArrayOutputStream stream = new ByteArrayOutputStream();
							bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream); //compress to which format 
							byte [] byte_arr = stream.toByteArray();
							String image_str = Base644.encodeBytes(byte_arr);
							attachment.put("source", image_str);
						}else{
							attachment.put("source", "");
						}						
						attachment.put("type","image");
						attachment.put("link", "");
					}else if (KIND_OF_POST == 1){
						// Kind post video
						attachment.put("type","video");
						attachment.put("source", "");
						attachment.put("link", real_video);
					}else{
						// Kind post only text
						attachment.put("source", "");
						attachment.put("type","text");
						attachment.put("link", "");
					}
					json.put("attachment", attachment);
					
					JSONObject geolocation = new JSONObject();
					geolocation.put("lat", "0");
					geolocation.put("lon", "0");
					geolocation.put("alt", "0");
					json.put("geolocation",geolocation);
							
					
					// Now I have json almost done ==> link image

					json = makeRequest(URL, json);
					
					
					
					
				}catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			}
			
			@Override
			protected void onPreExecute()
		    {
				String message = getString(R.string.posting_post);
		        Dialog.setMessage(message);
		        Dialog.show();
		    }
			
			@Override
			protected void onPostExecute(HttpResponse response){
				Dialog.dismiss();				
				try {
					Boolean status = json.getBoolean("success");
					if(status){
						AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
				        builder.setTitle("Image upload succesfully.")
				        
				        .setMessage("Message for success!.")
				        .setCancelable(false)
				        .setNegativeButton("OK",new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int id) {
				            	Intent i = new Intent(PostActivity.this, TimelineActivity.class);
								startActivity(i);
				            }
				        });
				        AlertDialog alert = builder.create();
				        alert.show();
					}else{
						AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
				        builder.setTitle("Image not upload!.")
				        
				        .setMessage("Message not success!.")
				        .setCancelable(false)
				        .setNegativeButton("OK",new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
				            }
				        });
				        AlertDialog alert = builder.create();
				        alert.show();	
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.execute();
   }
   
   private JSONObject makeRequest(String url, JSONObject json) {
	   
	   try{
		URI uri = new URI(String.format(url));
		HttpPost post = new HttpPost(uri);
		StringEntity entity = new StringEntity(json.toString());
		
		entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	    post.setEntity(entity);
	    
	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response = client.execute(post);
	    
	    Scanner in = new Scanner(response.getEntity().getContent());
	    in.useDelimiter("\\Z");
	    String body = in.next();
	    in.close();
	    
	    return new JSONObject(body);
	    
		
	   }catch (URISyntaxException e) {
		      e.printStackTrace();
	   } catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   } catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   } catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
		
	   
	   
	   return json;	   
   }
   
   
   
   
   
}
