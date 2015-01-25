package com.gpstracker.service;

import com.gpstracker.utils.Common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {

	private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";  
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION_BOOT)) {
			Common.printLog("[BootBroadcastReceiver] onReceive BOOT_COMPLETED");
			Intent bootIntent = new Intent(context, GPSTrackerService.class);
			context.startService(bootIntent);
		}
	}

}
