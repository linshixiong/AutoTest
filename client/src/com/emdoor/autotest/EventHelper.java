package com.emdoor.autotest;

import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.view.IWindowManager;
import android.os.ServiceManager;
import android.hardware.input.InputManager;
import android.os.SystemClock;
import android.os.RemoteException;
import android.util.Log;

public class EventHelper {

	private static final String TAG = "EventHelper";

	public static void Touch(float x, float y) {
		
	}

	public static void sendText(String text) {

		StringBuffer buff = new StringBuffer(text);

		boolean escapeFlag = false;
		for (int i = 0; i < buff.length(); i++) {
			if (escapeFlag) {
				escapeFlag = false;
				if (buff.charAt(i) == 's') {
					buff.setCharAt(i, ' ');
					buff.deleteCharAt(--i);
				}
			}
			if (buff.charAt(i) == '%') {
				escapeFlag = true;
			}
		}

		char[] chars = buff.toString().toCharArray();

		KeyCharacterMap kcm = KeyCharacterMap
				.load(KeyCharacterMap.VIRTUAL_KEYBOARD);
		KeyEvent[] events = kcm.getEvents(chars);
		for (int i = 0; i < events.length; i++) {
			injectKeyEvent(events[i]);
		}
	}

	public static void sendKeyEvent(int keyCode) {
		long now = SystemClock.uptimeMillis();
		injectKeyEvent(new KeyEvent(now, now, KeyEvent.ACTION_DOWN, keyCode, 0,
				0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0, 0,
				InputDevice.SOURCE_KEYBOARD));
		injectKeyEvent(new KeyEvent(now, now, KeyEvent.ACTION_UP, keyCode, 0,
				0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0, 0,
				InputDevice.SOURCE_KEYBOARD));
	}

	public static void sendTap(int inputSource, float x, float y) {
		long now = SystemClock.uptimeMillis();
		injectMotionEvent(inputSource, MotionEvent.ACTION_DOWN, now, x, y, 1.0f);
		injectMotionEvent(inputSource, MotionEvent.ACTION_UP, now, x, y, 0.0f);
	}

	public static void sendSwipe(int inputSource, float x1, float y1, float x2,
			float y2) {
		final int NUM_STEPS = 11;
		long now = SystemClock.uptimeMillis();
		injectMotionEvent(inputSource, MotionEvent.ACTION_DOWN, now, x1, y1,
				1.0f);
		for (int i = 1; i < NUM_STEPS; i++) {
			float alpha = (float) i / NUM_STEPS;
			injectMotionEvent(inputSource, MotionEvent.ACTION_MOVE, now,
					lerp(x1, x2, alpha), lerp(y1, y2, alpha), 1.0f);
		}
		injectMotionEvent(inputSource, MotionEvent.ACTION_UP, now, x1, y1, 0.0f);
	}

	/**
	 * Sends a simple zero-pressure move event.
	 * 
	 * @param inputSource
	 *            the InputDevice.SOURCE_* sending the input event
	 * @param dx
	 *            change in x coordinate due to move
	 * @param dy
	 *            change in y coordinate due to move
	 */
	public static void sendMove(int inputSource, float dx, float dy) {
		long now = SystemClock.uptimeMillis();
		injectMotionEvent(inputSource, MotionEvent.ACTION_MOVE, now, dx, dy,
				0.0f);
	}

	private static void injectKeyEvent(KeyEvent event) {
		Log.i(TAG, "injectKeyEvent: " + event);
		InputManager.getInstance().injectInputEvent(event,
				InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
	}

	/**
	 * Builds a MotionEvent and injects it into the event stream.
	 * 
	 * @param inputSource
	 *            the InputDevice.SOURCE_* sending the input event
	 * @param action
	 *            the MotionEvent.ACTION_* for the event
	 * @param when
	 *            the value of SystemClock.uptimeMillis() at which the event
	 *            happened
	 * @param x
	 *            x coordinate of event
	 * @param y
	 *            y coordinate of event
	 * @param pressure
	 *            pressure of event
	 */
	private static void injectMotionEvent(int inputSource, int action, long when,
			float x, float y, float pressure) {
		final float DEFAULT_SIZE = 1.0f;
		final int DEFAULT_META_STATE = 0;
		final float DEFAULT_PRECISION_X = 1.0f;
		final float DEFAULT_PRECISION_Y = 1.0f;
		final int DEFAULT_DEVICE_ID = 0;
		final int DEFAULT_EDGE_FLAGS = 0;
		MotionEvent event = MotionEvent.obtain(when, when, action, x, y,
				pressure, DEFAULT_SIZE, DEFAULT_META_STATE,
				DEFAULT_PRECISION_X, DEFAULT_PRECISION_Y, DEFAULT_DEVICE_ID,
				DEFAULT_EDGE_FLAGS);
		event.setSource(inputSource);
		Log.i("Input", "injectMotionEvent: " + event);
		InputManager.getInstance().injectInputEvent(event,
				InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
	}

	private static final float lerp(float a, float b, float alpha) {
		return (b - a) * alpha + a;
	}

}