package com.emdoor.autotest;


import android.content.Context;
import android.content.SharedPreferences;

public class Configuration {
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	private Context mContext;
	private static final String KEY_LCD_STATE="lcd";
	private static final String KEY_LCD_DEF="lcd_def";
	private static final String KEY_TOUCH_STATE="touch";
	private static final String KEY_TOUCH_DEF="touch_def";
	private static final String KEY_WIFI_STATE="wifi";
	private static final String KEY_WIFI_DEF="wifi_def";
	private static final String KEY_BLUETOOTH_STATE="bluetooth";
	private static final String KEY_BLUETOOTH_DEF="bluetooth_def";
	private static final String KEY_GPS_STATE="gps";
	private static final String KEY_GPS_DEF="gps_def";
	private static final String KEY_EVDO_STATE="evdo";
	private static final String KEY_EVDO_DEF="evdo_def";
	private static final String KEY_USB_STATE="usb";
	private static final String KEY_USB_DEF="usb_def";
	private static final String KEY_CAMERA_STATE="camera";
	private static final String KEY_CAMERA_DEF="camera_def";
	private static final String KEY_GSENSOR_STATE="gsensor";
	private static final String KEY_GSENSOR_DEF="gsensor_def";
	private static final String KEY_LIGHT_STATE="lightsensor";
	private static final String KEY_LIGHT_DEF="light_def";
	private static final String KEY_SPEAKER_STATE="speaker";
	private static final String KEY_SPEAKER_DEF="speaker_def";
	private static final String KEY_MIC_STATE="mic";
	private static final String KEY_MIC_DEF="mic_def";
	private static final String KEY_TFCARD_STATE="tfcard";
	private static final String KEY_TFCARD_DEF="tfcard_def";
	private static final String KEY_HDMI_STATE="hdmi";
	private static final String KEY_HDMI_DEF="hdmi_def";
	private static final String KEY_BATTERY_STATE="battery";
	private static final String KEY_BATTERY_DEF="battery_def";
	private static final String KEY_KEYBOARD_STATE="keyboard";
	private static final String KEY_KEYBOARD_DEF="keyboard_def";
	private static final String KEY_VERSION_STATE="version";
	private static final String KEY_VERSION_DEF="version_def";
	private static final String KEY_BRIGHTNESS_STATE="brightness";
	private static final String KEY_BRIGHTNESS_DEF="brightness_def";
	
	public Configuration(Context context)
	{
		this.mContext=context;
		settings=mContext.getSharedPreferences("emdoor_selftest", Context.MODE_PRIVATE);
		editor=settings.edit();		
	}

	public int isLCDTestOk(){
		return settings.getInt(KEY_LCD_STATE,0);
	}
	
	public void setLCDTestOk(int enable){
		editor.putInt(KEY_LCD_STATE, enable);
		editor.commit();
	}
	
	public Boolean isLCDDefSet(){
		return settings.getBoolean(KEY_LCD_DEF,false);
	}
	
	public void setLCDDef(Boolean enable){
		editor.putBoolean(KEY_LCD_DEF, enable);
		editor.commit();
	}
	
	public int isTOUCHTestOk(){
		return settings.getInt(KEY_TOUCH_STATE,0);
	}
	
	public void setTOUCHTestOk(int enable){
		editor.putInt(KEY_TOUCH_STATE, enable);
		editor.commit();
	}
	
	public Boolean isTOUCHDefSet(){
		return settings.getBoolean(KEY_TOUCH_DEF,false);
	}
	
	public void setTOUCHDef(Boolean enable){
		editor.putBoolean(KEY_TOUCH_DEF, enable);
		editor.commit();
	}	
	
	public int isWIFITestOk(){
		return settings.getInt(KEY_WIFI_STATE,0);
	}
	
	public void setWIFITestOk(int enable){
		editor.putInt(KEY_WIFI_STATE, enable);
		editor.commit();
	}
	
	public Boolean isWIFIDefSet(){
		return settings.getBoolean(KEY_WIFI_DEF,true);
	}
	
	public void setWIFIDef(Boolean enable){
		editor.putBoolean(KEY_WIFI_DEF, enable);
		editor.commit();
	}
	
