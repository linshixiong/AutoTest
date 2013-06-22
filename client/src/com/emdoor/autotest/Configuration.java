package com.emdoor.autotest;

import java.io.File;
import java.io.StringWriter;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Xml;

public class Configuration {
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	private Context mContext;
	private boolean semifinished;
	private static final String KEY_LCD_STATE = "lcd";
	private static final String KEY_LCD_DEF = "lcd_def";
	private static final String KEY_TOUCH_STATE = "touch";
	private static final String KEY_TOUCH_DEF = "touch_def";
	private static final String KEY_WIFI_STATE = "wifi";
	private static final String KEY_WIFI_DEF = "wifi_def";
	private static final String KEY_BLUETOOTH_STATE = "bluetooth";
	private static final String KEY_BLUETOOTH_DEF = "bluetooth_def";
	private static final String KEY_GPS_STATE = "gps";
	private static final String KEY_GPS_DEF = "gps_def";
	private static final String KEY_EVDO_STATE = "evdo";
	private static final String KEY_EVDO_DEF = "evdo_def";
	private static final String KEY_USB_STATE = "usb";
	private static final String KEY_USB_DEF = "usb_def";
	private static final String KEY_BACK_CAMERA_STATE = "back_camera";
	private static final String KEY_BACK_CAMERA_DEF = "back_camera_def";
	private static final String KEY_FRONT_CAMERA_STATE = "front_camera";
	private static final String KEY_FRONT_CAMERA_DEF = "front_camera_def";

	private static final String KEY_GSENSOR_STATE = "gsensor";
	private static final String KEY_GSENSOR_DEF = "gsensor_def";
	private static final String KEY_LIGHT_STATE = "lightsensor";
	private static final String KEY_LIGHT_DEF = "light_def";
	private static final String KEY_SPEAKER_STATE = "speaker";
	private static final String KEY_SPEAKER_DEF = "speaker_def";
	private static final String KEY_HEADSET_STATE = "headset";
	private static final String KEY_HEADSET_DEF = "headset_def";
	private static final String KEY_MIC_STATE = "mic";
	private static final String KEY_MIC_DEF = "mic_def";
	private static final String KEY_EXT_MIC_STATE = "ext_mic";
	private static final String KEY_EXT_MIC_DEF = "ext_mic_def";
	private static final String KEY_TFCARD_STATE = "tfcard";
	private static final String KEY_TFCARD_DEF = "tfcard_def";
	private static final String KEY_HDMI_STATE = "hdmi";
	private static final String KEY_HDMI_DEF = "hdmi_def";
	private static final String KEY_BATTERY_STATE = "battery";
	private static final String KEY_BATTERY_DEF = "battery_def";
	private static final String KEY_KEYBOARD_STATE = "keyboard";
	private static final String KEY_KEYBOARD_DEF = "keyboard_def";
	private static final String KEY_VERSION_STATE = "version";
	private static final String KEY_VERSION_DEF = "version_def";
	private static final String KEY_BRIGHTNESS_STATE = "brightness";
	private static final String KEY_BRIGHTNESS_DEF = "brightness_def";

	private static final File FILE_RESULT=new File("/sdcard/self_test/self_test_result.xml");
	// private static final String KEY_CAMERA_STATE = null;

	public Configuration(Context context, boolean semifinished) {
		this.mContext = context;
		settings = mContext.getSharedPreferences("emdoor_selftest",
				Context.MODE_PRIVATE);
		editor = settings.edit();
		this.semifinished = semifinished;
	}

	public Configuration(Context context) {
		this.mContext = context;
		settings = mContext.getSharedPreferences("emdoor_selftest",
				Context.MODE_PRIVATE);
		editor = settings.edit();
	}

	public void saveResultFile() {
		createXmlFile(FILE_RESULT);
	}

	private void createXmlFile(File file) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("utf-8", null);

			serializer.startTag("", "Results");

			serializer.startTag("", "item");
			serializer.attribute("", "name", "lcd");
			serializer.attribute("", "result",String.valueOf(isLCDTestOk()));
			serializer.endTag("", "item");
			
