package com.emdoor.autotest;

import java.io.BufferedReader;

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
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

	
	
	
	
	public static String getVersion(Context context) throws NameNotFoundException {

		PackageManager manager = context.getPackageManager();
		PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
		String version = info.versionName;
		return version;

	}

	public static boolean writeTextToFile(File file, String text) {
		try {		
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			OutputStream outstream = new FileOutputStream(file);
			OutputStreamWriter out = new OutputStreamWriter(outstream);
			out.write(text);
			out.close();
			return true;
		} catch (java.io.IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public static String readTextFromFile(File file) {

		String content = ""; // 文件内容字符串
		if (file.isDirectory()) {
			Log.d("TestFile", "The File doesn't not exist.");
		} else {
			try {
				InputStream instream = new FileInputStream(file);
				if (instream != null) {
					InputStreamReader inputreader = new InputStreamReader(
							instream);
					BufferedReader buffreader = new BufferedReader(inputreader);
					String line;
					// 分行读取
					while ((line = buffreader.readLine()) != null) {
						content += line;
					}
					instream.close();
				}
			} catch (java.io.FileNotFoundException e) {
				Log.d("TestFile", "The File doesn't not exist.");
			} catch (IOException e) {
				Log.d("TestFile", e.getMessage());
			}
		}
		return content;
	}

	// 删除文件夹
	// param folderPath 文件夹完整绝对路径

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	public static byte[] getResponeData(int index, int resultCode,
			String response) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(index);
		outputStream.write(0);
		outputStream.write(resultCode);
		try {
			outputStream.write(response.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}

	public static byte[] getResponeData(int index, int resultCode,
			byte[] response) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(index);
		outputStream.write(1);
		outputStream.write(resultCode);
		try {
			outputStream.write(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}

	public static void closeAppByName(String name, Context context) {

	}

	public static boolean launchAppByName(String name, Context context) {
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
			return true;
		} else {
			return false;
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
		screenBitmap = InternalAPI.screenshot((int) dims[0], (int) dims[1]);
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
