package com.gpstracker.service;

import com.gpstracker.data.GPSDataObtain;
import com.gpstracker.network.ConnectionManager;
import com.gpstracker.utils.Common;

import android.os.IBinder;
import android.app.Service;
import android.content.Intent;

public class GPSTrackerService extends Service {

	private final String host = "127.0.0.1";
	private final int port = 80;
	private ConnectionManager connectionManager = null;
	private GPSDataObtain gpsDataObtain = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Common.printLog("[GSPTrackerService] onCreate");
		
		// have open GPS
		
		// have network conection (wifi or others)
		
		// create conection
		connectionManager = new ConnectionManager(host, port);
		
		// create gps data
		gpsDataObtain = new GPSDataObtain();
		gpsDataObtain.startLocation();
	}
	
	public void sendSocketMessage(String message) {
		connectionManager.sendMessage(message);
	}
	
	public void onSocketMessageAvailable() {
		
	}
	
	public void onGPSDataAvailable() {
		
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Common.printLog("[GSPTrackerService] onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		Common.printLog("[GSPTrackerService] onBind");
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Common.printLog("[GSPTrackerService] onDestroy");
		connectionManager.closeTCPConnection();
	}
}