			serializer.startTag("", "item");
			serializer.attribute("", "name", "touch");
			serializer.attribute("", "result",String.valueOf(isTOUCHTestOk()));
			serializer.endTag("", "item");
			
			serializer.startTag("", "item");
			serializer.attribute("", "name", "wifi");
			serializer.attribute("", "result",String.valueOf(isWIFITestOk()));
			serializer.endTag("", "item");
			
			serializer.startTag("", "item");
			serializer.attribute("", "name", "bluetooth");
			serializer.attribute("", "result",String.valueOf(isBLUETOOTHTestOk()));
			serializer.endTag("", "item");
			
			serializer.startTag("", "item");
			serializer.attribute("", "name", "usb_storage");
			serializer.attribute("", "result",String.valueOf(isUSBTestOk()));
			serializer.endTag("", "item");
			
			serializer.startTag("", "item");
			serializer.attribute("", "name", "front_camera");
			serializer.attribute("", "result",String.valueOf(isFrontCameraTestOk()));
			serializer.endTag("", "item");
			
			serializer.startTag("", "item");
			serializer.attribute("", "name", "back_camera");
			serializer.attribute("", "result",String.valueOf(isBackCameraTestOk()));
			serializer.endTag("", "item");
			
			serializer.startTag("", "item");
			serializer.attribute("", "name", "gsensor");
			serializer.attribute("", "result",String.valueOf(isGSENSORTestOk()));
			serializer.endTag("", "item");
			
			serializer.startTag("", "item");
			serializer.attribute("", "name", "speaker");
			serializer.attribute("", "result",String.valueOf(isSPEAKERTestOk()));
			serializer.endTag("", "item");
			
			serializer.startTag("", "item");
			serializer.attribute("", "name", "headset");
			serializer.attribute("", "result",String.valueOf(isHeadsetTestOk()));
			serializer.endTag("", "item");
			
			serializer.startTag("", "item");
			serializer.attribute("", "name", "onboard_mic");
			serializer.attribute("", "result",String.valueOf(isMICTestOk()));
			serializer.endTag("", "item");
			
			serializer.startTag("", "item");
			serializer.attribute("", "name", "external_mic");
			serializer.attribute("", "result",String.valueOf(isExtMICTestOk()));
			serializer.endTag("", "item");
			
			serializer.startTag("", "item");
			serializer.attribute("", "name", "tfcard");
			serializer.attribute("", "result",String.valueOf(isTFCARDTestOk()));
			serializer.endTag("", "item");
			
			serializer.startTag("", "item");
			serializer.attribute("", "name", "hdmi");
			serializer.attribute("", "result",String.valueOf(isHDMITestOk()));
			serializer.endTag("", "item");
			
			
			serializer.startTag("", "item");
			serializer.attribute("", "name", "charging");
			serializer.attribute("", "result",String.valueOf(isBATTERYTestOk()));
			serializer.endTag("", "item");
			
			serializer.startTag("", "item");
			serializer.attribute("", "name", "brightness");
			serializer.attribute("", "result",String.valueOf(isBRIGHTNESSTestOk()));
			serializer.endTag("", "item");
			
