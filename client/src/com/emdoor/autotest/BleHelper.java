package com.emdoor.autotest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.broadcom.bt.gatt.BluetoothGatt;
import com.broadcom.bt.gatt.BluetoothGattAdapter;
import com.broadcom.bt.gatt.BluetoothGattCallback;
import com.broadcom.bt.gatt.BluetoothGattCharacteristic;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BleHelper {

	/** Intents to indicate GATT state */
	public static final String GATT_DEVICE_FOUND = "com.broadcom.gatt.device_found";
	public static final String GATT_DEVICE_LOST = "com.broadcom.gatt.device_lost";
	public static final String GATT_CONNECTION_STATE = "com.broadcom.gatt.connection_state";
	public static final String GATT_SERVICES_REFRESHED = "com.broadcom.gatt.refreshed";
	public static final String GATT_CHARACTERISTIC_READ = "com.broadcom.gatt.read";

	/** Intent extras */
	public static final String EXTRA_DEVICE = "DEVICE";
	public static final String EXTRA_RSSI = "RSSI";
	public static final String EXTRA_SOURCE = "SOURCE";
	public static final String EXTRA_ADDR = "ADDRESS";
	public static final String EXTRA_CONNECTED = "CONNECTED";
	public static final String EXTRA_STATUS = "STATUS";
	public static final String EXTRA_UUID = "UUID";
	public static final String EXTRA_VALUE = "VALUE";

	/** Source of device entries in the device list */
	public static final int DEVICE_SOURCE_SCAN = 0;
	public static final int DEVICE_SOURCE_BONDED = 1;
	public static final int DEVICE_SOURCE_CONNECTED = 2;

	protected static final String TAG = BleHelper.class.getName();
	private boolean mScanning;
	private BluetoothGatt mBluetoothGatt;
	private BluetoothAdapter mBluetoothAdapter = null;

	List<String> mReconnectList = new ArrayList<String>();
	HashMap<String, Integer> deviceList=new HashMap<String, Integer>();
	private Context mcContext;
	private  final BluetoothGattCallback mGattCallbacks = new BluetoothGattCallback() {
		@Override
		public void onAppRegistered(int status) {
			Log.d(TAG, "onAppRegistered() - status=" + status);
			if (mScanning)
				scan(true);
		}

		@Override
		public void onScanResult(BluetoothDevice device, int rssi,
				byte[] scanRecord) {
			// BleHelper.this.device=device;
			deviceList.put(device.getAddress().replace(":", ""), rssi);
			Log.d(TAG, "onScanResult() - device=" + device + ", rssi=" + rssi);
		}

		@Override
		public void onConnectionStateChange(BluetoothDevice device, int status,
				int newState) {
			Log.d(TAG, "onConnectionStateChange() - device=" + device
					+ ", state=" + newState + ", reconnect="
					+ getReconnect(device.getAddress()));

			Intent intent = new Intent(GATT_CONNECTION_STATE);
			intent.putExtra(EXTRA_ADDR, device.getAddress());
			intent.putExtra(EXTRA_CONNECTED,
					newState == BluetoothProfile.STATE_CONNECTED);
			intent.putExtra(EXTRA_STATUS, status);
			mcContext.sendBroadcast(intent);

			if (newState == BluetoothProfile.STATE_CONNECTED
					&& mBluetoothGatt != null) {
				mBluetoothGatt.discoverServices(device);
			}

			if (newState == BluetoothProfile.STATE_DISCONNECTED
					&& mBluetoothGatt != null) {
				if (getReconnect(device.getAddress())) {
					mBluetoothGatt.connect(device, true);
				} else {
					mBluetoothGatt.cancelConnection(device);
				}
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothDevice device, int status) {
			Log.d(TAG, "onServicesDiscovered() - device=" + device
					+ ", status=" + status);
			Intent intent = new Intent(GATT_SERVICES_REFRESHED);
			intent.putExtra(EXTRA_ADDR, device.getAddress());
			intent.putExtra(EXTRA_STATUS, status);
			mcContext.sendBroadcast(intent);
		}

		@Override
		public void onCharacteristicRead(
				BluetoothGattCharacteristic characteristic, int status) {
			Log.d(TAG, "onCharacteristicRead() - characteristic="
					+ characteristic.getUuid() + ", status=" + status);

			if (status == 0) {
				Intent intent = new Intent(GATT_CHARACTERISTIC_READ);
				intent.putExtra(EXTRA_UUID, characteristic.getUuid().toString());
				intent.putExtra(EXTRA_STATUS, status);
				intent.putExtra(EXTRA_VALUE, characteristic.getValue());
				mcContext.sendBroadcast(intent);
			}
		}

		@Override
		public void onCharacteristicChanged(
				BluetoothGattCharacteristic characteristic) {
			onCharacteristicRead(characteristic, 0);
		}
	};

	private final BluetoothProfile.ServiceListener mProfileServiceListener = new BluetoothProfile.ServiceListener() {
		@Override
		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			Log.d(TAG, "Gatt proxy service connected");
			mBluetoothGatt = (BluetoothGatt) proxy;
			mBluetoothGatt.registerApp(mGattCallbacks);
		}

		@Override
		public void onServiceDisconnected(int profile) {
			Log.d(TAG, "Gatt proxy service disconnected");
			mBluetoothGatt = null;
		}
	};

	public BleHelper(Context context) {
		this.mcContext = context;
		this.init();
	}

	
	public void destroy() {
		//Log.d(TAG, "Destroying app service");

		if (mBluetoothAdapter != null && mBluetoothGatt != null) {
			BluetoothGattAdapter.closeProfileProxy(BluetoothGattAdapter.GATT,
					mBluetoothGatt);
		}

	}

	public boolean init() {
		if (mBluetoothAdapter == null) {
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			if (mBluetoothAdapter == null)
				return false;
		}

		if (mBluetoothGatt == null) {
			return BluetoothGattAdapter.getProfileProxy(mcContext,
					mProfileServiceListener, BluetoothGattAdapter.GATT);
		}

		return true;
	}

	public void scan(boolean start) {
		mScanning = start;
		if (mBluetoothGatt == null)
			return;

		if (start) {
			mBluetoothGatt.startScan();
		} else {
			mBluetoothGatt.stopScan();
		}
	}

	public void setReconnect(String address, boolean reconnect) {
		if (reconnect) {
			mReconnectList.add(address);
		} else {
			mReconnectList.remove(address);
		}
	}

	public boolean getReconnect(String address) {
		return mReconnectList.contains(address);
	}

	public void connect(String address) {
		if (mBluetoothGatt == null)
			return;

		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		if (device == null)
			return;

		mBluetoothGatt.connect(device, false);
	}

	public void disconnect(){
		if (mBluetoothGatt == null)
			return;

		for (BluetoothDevice device:mBluetoothGatt.getConnectedDevices()) {
			mBluetoothGatt.cancelConnection(device);
		} 
	}
	
	public void disconnect(String address) {
		Log.d(TAG, "disconnect() - address=" + address);
		if (mBluetoothGatt == null)
			return;

		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		if (device == null)
			return;

		mBluetoothGatt.cancelConnection(device);
		
	}

	public boolean enableBle(boolean enable) {
		boolean result = false;
		if (enable) {
			result = mBluetoothAdapter.enable();
			if (result == true) {
				scan(true);
				//connect("");
			}
		} else {
			disconnect();
			scan(false);
			result = mBluetoothAdapter.disable();

		}
		return result;
	}

	public String getBleMAC() {

		return BluetoothAdapter.getDefaultAdapter().getAddress()
				.replace(":", "");
	}
	
	
	public int getBleRSSI(String address){
		if(mBluetoothGatt==null){
			return 0;
		}
		//connect(address);
		
		address=address.replace(":", "");
		if(deviceList.containsKey(address)){
			return deviceList.get(address);
		}
		
		return 0;
		//mBluetoothGatt.
	}

}
