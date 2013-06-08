package com.emdoor.autotest;

import android.view.MotionEvent;
import android.view.KeyEvent;
//import android.view.IWindowManager;
//import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.RemoteException;
import android.util.Log;

public class EventHelper {

	public static void Touch(float x, float y) {
		MotionEvent e = MotionEvent.obtain(SystemClock.uptimeMillis(),
				SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0);
		sendPointerSync(e);
		e = MotionEvent.obtain(SystemClock.uptimeMillis(),
				SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0);
		sendPointerSync(e);
	}

	public static void Key(int keycode) {
		KeyEvent k = new KeyEvent(KeyEvent.ACTION_DOWN, keycode);
		sendKeySync(k);
		k = new KeyEvent(KeyEvent.ACTION_UP, keycode);
		sendKeySync(k);
	}

	public static void KeyPress(int keycode) {
		KeyEvent k = new KeyEvent(KeyEvent.ACTION_DOWN, keycode);
		sendKeySync(k);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		k = new KeyEvent(KeyEvent.ACTION_UP, keycode);
		sendKeySync(k);
	}

	public static void MotionTouchPress(float x, float y) {
		MotionEvent e = MotionEvent.obtain(SystemClock.uptimeMillis(),
				SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0);
		sendPointerSync(e);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {

			e1.printStackTrace();
		}
		e = MotionEvent.obtain(SystemClock.uptimeMillis(),
				SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0);
		sendPointerSync(e);
	}

	public static void MotionMove(float x1, float y1, float x2, float y2) {

		MotionEvent e = MotionEvent.obtain(SystemClock.uptimeMillis(),
				SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x1, y1, 0);
		sendPointerSync(e);
		e = MotionEvent.obtain(SystemClock.uptimeMillis(),
				SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, x1, y1, 0);
		sendPointerSync(e);
		e = MotionEvent.obtain(SystemClock.uptimeMillis(),
				SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, x1, y1, 0);
		sendPointerSync(e);
		e = MotionEvent.obtain(SystemClock.uptimeMillis(),
				SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, x2, y2, 0);
		sendPointerSync(e);
		e = MotionEvent.obtain(SystemClock.uptimeMillis(),
				SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, x2, y2, 0);
		sendPointerSync(e);
		e = MotionEvent.obtain(SystemClock.uptimeMillis(),
				SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x2, y2, 0);
		sendPointerSync(e);
	}

	private static void sendPointerSync(MotionEvent event) {
		/*
		try {
			(IWindowManager.Stub.asInterface(ServiceManager
					.getService("window"))).injectPointerEvent(event, true);
		} catch (RemoteException e) {
		}*/
	}

	private static void sendKeySync(KeyEvent event) {
		/*
		try {
			(IWindowManager.Stub.asInterface(ServiceManager
					.getService("window"))).injectKeyEvent(event, true);
		} catch (RemoteException e) {
		}*/
	}
}