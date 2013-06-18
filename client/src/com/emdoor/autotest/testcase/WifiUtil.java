package com.emdoor.autotest.testcase;

import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

public class WifiUtil{
	private WifiManager mWifiManager;	

	private WifiInfo mWifiInfo;	
	
	private NetworkInfo mWifi;
	
	ConnectivityManager connManager;

	private List<ScanResult> mWifiList;	

	private List<WifiConfiguration> mWifiConfiguration;	

	WifiLock mWifiLock;	

	public  WifiUtil(Context context){
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);		
		mWifiInfo = mWifiManager.getConnectionInfo();
		connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);		
	}
	
	public boolean isWifiConnect() {
		mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return mWifi.isConnected();
	}
	
	public Boolean isWifiOpen(){
		return mWifiManager.isWifiEnabled();		
	}

	public void openWifi(){
		if (!mWifiManager.isWifiEnabled()){
			mWifiManager.setWifiEnabled(true);
		}
	}
	
	public void closeWifi(){
		if (mWifiManager.isWifiEnabled()){
			mWifiManager.setWifiEnabled(false);	
		}
	}
	
	public void acquireWifiLock(){
		mWifiLock.acquire();
	}
	
	public void releaseWifiLock(){
		if (mWifiLock.isHeld()){
			mWifiLock.acquire();
		}
	}
	
	public void creatWifiLock(){
		mWifiLock = mWifiManager.createWifiLock("Test");
	}

	public List<WifiConfiguration> getConfiguration(){	
		return mWifiConfiguration;
	}
	
	public void connectConfiguration(int index){		
		if(index > mWifiConfiguration.size()){
			return;
		}		
		mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId, true);
	}
	
	public void startScan(){
		mWifiManager.startScan();		
		mWifiList = mWifiManager.getScanResults(); 
		mWifiConfiguration = mWifiManager.getConfiguredNetworks();
	}
	
	public List<ScanResult> getWifiList(){
		return mWifiList;
	}
	
	public StringBuilder lookUpScan(){
		StringBuilder stringBuilder = new StringBuilder();
		
		if (mWifiList.size() > 0) {
			stringBuilder.append("Wifi has find\n");
		}
		for (int i = 0; i < mWifiList.size(); i++) {
			stringBuilder.append("Index_" + new Integer(i + 1).toString()
					+ ":");
			stringBuilder.append((mWifiList.get(i)).toString());
			stringBuilder.append("\n");
		}
		
		return stringBuilder;
	}
	
	
	public String getMacAddress()	{
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}
	
	
	public String getBSSID(){
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}
	
	
	public int getIPAddress(){
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}
	
	
	public int getNetworkId(){
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}
	
	public String getWifiInfo(){
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}
	
	
	public void addNetwork(WifiConfiguration wcg){
		int wcgID = mWifiManager.addNetwork(wcg); 
		mWifiManager.enableNetwork(wcgID, true); 
	}
	
	public void disconnectWifi(int netId){
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}
}
