
package com.emdoor.autotest;

import android.content.Context;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

class AccessPoint {
	static final String TAG = "Settings.AccessPoint";

	private static final String KEY_DETAILEDSTATE = "key_detailedstate";
	private static final String KEY_WIFIINFO = "key_wifiinfo";
	private static final String KEY_SCANRESULT = "key_scanresult";
	private static final String KEY_CONFIG = "key_config";


	/**
	 * These values are matched in string arrays -- changes must be kept in sync
	 */
	static final int SECURITY_NONE = 0;
	static final int SECURITY_WEP = 1;
	static final int SECURITY_PSK = 2;
	static final int SECURITY_EAP = 3;

	enum PskType {
		UNKNOWN, WPA, WPA2, WPA_WPA2
	}

	String ssid;
	String bssid;
	int security;
	int networkId;
	boolean wpsAvailable = false;

	PskType pskType = PskType.UNKNOWN;

	private WifiConfiguration mConfig;
	/* package */ScanResult mScanResult;

	private int mRssi;
	private WifiInfo mInfo;
	private DetailedState mState;

	static int getSecurity(WifiConfiguration config) {
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_PSK)) {
			return SECURITY_PSK;
		}
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_EAP)
				|| config.allowedKeyManagement.get(KeyMgmt.IEEE8021X)) {
			return SECURITY_EAP;
		}
		return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
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

	private static PskType getPskType(ScanResult result) {
		boolean wpa = result.capabilities.contains("WPA-PSK");
		boolean wpa2 = result.capabilities.contains("WPA2-PSK");
		if (wpa2 && wpa) {
			return PskType.WPA_WPA2;
		} else if (wpa2) {
			return PskType.WPA2;
		} else if (wpa) {
			return PskType.WPA;
		} else {
			Log.w(TAG, "Received abnormal flag string: " + result.capabilities);
			return PskType.UNKNOWN;
		}
	}

	AccessPoint(Context context, WifiConfiguration config) {

		loadConfig(config);

	}

	AccessPoint(Context context, ScanResult result) {

		loadResult(result);

	}

	AccessPoint(Context context, Bundle savedState) {

		mConfig = savedState.getParcelable(KEY_CONFIG);
		if (mConfig != null) {
			loadConfig(mConfig);
		}
		mScanResult = (ScanResult) savedState.getParcelable(KEY_SCANRESULT);
		if (mScanResult != null) {
			loadResult(mScanResult);
		}
		mInfo = (WifiInfo) savedState.getParcelable(KEY_WIFIINFO);
		if (savedState.containsKey(KEY_DETAILEDSTATE)) {
			mState = DetailedState.valueOf(savedState
					.getString(KEY_DETAILEDSTATE));
		}

	}

	public void saveWifiState(Bundle savedState) {
		savedState.putParcelable(KEY_CONFIG, mConfig);
		savedState.putParcelable(KEY_SCANRESULT, mScanResult);
		savedState.putParcelable(KEY_WIFIINFO, mInfo);
		if (mState != null) {
			savedState.putString(KEY_DETAILEDSTATE, mState.toString());
		}
	}

	private void loadConfig(WifiConfiguration config) {
		ssid = (config.SSID == null ? "" : removeDoubleQuotes(config.SSID));
		bssid = config.BSSID;
		security = getSecurity(config);
		networkId = config.networkId;
		mRssi = Integer.MAX_VALUE;
		mConfig = config;
	}

	private void loadResult(ScanResult result) {
		ssid = result.SSID;
		bssid = result.BSSID;
		security = getSecurity(result);
		wpsAvailable = security != SECURITY_EAP
				&& result.capabilities.contains("WPS");
		if (security == SECURITY_PSK)
			pskType = getPskType(result);
		networkId = -1;
		mRssi = result.level;
		mScanResult = result;
	}

	@Override
	public int hashCode() {
		int result = 0;
		if (mInfo != null)
			result += 13 * mInfo.hashCode();
		result += 19 * mRssi;
		result += 23 * networkId;
		result += 29 * ssid.hashCode();
		return result;
	}

	int getLevel() {
		if (mRssi == Integer.MAX_VALUE) {
			return -1;
		}
		return WifiManager.calculateSignalLevel(mRssi, 4);
	}

	WifiConfiguration getConfig() {
		return mConfig;
	}

	WifiInfo getInfo() {
		return mInfo;
	}

	DetailedState getState() {
		return mState;
	}

	static String removeDoubleQuotes(String string) {
		int length = string.length();
		if ((length > 1) && (string.charAt(0) == '"')
				&& (string.charAt(length - 1) == '"')) {
			return string.substring(1, length - 1);
		}
		return string;
	}

	static String convertToQuotedString(String string) {
		return "\"" + string + "\"";
	}

	/**
	 * Generate and save a default wifiConfiguration with common values. Can
	 * only be called for unsecured networks.
	 * 
	 * @hide
	 */
	protected void generateOpenNetworkConfig() {
		if (security != SECURITY_NONE)
			throw new IllegalStateException();
		if (mConfig != null)
			return;
		mConfig = new WifiConfiguration();
		mConfig.SSID = AccessPoint.convertToQuotedString(ssid);
		mConfig.allowedKeyManagement.set(KeyMgmt.NONE);
	}
}
