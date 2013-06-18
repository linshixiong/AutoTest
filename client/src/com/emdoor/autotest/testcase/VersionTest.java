package com.emdoor.autotest.testcase;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.emdoor.autotest.Configuration;
import com.emdoor.autotest.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.content.res.Resources;

public class VersionTest extends Activity {

	private Button versionCheckOk = null;
	private Button versionCheckFail = null;
	private Button versionCheckBack = null;
	private Configuration config;
	private String prompt;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.version);

		versionCheckOk = (Button) findViewById(R.id.btnversionok);
		versionCheckFail = (Button) findViewById(R.id.btnversionfail);
		versionCheckBack = (Button) findViewById(R.id.btnversionback);

		versionCheckOk.setOnClickListener(linstener);
		versionCheckFail.setOnClickListener(linstener);
		versionCheckBack.setOnClickListener(linstener);

		config = new Configuration(this);

		getData();

	}

	private void getData() {
		// TODO Auto-generated method stub
		String mode_number = android.os.Build.MODEL;
		String android_version = android.os.Build.VERSION.RELEASE;
		String kernel_version = getFormattedKernelVersion();
		String Baseband_version = null;
		try {
			Class cl = Class.forName("android.os.SystemProperties");
			Object invoker = cl.newInstance();
			Method m = cl.getMethod("get", new Class[] { String.class,
					String.class });
			Object result = m.invoke(invoker, new Object[] {
					"gsm.version.baseband", "UNKNOWN" });
			Baseband_version = (String) result;
		} catch (Exception e) {
		}

		String Build_number = android.os.Build.DISPLAY;

		((TextView) findViewById(R.id.model_no)).setText(mode_number);
		((TextView) findViewById(R.id.android_version))
				.setText(android_version);
		if (kernel_version != null) {
			((TextView) findViewById(R.id.kernel_version))
					.setText(kernel_version);
		}
		((TextView) findViewById(R.id.baseband_version))
				.setText(Baseband_version);
		((TextView) findViewById(R.id.build_no)).setText(Build_number);

	}

	private String getFormattedKernelVersion() {
		String procVersionStr;

		try {
			procVersionStr = readLine("/proc/version");

			final String PROC_VERSION_REGEX = "\\w+\\s+" + /* ignore: Linux */
			"\\w+\\s+" + /* ignore: version */
			"([^\\s]+)\\s+" + /* group 1: 2.6.22-omap1 */
			"\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+" + /*
														 * group 2:
														 * (xxxxxx@xxxxx
														 * .constant)
														 */
			"\\((?:[^(]*\\([^)]*\\))?[^)]*\\)\\s+" + /* ignore: (gcc ..) */
			"([^\\s]+)\\s+" + /* group 3: #26 */
			"(?:PREEMPT\\s+)?" + /* ignore: PREEMPT (optional) */
			"(.+)"; /* group 4: date */

			Pattern p = Pattern.compile(PROC_VERSION_REGEX);
			Matcher m = p.matcher(procVersionStr);

			if (!m.matches()) {
				return "Unavailable";
			} else if (m.groupCount() < 4) {
				return "Unavailable";
			} else {
				return (new StringBuilder(m.group(1)).append("\n")
						.append(m.group(2)).append(" ").append(m.group(3))
						.append("\n").append(m.group(4))).toString();
			}
		} catch (IOException e) {
			return "Unavailable";
		}
	}

	private String readLine(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename),
				256);
		try {
			return reader.readLine();
		} finally {
			reader.close();
		}
	}

	private final OnClickListener linstener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btnversionok:
				config.setVERSIONTestOk(1);
				break;
			case R.id.btnversionfail:
				config.setVERSIONTestOk(2);
				break;
			case R.id.btnversionback:
				config.setVERSIONTestOk(0);
				break;
			default:
				config.setVERSIONTestOk(0);
				break;
			}
			VersionTest.this.finish();
		}

	};

	@Override
	protected void onDestroy() {
		System.gc();
		super.onDestroy();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}
}
