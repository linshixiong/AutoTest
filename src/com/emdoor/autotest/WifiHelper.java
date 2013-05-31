package com.emdoor.autotest;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiHelper {

	private static final String TAG = "WifiHelper";
	private static WifiHelper wifiHelper;
	private WifiManager mWifiManager;
	private Context mContext;
	/** 指定热点SSID **/
	private final String wifiSSID = "\"TP-LINK_585034\"";
	/** 指定热点名称 **/
	private final String wifiName = "TP-LINK_585034";

	private int networkId;

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
	}

	// 打开WIFI
	public void turnOnWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	public boolean isWifiEnabled() {
		boolean isWifiEnabled = mWifiManager.isWifiEnabled();
		Log.d(TAG, "isWifiEnabled=" + isWifiEnabled);
		return isWifiEnabled;
	}

	public boolean isWifiConnected() {
		boolean isWifiConnected = (mWifiManager.getConnectionInfo() == null);
		Log.d(TAG, "isWifiConnected=" + isWifiConnected);
		return isWifiConnected;
	}

	
	public void scanAPList(){
		mWifiManager.startScan();
		
	}
	
	public List<ScanResult> getScanResultList(){
		return mWifiManager.getScanResults();
	}

	/**
	 * Description 连接指定热点
	 * 
	 * @return
	 */
	public boolean connectTargetWifi() {
		WifiConfiguration wc=new WifiConfiguration();
		
		wc.SSID = "\"emdoor_soft\"";

		wc.preSharedKey = "password";

		wc.hiddenSSID = false;

		wc.status = WifiConfiguration.Status.ENABLED;

		wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

		wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);

		wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

		wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);

		wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

		wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

		int res = mWifiManager.addNetwork(wc);

		Log.d(TAG, "add Network returned " + res );

		boolean b = mWifiManager.enableNetwork(res, true);

		Log.d(TAG, "enableNetwork returned " + b );
		return b;
	}
}