			serializer.endTag("", "Results");
			serializer.endDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Utils.writeTextToFile(file, writer.toString());
	}

	public int isLCDTestOk() {
		return settings.getInt(KEY_LCD_STATE, 0);
	}

	public void setLCDTestOk(int enable) {
		editor.putInt(KEY_LCD_STATE, enable);
		editor.commit();
	}

	public Boolean isLCDDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_semifinished_self_test_lcd) : mContext
				.getResources().getBoolean(R.bool.def_finished_self_test_lcd);
		// settings.getBoolean(KEY_LCD_DEF,true);
	}

	public void setLCDDef(Boolean enable) {
		editor.putBoolean(KEY_LCD_DEF, enable);
		editor.commit();
	}

	public int isTOUCHTestOk() {
		return settings.getInt(KEY_TOUCH_STATE, 0);
	}

	public void setTOUCHTestOk(int enable) {
		editor.putInt(KEY_TOUCH_STATE, enable);
		editor.commit();
	}

	public Boolean isTOUCHDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_semifinished_self_test_touch) : mContext
				.getResources().getBoolean(R.bool.def_finished_self_test_touch);
		// return settings.getBoolean(KEY_TOUCH_DEF, true);
	}

	public void setTOUCHDef(Boolean enable) {
		editor.putBoolean(KEY_TOUCH_DEF, enable);
		editor.commit();
	}

	public int isWIFITestOk() {
		return settings.getInt(KEY_WIFI_STATE, 0);
	}

	public void setWIFITestOk(int enable) {
		editor.putInt(KEY_WIFI_STATE, enable);
		editor.commit();
	}

	public Boolean isWIFIDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_semifinished_self_test_wifi) : mContext
				.getResources().getBoolean(R.bool.def_finished_self_test_wifi);
		// return settings.getBoolean(KEY_WIFI_DEF, true);
	}

	public void setWIFIDef(Boolean enable) {
		editor.putBoolean(KEY_WIFI_DEF, enable);
		editor.commit();
	}

	public int isBLUETOOTHTestOk() {
		return settings.getInt(KEY_BLUETOOTH_STATE, 0);
	}

	public void setBLUETOOTHTestOk(int enable) {
		editor.putInt(KEY_BLUETOOTH_STATE, enable);
		editor.commit();
	}

	public Boolean isBLUETOOTHDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_semifinished_self_test_bluetooth) : mContext
				.getResources().getBoolean(
						R.bool.def_finished_self_test_bluetooth);
		// return settings.getBoolean(KEY_BLUETOOTH_DEF, false);
	}

	public void setBLUETOOTHDef(Boolean enable) {
		editor.putBoolean(KEY_BLUETOOTH_DEF, enable);
		editor.commit();
	}

	public int isGPSTestOk() {
		return settings.getInt(KEY_GPS_STATE, 0);
	}

	public void setGPSTestOk(int enable) {
		editor.putInt(KEY_GPS_STATE, enable);
		editor.commit();
	}

	public Boolean isGPSDefSet() {
		return settings.getBoolean(KEY_GPS_DEF, false);
	}

	public void setGPSDef(Boolean enable) {
		editor.putBoolean(KEY_GPS_DEF, enable);
		editor.commit();
	}

	public int isEVDOTestOk() {
		return settings.getInt(KEY_EVDO_STATE, 0);
	}

	public void setEVDOTestOk(int enable) {
		editor.putInt(KEY_EVDO_STATE, enable);
		editor.commit();
	}

	public Boolean isEVDODefSet() {
		return settings.getBoolean(KEY_EVDO_DEF, false);
	}

	public void setEVDODef(Boolean enable) {
		editor.putBoolean(KEY_EVDO_DEF, enable);
		editor.commit();
	}

	public int isUSBTestOk() {
		return settings.getInt(KEY_USB_STATE, 0);
	}

	public void setUSBTestOk(int enable) {
		editor.putInt(KEY_USB_STATE, enable);
		editor.commit();
	}

	public Boolean isUSBDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_semifinished_self_test_usb) : mContext
				.getResources().getBoolean(R.bool.def_finished_self_test_usb);
		// return settings.getBoolean(KEY_USB_DEF, false);
	}

	public void setUSBDef(Boolean enable) {
		editor.putBoolean(KEY_USB_DEF, enable);
		editor.commit();
	}

	public int isBackCameraTestOk() {
		return settings.getInt(KEY_BACK_CAMERA_STATE, 0);
	}

	public void setBackCameraTestOk(int enable) {
		editor.putInt(KEY_BACK_CAMERA_STATE, enable);
		editor.commit();
	}

	public Boolean isBackCameraDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_semifinished_self_test_back_camera) : mContext
				.getResources().getBoolean(
						R.bool.def_finished_self_test_back_camera);
		// return settings.getBoolean(KEY_CAMERA_DEF, false);
	}

	public void setBackCameraDef(Boolean enable) {
		editor.putBoolean(KEY_BACK_CAMERA_DEF, enable);
		editor.commit();
	}

	public int isFrontCameraTestOk() {
		return settings.getInt(KEY_FRONT_CAMERA_STATE, 0);
	}

	public void setFrontCameraTestOk(int enable) {
		editor.putInt(KEY_FRONT_CAMERA_STATE, enable);
		editor.commit();
	}

	public Boolean isFrontCameraDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_semifinished_self_test_front_camera) : mContext
				.getResources().getBoolean(
						R.bool.def_finished_self_test_front_camera);
		// return settings.getBoolean(KEY_CAMERA_DEF, false);
	}

	public void setFrontCameraDef(Boolean enable) {
		editor.putBoolean(KEY_FRONT_CAMERA_DEF, enable);
		editor.commit();
	}

	public int isGSENSORTestOk() {
		return settings.getInt(KEY_GSENSOR_STATE, 0);
	}

	public void setGSENSORTestOk(int enable) {
		editor.putInt(KEY_GSENSOR_STATE, enable);
		editor.commit();
	}

	public Boolean isGSENSORDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_semifinished_self_test_gsensor) : mContext
				.getResources().getBoolean(
						R.bool.def_finished_self_test_gsensor);
		// return settings.getBoolean(KEY_GSENSOR_DEF, true);
	}

	public void setGSENSORDef(Boolean enable) {
		editor.putBoolean(KEY_GSENSOR_DEF, enable);
		editor.commit();
	}

	public int isLIGHTTestOk() {
		return settings.getInt(KEY_LIGHT_STATE, 0);
	}

	public void setLIGHTTestOk(int enable) {
		editor.putInt(KEY_LIGHT_STATE, enable);
		editor.commit();
	}

	public Boolean isLIGHTDefSet() {
		return settings.getBoolean(KEY_LIGHT_DEF, false);
	}

	public void setLIGHTDef(Boolean enable) {
		editor.putBoolean(KEY_LIGHT_DEF, enable);
		editor.commit();
	}

	public int isSPEAKERTestOk() {
		return settings.getInt(KEY_SPEAKER_STATE, 0);
	}

	public void setSPEAKERTestOk(int enable) {
		editor.putInt(KEY_SPEAKER_STATE, enable);
		editor.commit();
	}

	public Boolean isSPEAKERDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_semifinished_self_test_speaker) : mContext
				.getResources().getBoolean(
						R.bool.def_finished_self_test_speaker);
		// return settings.getBoolean(KEY_SPEAKER_DEF, true);
	}

	public void setSPEAKERDef(Boolean enable) {
		editor.putBoolean(KEY_SPEAKER_DEF, enable);
		editor.commit();
	}

	public int isHeadsetTestOk() {
		return settings.getInt(KEY_HEADSET_STATE, 0);
	}

	public void setHeadsetTestOk(int enable) {
		editor.putInt(KEY_HEADSET_STATE, enable);
		editor.commit();
	}

	public Boolean isHeadsetDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_finished_self_test_headset) : mContext
				.getResources().getBoolean(
						R.bool.def_finished_self_test_headset);
		// return settings.getBoolean(KEY_SPEAKER_DEF, true);
	}

	public void setHeadsetDef(Boolean enable) {
		editor.putBoolean(KEY_HEADSET_DEF, enable);
		editor.commit();
	}

	public int isMICTestOk() {
		return settings.getInt(KEY_MIC_STATE, 0);
	}

	public void setMICTestOk(int enable) {
		editor.putInt(KEY_MIC_STATE, enable);
		editor.commit();
	}

	public Boolean isMICDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_semifinished_self_test_mic) : mContext
				.getResources().getBoolean(R.bool.def_finished_self_test_mic);
		// return settings.getBoolean(KEY_MIC_DEF, true);
	}

	public void setMICDef(Boolean enable) {
		editor.putBoolean(KEY_MIC_DEF, enable);
		editor.commit();
	}

	public int isExtMICTestOk() {
		return settings.getInt(KEY_EXT_MIC_STATE, 0);
	}

	public void setExtMICTestOk(int enable) {
		editor.putInt(KEY_EXT_MIC_STATE, enable);
		editor.commit();
	}

	public Boolean isExtMICDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_semifinished_self_test_ext_mic) : mContext
				.getResources().getBoolean(
						R.bool.def_finished_self_test_ext_mic);
		// return settings.getBoolean(KEY_MIC_DEF, true);
	}

	public void setExtMICDef(Boolean enable) {
		editor.putBoolean(KEY_EXT_MIC_DEF, enable);
		editor.commit();
	}

	public int isTFCARDTestOk() {
		return settings.getInt(KEY_TFCARD_STATE, 0);
	}

	public void setTFCARDTestOk(int enable) {
		editor.putInt(KEY_TFCARD_STATE, enable);
		editor.commit();
	}

	public Boolean isTFCARDDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_semifinished_self_test_tfcard) : mContext
				.getResources()
				.getBoolean(R.bool.def_finished_self_test_tfcard);
		// return settings.getBoolean(KEY_TFCARD_DEF, true);
	}

	public void setTFCARDDef(Boolean enable) {
		editor.putBoolean(KEY_TFCARD_DEF, enable);
		editor.commit();
	}

	public int isHDMITestOk() {
		return settings.getInt(KEY_HDMI_STATE, 0);
	}

	public void setHDMITestOk(int enable) {
		editor.putInt(KEY_HDMI_STATE, enable);
		editor.commit();
	}

	public Boolean isHDMIDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_semifinished_self_test_hdmi) : mContext
				.getResources().getBoolean(R.bool.def_finished_self_test_hdmi);
		// return settings.getBoolean(KEY_HDMI_DEF, false);
	}

	public void setHDMIDef(Boolean enable) {
		editor.putBoolean(KEY_HDMI_DEF, enable);
		editor.commit();
	}

	public int isBATTERYTestOk() {
		return settings.getInt(KEY_BATTERY_STATE, 0);
	}

	public void setBATTERYTestOk(int enable) {
		editor.putInt(KEY_BATTERY_STATE, enable);
		editor.commit();
	}

	public Boolean isBATTERYDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_semifinished_self_test_battery) : mContext
				.getResources().getBoolean(
						R.bool.def_finished_self_test_battery);
		// return settings.getBoolean(KEY_BATTERY_DEF, false);
	}

	public void setBATTERYDef(Boolean enable) {
		editor.putBoolean(KEY_BATTERY_DEF, enable);
		editor.commit();
	}

	public int isKEYBOARDTestOk() {
		return settings.getInt(KEY_KEYBOARD_STATE, 0);
	}

	public void setKEYBOARDTestOk(int enable) {
		editor.putInt(KEY_KEYBOARD_STATE, enable);
		editor.commit();
	}

	public Boolean isKEYBOARDDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_semifinished_self_test_keyboard) : mContext
				.getResources().getBoolean(
						R.bool.def_finished_self_test_keyboard);
		// return settings.getBoolean(KEY_KEYBOARD_DEF, true);
	}

	public void setKEYBOARDDef(Boolean enable) {
		editor.putBoolean(KEY_KEYBOARD_DEF, enable);
		editor.commit();
	}

	public int isVERSIONTestOk() {
		return settings.getInt(KEY_VERSION_STATE, 0);
	}

	public void setVERSIONTestOk(int enable) {
		editor.putInt(KEY_VERSION_STATE, enable);
		editor.commit();
	}

	public Boolean isVERSIONDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_semifinished_self_test_version) : mContext
				.getResources().getBoolean(
						R.bool.def_finished_self_test_version);
		// return settings.getBoolean(KEY_VERSION_DEF, false);
	}

	public void setVERSIONDef(Boolean enable) {
		editor.putBoolean(KEY_VERSION_DEF, enable);
		editor.commit();
	}

	public int isBRIGHTNESSTestOk() {
		return settings.getInt(KEY_BRIGHTNESS_STATE, 0);
	}

	public void setBRIGHTNESSTestOk(int enable) {
		editor.putInt(KEY_BRIGHTNESS_STATE, enable);
		editor.commit();
	}

	public Boolean isBRIGHTNESSDefSet() {
		return semifinished ? mContext.getResources().getBoolean(
				R.bool.def_semifinished_self_test_brightness) : mContext
				.getResources().getBoolean(
						R.bool.def_finished_self_test_brightness);
		// return settings.getBoolean(KEY_BRIGHTNESS_DEF, false);
	}

	public void setBRIGHTNESSDef(Boolean enable) {
		editor.putBoolean(KEY_BRIGHTNESS_DEF, enable);
		editor.commit();
	}

	public void resetData() {
		editor.clear();
		editor.commit();
	}

}
