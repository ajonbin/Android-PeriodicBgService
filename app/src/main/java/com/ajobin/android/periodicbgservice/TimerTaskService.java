package com.ajobin.android.periodicbgservice;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTaskService extends Service {
	public static String ACTION_RESTART = "com.ajonbin.android.timertaskservice.restart";
	private Logger logger = new Logger();
	private Timer timer = new Timer();
	private TimerTask timerNetworkTask;
	private TimerTask timerGpsTask;
	private LocationManager locationManager;
	private GeoListener networkLocationListener;
	private GeoListener gpsLocationListener;

	private UserLocationDatabase db;
	private UserLocationDAO dao;

	private Handler handler = new Handler();

	public TimerTaskService() {
		super();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		logger.init(this, getClass().getName());
		logger.log("Call: onCreate()");

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		db = UserLocationDatabase.getDatabase(this);
		dao = db.userLocationDAO();

		networkLocationListener = new GeoListener(locationManager,logger,LocationManager.NETWORK_PROVIDER);
		gpsLocationListener = new GeoListener(locationManager,logger,LocationManager.GPS_PROVIDER);

		networkLocationListener.SetDatabase(db,dao);
		gpsLocationListener.SetDatabase(db,dao);

		timerNetworkTask = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						logger.log("TimerNetworkTask Run()");
						if (!networkLocationListener.isWaitingUpdate()) {
							logger.log("Request Network Location...");
							locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkLocationListener);
							networkLocationListener.startWaiting();
						} else {
							logger.log("Network is Locating");
						}
					}
				});
			}
		};
		timerGpsTask = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						logger.log("TimerGPSTask Run()");
						if (!gpsLocationListener.isWaitingUpdate()) {
							logger.log("Request GPS Location...");
							locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationListener);
							gpsLocationListener.startWaiting();
						}else {
							logger.log("GPS is Locating");
						}
					}
				});
			}
		};

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		logger.log("Call: onStartCommand()");


		timer.schedule(timerNetworkTask, 0, 5*60*1000);
		timer.schedule(timerGpsTask, 0, 5*60*1000);


		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		logger.log("Call: onDestroy()");
		Intent broadcastIntent = new Intent(ACTION_RESTART);
		sendBroadcast(broadcastIntent);
		super.onDestroy();
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
