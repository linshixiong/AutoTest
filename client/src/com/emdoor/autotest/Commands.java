package com.emdoor.autotest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.PowerManager;
import android.os.RecoverySystem;
import android.os.SystemClock;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.graphics.Bitmap;

public class Commands {

	public static final String CMD_GET_VERSION = "Test Version";

	public static final String CMD_GET_WIFI_INFO = "WIFI Level and WIFI Address";

	public static final String CMD_GET_BLE_INFO = "BLE Level and BLE Address";
	public static final String CMD_OPEN_BLE = "BLE Open";
	public static final String CMD_CLOSE_BLE = "BLE Close";

	public static final String CMD_SCREEN_RED = "Screen Red";
	public static final String CMD_SCREEN_GREEN = "Screen Green";
	public static final String CMD_SCREEN_BLUE = "Screen Blue";
	public static final String CMD_SCREEN_BLACK = "Screen Black";
	public static final String CMD_SCREEN_WITHE = "Screen White";
	public static final String CMD_SCREEN_NORMAL = "Screen Normal";

	public static final String CMD_SLEEP = "Android Sleep";
	public static final String CMD_WAKEUP = "Android Wakeup";
	public static final String CMD_SCREEN_OFF = "Screen Off";
	public static final String CMD_SCREEN_ON = "Screen On";

	public static final String CMD_TAKE_A_PICTURE = "Camera Catch";
	public static final String CMD_RECODE_AUDIO = "Record Audio";
	public static final String CMD_PLAY_AUDIO = "Play Audio";
	public static final String CMD_SET_VOLUME = "Volume=";

	public static final String CMD_GET_GSENSOR_COORDINATE = "3D XYZ";
	public static final String CMD_SD_WRITE = "SD Write";
	public static final String CMD_SET_TIME = "Time Setup";
	public static final String CMD_CHECK_FILE = "Check File";
	public static final String CMD_SN_WRITE = "SN Write";
	public static final String CMD_SN_READ = "SN Read";
	public static final String CMD_CLEAR_HISTORY = "Clear History";
	public static final String CMD_FACTORY_RESET = "Factory Reset";
	public static final String CMD_OPEN_APP = "Open App";
	public static final String CMD_CLOSE_APP = "Close App";
	public static final String CMD_MOTION_CLICK = "Click X=";
	public static final String CMD_MOTION_MOVE = "Move X1=";
	public static final String CMD_TAKE_SCREEN_SHOT = "PrtSc";
	public static final String CMD_TEST_END = "Test End";
	public static final String CMD_CHANGE_WIFI="SSID=";
	
	public static final HashMap<String, String> mapCmds = new HashMap<String, String>();

	private static final String TAG = "Commands";

	private final File SN_FILE = new File("/factory/sn.txt");

	private static Commands instance;
	private Context mContext;
	private AudioManager am;
	private PowerManager pm;
	private static String mSn;
	private static String mDataWrite;
	private SensorManager sensorMgr;
	private BleHelper bleHelper;
	private Sensor sensor;
	private float x;
	private float y;
	private float z;

	public static byte[] buffCameraPhoto;
	public static int deviceIndex;

