package com.emdoor.autotest;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity implements
		OnPreferenceChangeListener {
	private final String KEY_WIFI_SSID = "edittext_wifi_ssid";
	private final String KEY_WIFI_PWD = "edittext_wifi_pwd";
	private final String KEY_SERVER_HOST = "edittext_server_host";
	private final String KEY_SERVER_PORT = "edittext_server_port";
	private final String KEY_VERSION = "prdference_version";
	private final String KEY_BLE_MAC="edittext_ble_mac";
	private EditTextPreference preferenceSsid;
	private EditTextPreference preferencePwd;
	private EditTextPreference preferencePort;
	private EditTextPreference preferenceHost;
	private EditTextPreference preferenceBleMac;
	private Preference preferenceVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		preferencePwd = (EditTextPreference) findPreference(KEY_WIFI_PWD);
		preferenceSsid = (EditTextPreference) findPreference(KEY_WIFI_SSID);
		preferencePort = (EditTextPreference) findPreference(KEY_SERVER_PORT);
		preferenceHost = (EditTextPreference) findPreference(KEY_SERVER_HOST);
		preferenceVersion = (Preference) findPreference(KEY_VERSION);
		preferenceBleMac=(EditTextPreference)findPreference(KEY_BLE_MAC);
		if (preferencePort != null) {
			preferencePort.setOnPreferenceChangeListener(this);
		}
		if (preferenceHost != null) {
			preferenceHost.setOnPreferenceChangeListener(this);
		}
		if (preferenceSsid != null) {
			preferenceSsid.setOnPreferenceChangeListener(this);
		}
		if (preferencePwd != null) {
			preferencePwd.setOnPreferenceChangeListener(this);
		}
		if (preferenceBleMac != null) {
			preferenceBleMac.setOnPreferenceChangeListener(this);
		}
		if (preferenceVersion != null) {
			try {
				preferenceVersion.setSummary(this.getPackageManager()
						.getPackageInfo(getPackageName(), 0).versionName);
			} catch (Exception e) {

			}
		}

		updatePreferences();
	}

	private void updatePreferences() {
		if (preferenceSsid != null) {
			preferenceSsid.setText(Settings.getSSID());
			preferenceSsid.setSummary(getResources().getString(
					R.string.wifi_ssid_summary, Settings.getSSID()));
		}
		if (preferencePwd != null) {
			preferencePwd.setText(Settings.getPwd());
		}
		if (preferencePort != null) {
			preferencePort.setText(String.valueOf(Settings.getPort()));
			preferencePort.setSummary(getResources().getString(
					R.string.server_port_summary, Settings.getPort()));
		}
		if (preferenceHost != null) {
			preferenceHost.setText(Settings.getServerHost());
			preferenceHost.setSummary(getResources().getString(
					R.string.server_host_summary, Settings.getServerHost()));
		}
		if (preferenceBleMac != null) {
			preferenceBleMac.setText(Settings.getBLEDeviceMAC());
			preferenceBleMac.setSummary(getResources().getString(
					R.string.ble_mac_summary, Settings.getBLEDeviceMAC()));
		}
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (KEY_SERVER_PORT.equals(preference.getKey())) {
			int port = Integer.parseInt(newValue.toString());
			if (port < 1024 || port > 65535) {
				Toast.makeText(this, R.string.server_port_invalid,
						Toast.LENGTH_SHORT).show();
			} else {
				Settings.setPort(port);
				updatePreferences();

			}

		} else if (KEY_SERVER_HOST.equals(preference.getKey())) {
			String host = newValue.toString();
			Settings.setServerHost(host);
			updatePreferences();
		}
		else if (KEY_WIFI_SSID.equals(preference.getKey())) {
			String ssid = newValue.toString();
			Settings.setSSID(ssid);
			updatePreferences();
		}
		else if (KEY_WIFI_PWD.equals(preference.getKey())) {
			String pwd = newValue.toString();
			Settings.setPwd(pwd);
			updatePreferences();
		}
		else if (KEY_BLE_MAC.equals(preference.getKey())) {
			String mac = newValue.toString();
			Settings.setBLEDeviceMAC(mac);
			updatePreferences();
		}
		return false;
	}

}
