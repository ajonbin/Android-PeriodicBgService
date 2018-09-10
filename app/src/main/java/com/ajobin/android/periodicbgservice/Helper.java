package com.ajobin.android.periodicbgservice;

import android.app.ActivityManager;
import android.content.Context;

public class Helper {
	public static String getAppName(Context context){
		return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
	}

	public static boolean isServiceRunning(Context ctx, Class<?> serviceClass){
		ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo info : activityManager.getRunningServices(Integer.MAX_VALUE)){
			if(info.getClass().getName().equals(serviceClass.getName())){
				return true;
			}
		}
		return false;
	}
}
