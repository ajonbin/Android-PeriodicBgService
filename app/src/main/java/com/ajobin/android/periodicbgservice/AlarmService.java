package com.ajobin.android.periodicbgservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import java.nio.channels.AlreadyConnectedException;

public class AlarmService extends Service{
	private Logger logger = new Logger();
	public static String ACTION_RESTART = "com.ajonbin.android.alarmservice.restart";
	public static String ACTION_GEO = "com.ajonbin.android.alarmservice.geo";

	private AlarmReceiver geoAlarmReceiver;
	private LocationManager locationManager;
	private GeoListener networkLocationListener;
	private GeoListener gpsLocationListener;

	private UserLocationDatabase db;
	private UserLocationDAO dao;

	public AlarmService() {
		super();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		logger.init(this, this.getClass().getName());
		logger.log("AlarmService::onCreate()");

		db = UserLocationDatabase.getDatabase(this);
		dao = db.userLocationDAO();

	}

	/*
	inner Broadcast receiver must be static ( to be registered through Manifest)
	OR
	Non-static broadcast receiver must be registered and unregistered inside the Parent class
	 */
	public class AlarmReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			logger.log("TimerNetworkTask Run()");
			if (!networkLocationListener.isWaitingUpdate()) {
				logger.log("Request Network Location...");
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkLocationListener);
				networkLocationListener.startWaiting();
			} else {
				logger.log("Network is Locating");
			}

			if (!gpsLocationListener.isWaitingUpdate()) {
				logger.log("Request GPS Location...");
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationListener);
				gpsLocationListener.startWaiting();
			}else {
				logger.log("GPS is Locating");
			}

		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		logger.log("AlarmService::onStartCommand()");
		Intent alarmIntent = new Intent(ACTION_GEO);
		PendingIntent alarmPendingIndent = PendingIntent.getBroadcast(this,0,alarmIntent,0);
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
			SystemClock.elapsedRealtime(),
			5*60*1000,
			alarmPendingIndent);

		geoAlarmReceiver = new AlarmReceiver();

		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_GEO);
		registerReceiver(geoAlarmReceiver,filter);


		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		networkLocationListener = new GeoListener(locationManager,logger,LocationManager.NETWORK_PROVIDER);
		gpsLocationListener = new GeoListener(locationManager,logger,LocationManager.GPS_PROVIDER);
		networkLocationListener.SetDatabase(db,dao);
		gpsLocationListener.SetDatabase(db,dao);

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		logger.log("AlarmService::onDestroy()");
		Intent intent = new Intent(ACTION_RESTART);
		sendBroadcast(intent);
		super.onDestroy();
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
