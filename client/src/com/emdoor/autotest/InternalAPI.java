package com.emdoor.autotest;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.input.InputManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;

import android.app.SystemWriteManager; 

public class InternalAPI {


	public static void injectInputEvent(MotionEvent event) {
		 InputManager.getInstance().injectInputEvent(event,
		 InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
	}

	public static void injectInputEvent(KeyEvent event) {
		 InputManager.getInstance().injectInputEvent(event,
		 InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
	}

	public static Bitmap screenshot(int a, int b) {
		return Surface.screenshot(a,b);
	}

	public static void setProperty(Context context, String key, String value) {
		SystemWriteManager sw = (SystemWriteManager) context
				.getSystemService("system_write");

		sw.setProperty(key, value);
	}
	

}