	public int isBLUETOOTHTestOk(){
		return settings.getInt(KEY_BLUETOOTH_STATE,0);
	}
	
	public void setBLUETOOTHTestOk(int enable){
		editor.putInt(KEY_BLUETOOTH_STATE, enable);
		editor.commit();
	}
	
	public Boolean isBLUETOOTHDefSet(){
		return settings.getBoolean(KEY_BLUETOOTH_DEF,false);
	}
	
	public void setBLUETOOTHDef(Boolean enable){
		editor.putBoolean(KEY_BLUETOOTH_DEF, enable);
		editor.commit();
	}
	
	public int isGPSTestOk(){
		return settings.getInt(KEY_GPS_STATE,0);
	}
	
	public void setGPSTestOk(int enable){
		editor.putInt(KEY_GPS_STATE, enable);
		editor.commit();
	}
	
	public Boolean isGPSDefSet(){
		return settings.getBoolean(KEY_GPS_DEF,false);
	}
	
	public void setGPSDef(Boolean enable){
		editor.putBoolean(KEY_GPS_DEF, enable);
		editor.commit();
	}
	
	public int isEVDOTestOk(){
		return settings.getInt(KEY_EVDO_STATE,0);
	}
	
	public void setEVDOTestOk(int enable){
		editor.putInt(KEY_EVDO_STATE, enable);
		editor.commit();
	}
	
	public Boolean isEVDODefSet(){
		return settings.getBoolean(KEY_EVDO_DEF,false);
	}
	
	public void setEVDODef(Boolean enable){
		editor.putBoolean(KEY_EVDO_DEF, enable);
		editor.commit();
	}
	
	public int isUSBTestOk(){
		return settings.getInt(KEY_USB_STATE,0);
	}
	
	public void setUSBTestOk(int enable){
		editor.putInt(KEY_USB_STATE, enable);
		editor.commit();
	}
	
	public Boolean isUSBDefSet(){
		return settings.getBoolean(KEY_USB_DEF,false);
	}
	
	public void setUSBDef(Boolean enable){
		editor.putBoolean(KEY_USB_DEF, enable);
		editor.commit();
	}
	
	public int isCAMERATestOk(){
		return settings.getInt(KEY_CAMERA_STATE,0);
	}
	
	public void setCAMERATestOk(int enable){
		editor.putInt(KEY_CAMERA_STATE, enable);
		editor.commit();
	}
	
	public Boolean isCAMERADefSet(){
		return settings.getBoolean(KEY_CAMERA_DEF,false);
	}
	
	public void setCAMERADef(Boolean enable){
		editor.putBoolean(KEY_CAMERA_DEF, enable);
		editor.commit();
	}
	
	public int isGSENSORTestOk(){
		return settings.getInt(KEY_GSENSOR_STATE,0);
	}
	
	public void setGSENSORTestOk(int enable){
		editor.putInt(KEY_GSENSOR_STATE, enable);
		editor.commit();
	}
	
	public Boolean isGSENSORDefSet(){
		return settings.getBoolean(KEY_GSENSOR_DEF,true);
	}
	
	public void setGSENSORDef(Boolean enable){
		editor.putBoolean(KEY_GSENSOR_DEF, enable);
		editor.commit();
	}
	public int isLIGHTTestOk(){
		return settings.getInt(KEY_LIGHT_STATE,0);
	}
	
	public void setLIGHTTestOk(int enable){
		editor.putInt(KEY_LIGHT_STATE, enable);
		editor.commit();
	}
	
	public Boolean isLIGHTDefSet(){
		return settings.getBoolean(KEY_LIGHT_DEF,false);
	}
	
	public void setLIGHTDef(Boolean enable){
		editor.putBoolean(KEY_LIGHT_DEF, enable);
		editor.commit();
	}
	
	public int isSPEAKERTestOk(){
		return settings.getInt(KEY_SPEAKER_STATE,0);
	}
	
	public void setSPEAKERTestOk(int enable){
		editor.putInt(KEY_SPEAKER_STATE, enable);
		editor.commit();
	}
	
	public Boolean isSPEAKERDefSet(){
		return settings.getBoolean(KEY_SPEAKER_DEF,true);
	}
	
