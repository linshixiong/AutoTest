package com.emdoor.autotest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;

public class Utils {
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			return  version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
