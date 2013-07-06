package com.emdoor.autotest;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiHelper {

	static final int SECURITY_NONE = 0;
	static final int SECURITY_WEP = 1;
	static final int SECURITY_PSK = 2;
	static final int SECURITY_EAP = 3;

	private static final String TAG = "WifiHelper";
	private static WifiHelper wifiHelper;
	private WifiManager mWifiManager;
	private Context mContext;
	//private String targetSSID;
	private int securityType = -1;

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
		//targetSSID = Settings.getSSID();
	}

	public WifiManager getWifiManager() {
		return mWifiManager;
	}

	// ´ò¿ªWIFI
	public void turnOnWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	public int getWifiState() {
		return mWifiManager.getWifiState();
	}

	public void disableTargetWifi(){
		if(isTargetWifiConnected()){
			WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
			if (wifiInfo == null) {
				return ;
			}
			mWifiManager.disableNetwork(wifiInfo.getNetworkId());
		}
	}
	
	
	public boolean isWifiEnabled() {
		boolean isWifiEnabled = mWifiManager.isWifiEnabled();
		Log.d(TAG, "isWifiEnabled=" + isWifiEnabled);
		return isWifiEnabled;
	}

	public boolean isTargetWifiConnected() {
		if(!mWifiManager.isWifiEnabled()){
			return false;
		}
		WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
		if (wifiInfo == null) {
			return false;
		}
		
		String ssid=wifiInfo.getSSID();
		if(ssid.startsWith("\"")){
			ssid=ssid.substring(1);
		}
		if(ssid.endsWith("\"")){
			ssid=ssid.substring(0, ssid.length()-1);
		}
		String targetSSID=Settings.getSSID();
		Log.d(TAG, "targetSSID:"+targetSSID+",wifiInfo.getSSID()="+ssid);
		return targetSSID.equals(ssid);
		//return convertToQuotedString(targetSSID).equals(wifiInfo.getSSID());

	}

	public String getWifiMAC() {
		WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
		if (wifiInfo == null) {
			return null;
		}
		return wifiInfo.getMacAddress().replace(":", "").toUpperCase();
	}

	public int getWifiSignal() {
		WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
		if (wifiInfo == null) {
			return 0;
		}
		return wifiInfo.getRssi();
	}

	public void scanAPList() {
		mWifiManager.startScan();

	}

	public List<ScanResult> getScanResultList() {
		return mWifiManager.getScanResults();
	}

	public List<WifiConfiguration> getConfiguredNetworks() {
		return mWifiManager.getConfiguredNetworks();
	}

	public boolean isTargetAPExist() {
		List<ScanResult> scanResults = mWifiManager.getScanResults();
		if (scanResults == null) {
			return false;
		}
		String targetSSID=Settings.getSSID();
		for (ScanResult scanResult : scanResults) {
			if (targetSSID.equals(scanResult.SSID)) {
				securityType = getSecurity(scanResult);
				return true;
			}
		}
		return false;
	}

	public boolean isTargetAPExist(String ssid){
		if(ssid==null){
			return false;
		}
		List<ScanResult> scanResults = mWifiManager.getScanResults();
		if (scanResults == null) {
			return false;
		}
		for (ScanResult scanResult : scanResults) {
			if (ssid.equals(scanResult.SSID)) {
				securityType = getSecurity(scanResult);
				return true;
			}
		}
		return false;
	}
	
	public boolean connectWifi() {
		String targetSSID=Settings.getSSID();
		Log.d(TAG, "connecting to " + targetSSID + " securityType is "
				+ securityType);
		String password = Settings.getPwd();
		WifiConfiguration config = getConfig(-1, targetSSID, password,
				securityType);

		int netId = mWifiManager.addNetwork(config);
		Log.d(TAG, "add network id=" + netId);
		mWifiManager.saveConfiguration();
		boolean success = mWifiManager.enableNetwork(netId, true);

		Log.d(TAG, "connect success=" + success);
		return success;
	}

	private static WifiConfiguration getConfig(int networkId, String ssid,
			String password, int mAccessPointSecurity) {

		WifiConfiguration config = new WifiConfiguration();

		if (networkId == -1) {
			config.SSID = convertToQuotedString(ssid);
		} else {
			config.networkId = networkId;
		}

		switch (mAccessPointSecurity) {
		case SECURITY_NONE:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			break;

		case SECURITY_WEP:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
			if (password.length() != 0) {
				int length = password.length();
				if ((length == 10 || length == 26 || length == 58)
						&& password.matches("[0-9A-Fa-f]*")) {
					config.wepKeys[0] = password;
				} else {
					config.wepKeys[0] = '"' + password + '"';
				}
			}
			break;

		case SECURITY_PSK:
			config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
			if (password.length() != 0) {

				if (password.matches("[0-9A-Fa-f]{64}")) {
					config.preSharedKey = password;
				} else {
					config.preSharedKey = '"' + password + '"';
				}
			}
			break;

		case SECURITY_EAP:
			config.allowedKeyManagement.set(KeyMgmt.WPA_EAP);
			config.allowedKeyManagement.set(KeyMgmt.IEEE8021X);
			if (password.length() != 0) {
				if (password.matches("[0-9A-Fa-f]{64}")) {
					config.preSharedKey = password;
				} else {
					config.preSharedKey = '"' + password + '"';
				}
			}
			break;

		default:
			return null;
		}

		return config;
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
	
	static String convertToQuotedString(String string) {
		return "\"" + string + "\"";
	}
}
