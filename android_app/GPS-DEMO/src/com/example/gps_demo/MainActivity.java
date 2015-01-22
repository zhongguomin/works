package com.example.gps_demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String TAG = "GPS-DEMO";
	private EditText editText_1 = null;
	private EditText editText_2 = null;
	private LocationManager locationManager = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		editText_1 = (EditText)findViewById(R.id.editText_1);
		editText_2 = (EditText)findViewById(R.id.editText_2);
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "Please open GPS", Toast.LENGTH_LONG).show();
			
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, 0);
			return;
		}
		
		String bestProvider = locationManager.getBestProvider(getCriteria(), true);
		Location location = locationManager.getLastKnownLocation(bestProvider);
		updateLocationView(location);
		locationManager.addGpsStatusListener(gpsListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
	}
	
	private LocationListener locationListener = new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			case LocationProvider.AVAILABLE:
				updateGpsStatusView("GPS Provider available ..");
				break;
			case LocationProvider.OUT_OF_SERVICE:
				updateGpsStatusView("GPS out of service ..");
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				updateGpsStatusView("Current GPS service unavailable ..");
				break;
			default:
				break;
			}
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			updateGpsStatusView("GPS service enable ..");
			Location location = locationManager.getLastKnownLocation(provider);
			updateLocationView(location);
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			updateGpsStatusView("GPS service disable ..");
			updateLocationView(null);
		}
		
		@Override
		public void onLocationChanged(Location location) {
			updateLocationView(location);
		}
	};
	
	GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
		@Override
		public void onGpsStatusChanged(int event) {
			switch (event) {
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				updateGpsStatusView("fisrt location ..");
				break;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				updateGpsStatusView("satellite status change ..");
				GpsStatus gpsStatus = locationManager.getGpsStatus(null);
				int maxSatellites = gpsStatus.getMaxSatellites();
				Iterator<GpsSatellite> iterator = gpsStatus.getSatellites().iterator();
				int count = 0;
				while (iterator.hasNext() && count <= maxSatellites) {
					iterator.next();
					count++;
				}
				updateGpsStatusView("Search " + count + " Satellites");
				break;
			case GpsStatus.GPS_EVENT_STARTED:
				updateGpsStatusView("Location Start ..");
				break;
			case GpsStatus.GPS_EVENT_STOPPED:
				updateGpsStatusView("Location Stop ..");
				break;
			default:
				break;
			}
		}
	};
	
	private void updateGpsStatusView(String status) {
		editText_2.setText("GPS Status : \n");
		editText_2.append("[ ");
		editText_2.append(new SimpleDateFormat("HH:mm:ss").format(new Date()));
		editText_2.append(" ] ");
		editText_2.append(status);
		editText_2.append("\n");
	}
	
	private void updateLocationView(Location location) {
		if (location != null) {
			editText_1.setText("Device Location Info: \n");
			editText_1.append("\n Time: ");
			editText_1.append(String.valueOf(location.getTime()));
			editText_1.append("\n Longitude: ");
			editText_1.append(String.valueOf(location.getLongitude()));
			editText_1.append("\n Latitude: ");
			editText_1.append(String.valueOf(location.getLatitude()));
			editText_1.append("\n Altitude: ");
			editText_1.append(String.valueOf(location.getAltitude()));
		} else {
			editText_1.setText("[ ");
			editText_1.append(new SimpleDateFormat("HH:mm:ss").format(new Date()));
			editText_1.append(" ] ");
			editText_1.append("Location is null ..");
			//editText_1.getEditableText().clear();
		}
	}
	
	private Criteria getCriteria() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(false);
		criteria.setBearingRequired(false);
		criteria.setAltitudeRequired(false);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		return criteria;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean hasGPSDevice(Context context) {
		final LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager == null)	return false;
		final List<String> providers = locationManager.getAllProviders();
		if (providers == null)	return false;
		return providers.contains(LocationManager.GPS_PROVIDER);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		locationManager.removeUpdates(locationListener);
	}
	
}
