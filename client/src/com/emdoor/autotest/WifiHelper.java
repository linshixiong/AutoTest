package com.emdoor.autotest;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiHelper {
	/**
	 * These values are matched in string arrays -- changes must be kept in sync
	 */
	static final int SECURITY_NONE = 0;
	static final int SECURITY_WEP = 1;
	static final int SECURITY_PSK = 2;
	static final int SECURITY_EAP = 3;

	private static final String TAG = "WifiHelper";
	private static WifiHelper wifiHelper;
	private WifiManager mWifiManager;
	private Context mContext;
	private HashMap<String, AccessPoint> apMap;
	private String targetSSID;
	private int securityType=-1;
	public static WifiHelper getInstance(Context context) {
		if (wifiHelper == null) {
			wifiHelper = new WifiHelper(context);
		}
		return wifiHelper;
	}

	private WifiHelper(Context context) {
		this.mContext = context;
		mWifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		apMap = new HashMap<String, AccessPoint>();
		targetSSID=mContext.getString(R.string.def_wifi_ssid);
	}

	public WifiManager getWifiManager(){
		return mWifiManager;
	}
	
	// ´ò¿ªWIFI
	public void turnOnWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	public int getWifiState(){
		return mWifiManager.getWifiState();
	}
	
	public boolean isWifiEnabled() {
		boolean isWifiEnabled = mWifiManager.isWifiEnabled();
		Log.d(TAG, "isWifiEnabled=" + isWifiEnabled);
		return isWifiEnabled;
	}

	public boolean isTargetWifiConnected() {
		WifiInfo wifiInfo= mWifiManager.getConnectionInfo();
		if(wifiInfo==null){
			return false;
		}
		Log.d(TAG, "targetSSID :"+targetSSID+",wifiInfo.getSSID:"+wifiInfo.getSSID());
		return ("\""+targetSSID+"\"").equals(wifiInfo.getSSID());

	}

	
	public void scanAPList(){
		mWifiManager.startScan();
		
	}
	
	public List<ScanResult> getScanResultList(){
		return mWifiManager.getScanResults();
	}
	
	public List<WifiConfiguration> getConfiguredNetworks(){
		return mWifiManager.getConfiguredNetworks();
	}
	
	public boolean isTargetAPExist(){
		List<ScanResult> scanResults=  mWifiManager.getScanResults();
		if(scanResults==null){
			return false;
		}
		for (ScanResult scanResult : scanResults) {
			Log.d(TAG,"ScanResult :"+scanResult.SSID+",targetSSID:"+targetSSID);
			if( targetSSID.equals( scanResult.SSID)){
				securityType=getSecurity(scanResult);
				return true;
			}
		}
		return false;
	}
	
	
	public boolean connectWifi() {
		
	

		WifiConfiguration config=new WifiConfiguration();
		
		config.SSID="\""+targetSSID+"\"";
		Log.d(TAG, "connecting to "+targetSSID+" securityType is "+securityType);


		String password = "\""+mContext.getString(R.string.def_wifi_pwd)+"\"";
		
		config.preSharedKey = password;
		config.hiddenSSID = true;

		config.status = WifiConfiguration.Status.ENABLED;

		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);

		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

		config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);

		config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

		config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		int netId= mWifiManager.addNetwork(config);
		Log.d(TAG, "add network id="+netId);
		mWifiManager.saveConfiguration();
		boolean success = mWifiManager.enableNetwork(
				netId, true);
		
		
		Log.d(TAG, "connect success=" + success);
		return success;
	}
	
	
	private static int getSecurity(ScanResult result) {
		if (result.capabilities.contains("WEP")) {
			return SECURITY_WEP;
		} else if (result.capabilities.contains("PSK")) {
			return SECURITY_PSK;
		} else if (result.capabilities.contains("EAP")) {
			return SECURITY_EAP;
		}
		return SECURITY_NONE;
	}
}
