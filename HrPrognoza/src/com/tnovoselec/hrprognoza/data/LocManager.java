package com.tnovoselec.hrprognoza.data;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.tnovoselec.hrprognoza.listeners.LocationListener;

public enum LocManager {

	INSTANCE;

	public void getLocation(Context context, final LocationListener locationListener) {
		LocationManager man = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Location location = man.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);
		if (location != null) {
			if (locationListener != null) {
				locationListener.onComplete(location);
			}
		} else {

			man.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new android.location.LocationListener() {

				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) {
				}

				@Override
				public void onProviderEnabled(String provider) {
				}

				@Override
				public void onProviderDisabled(String provider) {
				}

				@Override
				public void onLocationChanged(Location location) {
					if (locationListener != null) {
						locationListener.onComplete(location);
					}
				}
			});
		}
	}

}
