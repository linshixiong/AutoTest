package com.emdoor.autotest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PreviewCallback;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback,
		Camera.PictureCallback {

	private static final String TAG = "CameraView";
	private SurfaceHolder holder;
	private Camera camera;
	private Handler handler;
	boolean auto;
	private int cameraType = CameraInfo.CAMERA_FACING_BACK;

	public CameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// this.handler = handler;
		holder = getHolder();// 生成Surface Holder
		holder.addCallback(this);
		auto = false;
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 指定Push Buffer
	}

	public CameraView(Activity context, Handler handler) {// 构造函数
		super(context);
		this.handler = handler;
		holder = getHolder();// 生成Surface Holder
		holder.addCallback(this);
		auto = true;
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 指定Push Buffer
	}

	public void setCameraType(int type) {
		this.cameraType = type;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {// Surface生成事件的处理
		try {
			camera = Camera.open(cameraType);
			camera.setPreviewDisplay(holder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {// Surface改变事件的处理
		if (camera != null) {
			Camera.Parameters parameters = camera.getParameters();
			Log.d(TAG, "surfaceChanged width=" + width + ",height=" + height);
			if (cameraType == CameraInfo.CAMERA_FACING_BACK) {
				parameters.setPictureSize(1600, 1200);
			} else {
				parameters.setPictureSize(640, 480);
			}
			camera.setDisplayOrientation(Surface.ROTATION_0);
			camera.enableShutterSound(true);
			camera.setParameters(parameters);// 设置参数
			camera.startPreview();// 开始预览

			if (auto) {
				handler.sendEmptyMessageDelayed(Messages.MSG_PHOTO_SHOT, 1000);
			}
		} else {
			Toast.makeText(getContext(), "camera open fail", Toast.LENGTH_SHORT)
					.show();
			if (handler != null) {
				Message msg = new Message();
				msg.what = Messages.MSG_PHOTO_TAKEN;
				handler.sendMessage(msg);
			}
			if (auto) {
				((Activity) getContext()).finish();
			}
		}
	}

	public void takePicture() {

		try {
			camera.takePicture(null, null, CameraView.this);
		} catch (Exception e) {
			if (handler != null) {
				Message msg = new Message();
				msg.what = Messages.MSG_PHOTO_TAKEN;
				handler.sendMessage(msg);
			}
			if (auto) {
				((Activity) getContext()).finish();

			}
		}

	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {// 拍摄完成后保存照片
		Log.d(TAG, "onPictureTaken,data length=" + data.length);
		if (handler != null) {
			Message msg = new Message();
			msg.obj = data;
			msg.what = Messages.MSG_PHOTO_TAKEN;
			handler.sendMessage(msg);
		}
		if(auto){
			((Activity) getContext()).finish();
		}
		else{
			camera.startPreview();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		if (camera != null) {
			camera.setPreviewCallback(null);
			camera.stopPreview();
			camera.release();
			camera = null;
		}
	}

}