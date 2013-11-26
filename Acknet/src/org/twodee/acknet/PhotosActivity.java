package org.twodee.acknet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;
import com.cs491.acknet.R;

public class PhotosActivity extends Activity {
	
	// Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
	
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Acknet";
    
    private Uri fileUri; // file url to store image/video
    
    private ImageView imgPreview;
    private VideoView videoPreview;
    private Button btnCapturePicture, btnRecordVideo;
    
	
	/** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.photos_layout);
       
       imgPreview = (ImageView) findViewById(R.id.imgPreview);
       videoPreview = (VideoView) findViewById(R.id.videoPreview);
       btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
       btnRecordVideo = (Button) findViewById(R.id.btnRecordVideo);
       
       
       /**
        * Capture image button click event
        * */
       btnCapturePicture.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               // capture picture
               captureImage();
           }
       });

       /**
        * Record video button click event
        */
       /*btnRecordVideo.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               // record video
               recordVideo();
           }
       });*/
       
   }
   
   /*
    * Capturing Camera Image will lauch camera app requrest image capture
    */
   private void captureImage() {
       Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
       intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
       // start the image capture Intent
       startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
   }
      
  
   
   /**
    * Creating file uri to store image/video
    */
   public Uri getOutputMediaFileUri(int type) {
       return Uri.fromFile(getOutputMediaFile(type));
   }
    
   
   
   
   
   /*
    * returning image / video
    */
   private static File getOutputMediaFile(int type) {
    
       // External sdcard location
       File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);
    
       // Create the storage directory if it does not exist
       if (!mediaStorageDir.exists()) {
           if (!mediaStorageDir.mkdirs()) {
               Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                       + IMAGE_DIRECTORY_NAME + " directory");
               return null;
           }
       }
    
       // Create a media file name
       String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
               Locale.getDefault()).format(new Date());
       File mediaFile;
       if (type == MEDIA_TYPE_IMAGE) {
           mediaFile = new File(mediaStorageDir.getPath() + File.separator
                   + "IMG_" + timeStamp + ".jpg");
       } else if (type == MEDIA_TYPE_VIDEO) {
           mediaFile = new File(mediaStorageDir.getPath() + File.separator
                   + "VID_" + timeStamp + ".mp4");
       } else {
           return null;
       }
    
       return mediaFile;
   }
   
	/**
	 * Receiving activity result method will be called after closing the camera
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if the result is capturing Image
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// successfully captured the image
				// display it in image view
				previewCapturedImage();
				
							
				
				
			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();
			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// video successfully recorded
				// preview the recorded video
				//previewVideo();
			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled recording
				Toast.makeText(getApplicationContext(),
						"User cancelled video recording", Toast.LENGTH_SHORT)
						.show();
			} else {
				// failed to record video
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to record video", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
	
	/*
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // hide video preview
            videoPreview.setVisibility(View.GONE);
 
            imgPreview.setVisibility(View.VISIBLE);
 
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
 
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;
 
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
 
            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
   
   
   
}
