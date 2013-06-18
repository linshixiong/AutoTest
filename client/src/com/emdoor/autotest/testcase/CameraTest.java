package com.emdoor.autotest.testcase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CameraTest extends Activity{ 

		
		ImageView imageView;  
	    Button cameraOk;  
	    Button cameraFail; 
	    Button cameraBack; 
	    LinearLayout linerClick;
	    Configuration config;
	    private Bitmap myBitmap;
	    boolean isPreview = false;  	    
	    private byte[] mContent;
	    private String strImgPath = "";
	    private static final int RESULT_CAPTURE_IMAGE = 1;
	  
	    @Override  
	    public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.camera);  
	          
	        imageView = (ImageView)findViewById(R.id.photo);  
	        cameraOk = (Button) findViewById(R.id.btncamerabok);
	        cameraFail = (Button) findViewById(R.id.btncamerafail);
	        cameraBack = (Button) findViewById(R.id.btncameraback);
	        linerClick = (LinearLayout) findViewById(R.id.linerclick);
	        
	        linerClick.setOnClickListener(new OnClickListener() {	
	        	
				public void onClick(View v) {
					// TODO Auto-generated method stub
					StartCamera();
				}
			});
			
			config = new Configuration(this);
			
			cameraOk.setOnClickListener(listener);	
			cameraFail.setOnClickListener(listener);
			cameraBack.setOnClickListener(listener);

			
	        StartCamera(); 
	          
	    }
	    
	private final Button.OnClickListener listener = new Button.OnClickListener() {
		public void onClick(View args0) {

			/*
			 * FileOutputStream out; // declare a // file PrintStream p; //
			 * declare a print
			 * 
			 * try { out = new FileOutputStream("/tmp/lcd01.txt"); p = new
			 * PrintStream(out); p.println("OK"); p.close(); } catch (Exception
			 * e) { e.printStackTrace(); }
			 */
			if (args0.getId() == R.id.btncamerabok) {
				config.setCAMERATestOk(1);
			}
			if (args0.getId() == R.id.btncamerafail) {
				config.setCAMERATestOk(2);
			}
			if (args0.getId() == R.id.btncamerafail) {
				config.setCAMERATestOk(0);
			}

			finish();

		}
	};

	private void StartCamera() {
		// TODO Auto-generated method stub
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		startActivityForResult(intent,1);

	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ContentResolver contentResolver = getContentResolver();

		if (resultCode == RESULT_OK) {
			try {
				Bundle extras = data.getExtras();
				myBitmap = (Bitmap) extras.get("data");
				imageView.setImageBitmap(myBitmap);
				//imageView.setVisibility(View.VISIBLE);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {   
        if (bytes != null)   
            if (opts != null)   
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts);   
            else   
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);   
        return null;   
    }  
	
	 public static byte[] readStream(InputStream in) throws Exception{  
	     byte[] buffer  =new byte[1024];  
	     int len  =-1;  
	     ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
	       
	     while((len=in.read(buffer))!=-1){  
	         outStream.write(buffer, 0, len);  
	     }  
	     byte[] data  =outStream.toByteArray();  
	     outStream.close();  
	     in.close();  
	     return data;  
	 } 
}