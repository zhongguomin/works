package com.gpstracker.utils;

import android.util.Log;

public class Common {
	
	private static final String TAG = "GPSTracker-DEBUG";
	private static boolean DEBUG = true;
	
	public static void printLog(String log) {
		if (DEBUG)	Log.d(TAG, log);
	}

}
