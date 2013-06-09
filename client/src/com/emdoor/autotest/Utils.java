package com.emdoor.autotest;

import java.io.FileOutputStream;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class Utils {
	private static final String TAG = "Utils";

	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void closeAppByName(String name,Context context){
		
	}
	
	public static void launchAppByName(String name, Context context) {
		PackageManager pm = context.getApplicationContext().getPackageManager();

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final List<ResolveInfo> allApps = pm.queryIntentActivities(mainIntent,
				0);


		Intent intent = null;
		if (null != allApps && null != name) {
			for (ResolveInfo ri : allApps) {
				// 在本地已经安装应用中比较应用名称
				String labelString = ri.loadLabel(pm).toString();
				Log.d(TAG, "find app=" + labelString);
				if (name.equalsIgnoreCase(labelString)) {
					// intent = pm.get
					ComponentName className = new ComponentName(
							ri.activityInfo.applicationInfo.packageName,
							ri.activityInfo.name);
					intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_LAUNCHER);
					intent.setComponent(className);
					int launchFlags = Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;
					intent.setFlags(launchFlags);

					break;
				}
			}
		}
		if (intent != null) {
			context.startActivity(intent);
		}
	}

	@SuppressLint("NewApi")
	public static Bitmap takeScreenShot(Context context) {

		Matrix displayMatrix = new Matrix();

		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getRealMetrics(displayMetrics);
		display.getRealMetrics(displayMetrics);
		float[] dims = { displayMetrics.widthPixels,
				displayMetrics.heightPixels };
		float degrees = getDegreesForRotation(display.getRotation());
		boolean requiresRotation = (degrees > 0);
		if (requiresRotation) {
			// Get the dimensions of the device in its native orientation
			displayMatrix.reset();
			displayMatrix.preRotate(-degrees);
			displayMatrix.mapPoints(dims);
			dims[0] = Math.abs(dims[0]);
			dims[1] = Math.abs(dims[1]);
		}
		Bitmap screenBitmap = null;
		screenBitmap= Surface.screenshot((int) dims[0], (int) dims[1]);
		if (requiresRotation) {
			// Rotate the screenshot to the current orientation
			Bitmap ss = Bitmap.createBitmap(displayMetrics.widthPixels,
					displayMetrics.heightPixels, Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(ss);
			c.translate(ss.getWidth() / 2, ss.getHeight() / 2);
			c.rotate(degrees);
			c.translate(-dims[0] / 2, -dims[1] / 2);
			c.drawBitmap(screenBitmap, 0, 0, null);
			c.setBitmap(null);
			screenBitmap = ss;
		}

		// If we couldn't take the screenshot, notify the user
		if (screenBitmap == null) {

			return null;
		}

		// Optimizations
		screenBitmap.setHasAlpha(false);
		screenBitmap.prepareToDraw();

		return screenBitmap;
	}

	/**
	 * @return the current display rotation in degrees
	 */
	private static float getDegreesForRotation(int value) {
		switch (value) {
		case Surface.ROTATION_90:
			return 360f - 90f;
		case Surface.ROTATION_180:
			return 360f - 180f;
		case Surface.ROTATION_270:
			return 360f - 270f;
		}
		return 0f;
	}
}