	private Commands(Context context, Handler handler) {
		this.mContext = context;
		this.handler = handler;
		this.am = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		this.pm = (PowerManager) mContext
				.getSystemService(Context.POWER_SERVICE);
		this.bleHelper=new BleHelper(context);
		sensorMgr = (SensorManager) mContext
				.getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorMgr
				.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_GAME);
	}

	private SensorEventListener lsn = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent e) {
			x = e.values[SensorManager.DATA_X];
			y = e.values[SensorManager.DATA_Y];
			z = e.values[SensorManager.DATA_Z];
			// Log.d(TAG,"sensor:x="+x+",y="+y+",z="+z);
		}

		@Override
		public void onAccuracyChanged(Sensor s, int accuracy) {
		}
	};

	private Handler handler;

	private MediaRecorder mRecorder;

	private MediaPlayer mPlayer;

	public static Commands getInstance(Context context, Handler handler) {
		if (instance == null) {
			instance = new Commands(context, handler);
		}
		return instance;
	}

	public byte[] excute(String cmd) {
		cmd=cmd.trim();
		if (cmd.toUpperCase().startsWith(CMD_GET_VERSION.toUpperCase())) {
			return getVersion();
		} else if (cmd.toUpperCase()
				.startsWith(CMD_GET_WIFI_INFO.toUpperCase())) {
			return getWifiInfo();
		} else if (cmd.toUpperCase().startsWith(CMD_GET_BLE_INFO.toUpperCase())) {
			return getBleInfo();
		} else if (cmd.toUpperCase().startsWith(CMD_OPEN_BLE.toUpperCase())) {
			return enableBle(true);
		} else if (cmd.toUpperCase().startsWith(CMD_CLOSE_BLE.toUpperCase())) {
			return enableBle(false);
		} else if (cmd.toUpperCase().startsWith(CMD_SCREEN_RED.toUpperCase())) {
			return showBlankScreen(cmd, MainActivity.COLOR_RED, true);
		} else if (cmd.toUpperCase().startsWith(CMD_SCREEN_GREEN.toUpperCase())) {
			return showBlankScreen(cmd, MainActivity.COLOR_GREEN, true);
		} else if (cmd.toUpperCase().startsWith(CMD_SCREEN_BLUE.toUpperCase())) {
			return showBlankScreen(cmd, MainActivity.COLOR_BLUE, true);
		} else if (cmd.toUpperCase().startsWith(CMD_SCREEN_BLACK.toUpperCase())) {
			return showBlankScreen(cmd, MainActivity.COLOR_BLACK, true);
		} else if (cmd.toUpperCase().startsWith(CMD_SCREEN_WITHE.toUpperCase())) {
			return showBlankScreen(cmd, MainActivity.COLOR_WHITE, true);
		} else if (cmd.toUpperCase()
				.startsWith(CMD_SCREEN_NORMAL.toUpperCase())) {
			return showBlankScreen(cmd, MainActivity.COLOR_WHITE, false);
		} else if (cmd.toUpperCase().startsWith(
				CMD_TAKE_A_PICTURE.toUpperCase())) {
			return takePhoto(cmd);
		} else if (cmd.toUpperCase().startsWith(CMD_RECODE_AUDIO.toUpperCase())) {
			return recodAudio(cmd);
		} else if (cmd.toUpperCase().startsWith(CMD_PLAY_AUDIO.toUpperCase())) {
			return playAudio(cmd);
		} else if (cmd.toUpperCase().startsWith(CMD_SET_VOLUME.toUpperCase())) {
			return setVolume(cmd);
		} else if (cmd.toUpperCase().startsWith(
				CMD_GET_GSENSOR_COORDINATE.toUpperCase())) {
			return getGsensorCoordinate();
		}

		else if (cmd.toUpperCase().startsWith(CMD_SD_WRITE.toUpperCase())) {
			return writeFileToSdcard(cmd);
		} else if (cmd.toUpperCase().startsWith(CMD_SET_TIME.toUpperCase())) {
			return setTime(cmd);
		} else if (cmd.toUpperCase().startsWith(CMD_CHECK_FILE.toUpperCase())) {

		} else if (cmd.toUpperCase().startsWith(CMD_SN_WRITE.toUpperCase())) {
			return writeSN(cmd);
		} else if (cmd.toUpperCase().startsWith(CMD_SN_READ.toUpperCase())) {
			return readSN(cmd);
		} else if (cmd.toUpperCase()
				.startsWith(CMD_CLEAR_HISTORY.toUpperCase())) {
			return clearHistory(cmd);
		} else if (cmd.toUpperCase()
				.startsWith(CMD_FACTORY_RESET.toUpperCase())) {
			return factoryReset(cmd);
		} else if (cmd.toUpperCase().startsWith(CMD_TEST_END.toUpperCase())) {
			return testEnd(cmd);
		} else if (cmd.toUpperCase().startsWith(CMD_SLEEP.toUpperCase())) {
			return screenOff(cmd);
		}
		else if (cmd.toUpperCase().startsWith(CMD_WAKEUP.toUpperCase())) {
			return screenOn(cmd);
		}
		else if (cmd.toUpperCase().startsWith(CMD_OPEN_APP.toUpperCase())) {
			return openApp(cmd);
		} else if (cmd.toUpperCase().startsWith(CMD_CLOSE_APP.toUpperCase())) {
			return closeApp(cmd);
		}

		else if (cmd.toUpperCase().startsWith(CMD_MOTION_CLICK.toUpperCase())) {
			return motionClick(cmd);
		} else if (cmd.toUpperCase().startsWith(CMD_MOTION_MOVE.toUpperCase())) {
			return motionSwipe(cmd);
		} else if (cmd.toUpperCase().startsWith(
				CMD_TAKE_SCREEN_SHOT.toUpperCase())) {
			return takeScreenShot(cmd);
		}
		else if (cmd.toUpperCase().startsWith(
				CMD_CHANGE_WIFI.toUpperCase())) {
			return changeWifi(cmd);
		}
		
		return Utils.getResponeData(deviceIndex, "Unknown command\r\n");
	}

	private byte[] getVersion() {
		String version = "Test_Version=" + Utils.getVersion(mContext) + "\r\n";
		return Utils.getResponeData(deviceIndex, version);
	}

	private byte[] getWifiInfo() {
		String result = String.format("Level=%dDB and Address=%s\r\n",
				WifiHelper.getInstance(mContext).getWifiSignal(), WifiHelper
						.getInstance(mContext).getWifiMAC());
		return Utils.getResponeData(deviceIndex, result);
	}

	private byte[] getBleInfo() {
		String result = String.format("Level=%dDB and Address=%s\r\n", bleHelper.getBleRSSI("00:17:EA:8F:08:8A"),
				bleHelper.getBleMAC());
		return Utils.getResponeData(deviceIndex, result);
	}

	private byte[] enableBle(boolean enable) {

		bleHelper.enableBle(enable);
		String result = enable ? "BLE Open ERROR\r\n" : "BLE Close ERROR\r\n";
		return Utils.getResponeData(deviceIndex, result);
	}

	private byte[] showBlankScreen(String cmd, int color, boolean fullScreen) {
		Intent intent = new Intent(Intents.ACTION_FULLSCREEN_STATE_CHANGE);
		intent.putExtra("full_screen", fullScreen);
		intent.putExtra("background_color", color);
		mContext.sendBroadcast(intent);
		String result = cmd + " OK\r\n";
		return Utils.getResponeData(deviceIndex, result);
	}

	private byte[] getGsensorCoordinate() {

		String result = String.format("X=%f,Y=%f,Z=%f\r\n", x, y, z);
		return Utils.getResponeData(deviceIndex, result);
	}

	private byte[] setVolume(String cmd) {
		String volume = cmd.substring(cmd.lastIndexOf('=') + 1);
		String result = "";
		try {
			int iVolume = Integer.parseInt(volume);

			Log.d(TAG, "set Volume to " + iVolume);
			am.setStreamVolume(AudioManager.STREAM_MUSIC, iVolume,
					AudioManager.FLAG_SHOW_UI); // tempVolume:音量绝对值
			result = cmd + " OK\r\n";
		} catch (Exception ex) {
			result = cmd + " ERROR\r\n";
		}

		return Utils.getResponeData(deviceIndex, result);
	}

	private byte[] screenOn(String cmd) {
		String result = cmd + " OK\r\n";
		try {
			pm.wakeUp(SystemClock.uptimeMillis()+1);
		} catch (Exception ex) {
			ex.printStackTrace();
			result = cmd + " ERROR\r\n";
		}
		return Utils.getResponeData(deviceIndex, result);
	}

	private byte[] screenOff(String cmd) {
		String result = "";
		try {
			pm.goToSleep(SystemClock.uptimeMillis()+1);
		} catch (Exception ex) {
			ex.printStackTrace();
			result = cmd + " ERROR\r\n";
		}
		return Utils.getResponeData(deviceIndex, result);
	}

	private byte[] recodAudio(String cmd) {

		String result = "";

		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		File cacheDir = mContext.getCacheDir();
		File audioFileName = new File(cacheDir, "record.3gp");
		Log.d(TAG, "record audio to " + audioFileName.getAbsolutePath());
		mRecorder.setOutputFile(audioFileName.getAbsolutePath());
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
			mRecorder.start();
			Thread.sleep(5000);

			mRecorder.stop();
			result = "Record Audio OK\r\n";
		} catch (Exception e) {
			Log.e(TAG, "prepare() failed");
			result = "Record Audio ERROR\r\n";
		}
		return Utils.getResponeData(deviceIndex, result);
	}

	private byte[] playAudio(String cmd) {
		String result = "";
		File cacheDir = mContext.getCacheDir();
		File audioFileName = new File(cacheDir, "record.3gp");
		if (!audioFileName.exists()) {
			return (cmd + " ERROR\r\n").getBytes();
		}
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(audioFileName.getAbsolutePath());
			mPlayer.prepare();
			mPlayer.start();
			Thread.sleep(mPlayer.getDuration());

		} catch (Exception e) {
			Log.e(TAG, "prepare() failed");
			result = cmd + " ERROR\r\n";
		}
		result = cmd + " OK\r\n";
		return Utils.getResponeData(deviceIndex, result);

	}

	private byte[] takePhoto(String cmd) {

		Intent cameraTest = new Intent();
		cameraTest.setClass(mContext, CameraTestActivity.class);
		cameraTest.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		CameraTestActivity.handler = handler;
		mContext.startActivity(cameraTest);
		return null;
	}

	private byte[] takeScreenShot(String cmd) {
		try {
			Bitmap bm = Utils.takeScreenShot(mContext);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			return Utils.getResponeData(deviceIndex, baos.toByteArray());

		} catch (Exception e) {
			e.printStackTrace();
			return Utils.getResponeData(deviceIndex, cmd + " ERROR\r\n");
		}

	}

	private byte[] writeFileToSdcard(String cmd) {
		String result = "";
		if (!DeviceManager.getInstance(mContext).isExternalSDCardMounted()) {
			result = "SD Read=ERROR\r\n";
		} else {
			File file = new File("/storage/external_storage/sdcard1/.temp");
			mDataWrite = cmd.substring(cmd.lastIndexOf('=') + 1);
			if (file.exists()) {
				file.delete();
			}

			boolean success = Utils.writeTextToFile(file, mDataWrite);
			if (success == true) {
				mDataWrite = Utils.readTextFromFile(file);
				result = "SD Read=" + mDataWrite + "\r\n";
			} else {
				result = "SD Read=ERROR\r\n";
			}

		}
		return Utils.getResponeData(deviceIndex, result);
	}

	private byte[] writeSN(String cmd) {
		mSn = cmd.substring(cmd.lastIndexOf('=') + 1);
		boolean success = Utils.writeTextToFile(SN_FILE, mSn);
		if (success == true) {
			return Utils.getResponeData(deviceIndex, cmd + " OK\r\n");
		} else {
			return Utils.getResponeData(deviceIndex, cmd + " ERROR\r\n");

		}

	}

	private byte[] readSN(String cmd) {
		mSn = Utils.readTextFromFile(SN_FILE);

		String result = "SN Read=" + mSn + "\r\n";
		return Utils.getResponeData(deviceIndex, result);
	}

	private byte[] setTime(String cmd) {
		// boolean isSuc = SystemClock.setCurrentTimeMillis(0);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		String result = "Date Time=" + str + "\r\n";
		return Utils.getResponeData(deviceIndex, result);
	}

	private byte[] clearHistory(String cmd) {
		
		Settings.reset();
		File cacheDir=mContext.getCacheDir();
		Utils.delAllFile(cacheDir.getAbsolutePath());
		
		return Utils.getResponeData(deviceIndex, cmd  + " OK\r\n");
	}

	private byte[] factoryReset(String cmd) {
		try {
			RecoverySystem.rebootWipeUserData(mContext);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return Utils.getResponeData(deviceIndex, cmd + " OK\r\n");

	}

	private byte[] testEnd(String cmd) {
		return Utils.getResponeData(deviceIndex, cmd + " OK\r\n");
	}

	private byte[] openApp(String cmd) {
		String name = cmd.substring(cmd.lastIndexOf('=') + 1);
		Utils.launchAppByName(name, mContext);
		return Utils.getResponeData(deviceIndex, cmd + " OK\r\n");
	}

	private byte[] closeApp(String cmd) {
		// String name = cmd.substring(cmd.lastIndexOf('=') + 1);
		EventHelper.sendKeyEvent(KeyEvent.KEYCODE_HOME);
		return Utils.getResponeData(deviceIndex, cmd + " OK\r\n");
	}

	private byte[] motionClick(String cmd) {

		String xStr = cmd.substring(cmd.indexOf('=') + 1, cmd.indexOf(','));
		String yStr = cmd.substring(cmd.lastIndexOf('=') + 1);
		try {
			float x = Float.parseFloat(xStr);
			float y = Float.parseFloat(yStr);

			EventHelper.sendTap(InputDevice.SOURCE_TOUCHSCREEN, x, y);
			Log.d(TAG, "motionClick,x=" + x + ",y=" + y);
		} catch (Exception e) {
			return Utils.getResponeData(deviceIndex, cmd + " ERROR\r\n");
		}

		return Utils.getResponeData(deviceIndex, cmd + " OK\r\n");

	}

	private byte[] motionSwipe(String cmd) {
		cmd = cmd.toUpperCase();
		try {
			String x1Str = cmd.substring(cmd.indexOf("X1=") + 3,
					cmd.indexOf(",Y1"));
			String y1Str = cmd.substring(cmd.indexOf("Y1=") + 3,
					cmd.indexOf(",X2"));
			String x2Str = cmd.substring(cmd.indexOf("X2=") + 3,
					cmd.indexOf(",Y2"));
			String y2Str = cmd.substring(cmd.indexOf("Y2=") + 3);
			Log.d(TAG, "motionSwipe,x1=" + x1Str + ",y1=" + y1Str + ",x2="
					+ x2Str + ",y2=" + y2Str);
			float x1 = Float.parseFloat(x1Str);
			float y1 = Float.parseFloat(y1Str);
			float x2 = Float.parseFloat(x2Str);
			float y2 = Float.parseFloat(y2Str);

			EventHelper.sendSwipe(InputDevice.SOURCE_TOUCHSCREEN, x1, y1, x2,
					y2);
		} catch (Exception e) {
			e.printStackTrace();
			return Utils.getResponeData(deviceIndex, cmd + " ERROR\r\n");
		}
		return Utils.getResponeData(deviceIndex, cmd + " OK\r\n");
	}
	
	
	private byte[] changeWifi(String cmd){
		String ssid= cmd.substring(cmd.lastIndexOf('=') + 1);
		if(ssid!=null){
			ssid=ssid.replace("\"", "");
			Settings.setSSID(ssid);
		}
		return Utils.getResponeData(deviceIndex, cmd + " OK\r\n");
	}
}