	public void setSPEAKERDef(Boolean enable){
		editor.putBoolean(KEY_SPEAKER_DEF, enable);
		editor.commit();
	}
	
	public int isMICTestOk(){
		return settings.getInt(KEY_MIC_STATE,0);
	}
	
	public void setMICTestOk(int enable){
		editor.putInt(KEY_MIC_STATE, enable);
		editor.commit();
	}
	
	public Boolean isMICDefSet(){
		return settings.getBoolean(KEY_MIC_DEF,true);
	}
	
	public void setMICDef(Boolean enable){
		editor.putBoolean(KEY_MIC_DEF, enable);
		editor.commit();
	}
	
	public int isTFCARDTestOk(){
		return settings.getInt(KEY_TFCARD_STATE,0);
	}
	
	public void setTFCARDTestOk(int enable){
		editor.putInt(KEY_TFCARD_STATE, enable);
		editor.commit();
	}
	
	public Boolean isTFCARDDefSet(){
		return settings.getBoolean(KEY_TFCARD_DEF,true);
	}
	
	public void setTFCARDDef(Boolean enable){
		editor.putBoolean(KEY_TFCARD_DEF, enable);
		editor.commit();
	}
	
	public int isHDMITestOk(){
		return settings.getInt(KEY_HDMI_STATE,0);
	}
	
	public void setHDMITestOk(int enable){
		editor.putInt(KEY_HDMI_STATE, enable);
		editor.commit();
	}
	
	public Boolean isHDMIDefSet(){
		return settings.getBoolean(KEY_HDMI_DEF,false);
	}
	
	public void setHDMIDef(Boolean enable){
		editor.putBoolean(KEY_HDMI_DEF, enable);
		editor.commit();
	}
	
	public int isBATTERYTestOk(){
		return settings.getInt(KEY_BATTERY_STATE,0);
	}
	
	public void setBATTERYTestOk(int enable){
		editor.putInt(KEY_BATTERY_STATE, enable);
		editor.commit();
	}
	
	public Boolean isBATTERYDefSet(){
		return settings.getBoolean(KEY_BATTERY_DEF,false);
	}
	
	public void setBATTERYDef(Boolean enable){
		editor.putBoolean(KEY_BATTERY_DEF, enable);
		editor.commit();
	}
	
	public int isKEYBOARDTestOk(){
		return settings.getInt(KEY_KEYBOARD_STATE,0);
	}
	
	public void setKEYBOARDTestOk(int enable){
		editor.putInt(KEY_KEYBOARD_STATE, enable);
		editor.commit();
	}
	
	public Boolean isKEYBOARDDefSet(){
		return settings.getBoolean(KEY_KEYBOARD_DEF,true);
	}
	
	public void setKEYBOARDDef(Boolean enable){
		editor.putBoolean(KEY_KEYBOARD_DEF, enable);
		editor.commit();
	}
	
	public int isVERSIONTestOk(){
		return settings.getInt(KEY_VERSION_STATE,0);
	}
	
	public void setVERSIONTestOk(int enable){
		editor.putInt(KEY_VERSION_STATE, enable);
		editor.commit();
	}
	
	public Boolean isVERSIONDefSet(){
		return settings.getBoolean(KEY_VERSION_DEF,false);
	}
	
	public void setVERSIONDef(Boolean enable){
		editor.putBoolean(KEY_VERSION_DEF, enable);
		editor.commit();
	}
	
	public int isBRIGHTNESSTestOk(){
		return settings.getInt(KEY_BRIGHTNESS_STATE,0);
	}
	
	public void setBRIGHTNESSTestOk(int enable){
		editor.putInt(KEY_BRIGHTNESS_STATE, enable);
		editor.commit();
	}
	
	public Boolean isBRIGHTNESSDefSet(){
		return settings.getBoolean(KEY_BRIGHTNESS_DEF,false);
	}
	
	public void setBRIGHTNESSDef(Boolean enable){
		editor.putBoolean(KEY_BRIGHTNESS_DEF, enable);
		editor.commit();
	}
	
	public void resetData(){
		editor.clear();
		editor.commit();
	}
	
}
