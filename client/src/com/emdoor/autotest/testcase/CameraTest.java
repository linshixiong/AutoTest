package com.emdoor.autotest.testcase;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.emdoor.autotest.CameraView;
import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.Messages;
import com.emdoor.autotest.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraTest extends Activity {

	ImageView imageView;
	Button cameraOk;
	Button cameraFail;
	Button cameraBack;
	Configuration config;

	boolean isPreview = false;

	private int cameraType = 1;
	private boolean isBackCamera;
	private CameraView cameraView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);

		imageView = (ImageView) findViewById(R.id.photo);
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cameraView.setVisibility(View.VISIBLE);
				imageView.setVisibility(View.GONE);
			}
		});
		cameraOk = (Button) findViewById(R.id.btncamerabok);
		cameraFail = (Button) findViewById(R.id.btncamerafail);
		cameraBack = (Button) findViewById(R.id.btncameraback);
		cameraView = (CameraView) findViewById(R.id.camera_view);

		cameraView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cameraView.takePicture();
			}
		});

		config = new Configuration(this);

		cameraOk.setOnClickListener(listener);
		cameraFail.setOnClickListener(listener);
		cameraBack.setOnClickListener(listener);
		cameraType = getIntent().getIntExtra("camera_type", 1);
		isBackCamera = (cameraType == 1);
		cameraView.setCameraType(isBackCamera ? CameraInfo.CAMERA_FACING_BACK
				: CameraInfo.CAMERA_FACING_FRONT);
		cameraView.setHandler(handler);
		Toast.makeText(this, "Press the screen to take photo",
				Toast.LENGTH_LONG).show();

	}

	private final Button.OnClickListener listener = new Button.OnClickListener() {
		@Override
		public void onClick(View args0) {

			if (args0.getId() == R.id.btncamerabok) {
				if (isBackCamera) {
					config.setBackCameraTestOk(1);
				} else {
					config.setFrontCameraTestOk(1);
				}
			}
			if (args0.getId() == R.id.btncamerafail) {
				if (isBackCamera) {
					config.setBackCameraTestOk(2);
				} else {
					config.setFrontCameraTestOk(2);
				}
			}
			if (args0.getId() == R.id.btncameraback) {
				if (isBackCamera) {
					config.setBackCameraTestOk(0);
				} else {
					config.setFrontCameraTestOk(0);
				}
			}

			finish();

		}
	};

	@Override
	protected void onResume() {

		super.onResume();
	}

	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Messages.MSG_PHOTO_TAKEN:
				if(msg.obj!=null){
					byte[] data=(byte[])msg.obj;
					Bitmap bitmap=getPicFromBytes(data,null);
					imageView.setVisibility(View.VISIBLE);
					cameraView.setVisibility(View.GONE);
				
					imageView.setImageBitmap(bitmap);
					
				}
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
		
		
	};
	
	
	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

}