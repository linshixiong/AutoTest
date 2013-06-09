package com.emdoor.autotest;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback,
		Camera.PictureCallback {
	private static final String TAG = "CameraView";
	private SurfaceHolder holder;
	private Camera camera;
	private Handler handler;

	public CameraView(Activity context) {// 构造函数
		super(context);

		holder = getHolder();// 生成Surface Holder
		holder.addCallback(this);

		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 指定Push Buffer
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {// Surface生成事件的处理
		try {
			camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
			camera.setPreviewDisplay(holder);
		} catch (Exception e) {
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {// Surface改变事件的处理

		if (camera != null) {
			Camera.Parameters parameters = camera.getParameters();
			Log.d(TAG, "surfaceChanged width=" + width + ",height=" + height);
			// parameters.setPreviewSize(320, 240);
			parameters.setPictureSize(640, 480);
			camera.setParameters(parameters);// 设置参数
			camera.startPreview();// 开始预览
		}
	}

	public void takePicture(Handler handler) {
		this.handler = handler;
		camera.takePicture(null, null, this);
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {// 拍摄完成后保存照片
		Log.d(TAG, "onPictureTaken,data length=" + data.length);
		Commands.buffCameraPhoto = data;
		camera.setPreviewCallback(null);
		camera.stopPreview();
		camera.release();
		camera = null;
		if(handler!=null){
			 Message msg=new Message();
			 msg.what=Messages.MSG_PHOTO_TAKEN;
			 msg.obj=data;
			 handler.sendMessage(msg);
		}
		((Activity) getContext()).finish();
	
		// camera.startPreview();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}