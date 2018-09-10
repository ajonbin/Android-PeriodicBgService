package com.ajobin.android.periodicbgservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BackgroundServiceBoradcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(TimerTaskService.ACTION_RESTART)){
			context.startService(new Intent(context,TimerTaskService.class));
		}else if(intent.getAction().equals(AlarmService.ACTION_RESTART)){
			context.startService(new Intent(context,AlarmService.class));
		}
	}
}
