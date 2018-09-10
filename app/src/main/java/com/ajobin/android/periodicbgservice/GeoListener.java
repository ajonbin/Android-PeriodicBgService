package com.ajobin.android.periodicbgservice;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

public class GeoListener implements LocationListener {
	private LocationManager locationManager;
	private Logger logger;
	private String provider;
	private boolean waitingUpdate;
	private int counter = 0;

	private UserLocationDatabase db;
	private UserLocationDAO dao;

	private static class InsertAsyncTask extends AsyncTask<UserLocation, Void, Void> {

		private UserLocationDAO mAsyncTaskDao;

		InsertAsyncTask(UserLocationDAO dao) {
			mAsyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(UserLocation... userLocations) {
			mAsyncTaskDao.insertLocation(userLocations[0]);
			return null;
		}
	}


	public GeoListener(LocationManager locationManager, Logger logger, String provider) {
		this.locationManager = locationManager;
		this.logger = logger;
		this.provider = provider;
		waitingUpdate = false;
	}

	public void SetDatabase(UserLocationDatabase db, UserLocationDAO dao){
		this.db = db;
		this.dao = dao;
	}

	public void startWaiting() {
		waitingUpdate = true;
	}

	public boolean isWaitingUpdate() {
		return waitingUpdate;
	}

	@Override
	public void onLocationChanged(Location location) {
		locationManager.removeUpdates(this);
		logger.log(String.valueOf(++counter), provider, String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()));
		new InsertAsyncTask(dao).execute(new UserLocation(provider,location.getLongitude(), location.getLatitude(), location.getTime()));
		waitingUpdate = false;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}
}
