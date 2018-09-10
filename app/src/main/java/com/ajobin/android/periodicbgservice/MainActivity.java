package com.ajobin.android.periodicbgservice;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.time.chrono.HijrahEra;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	protected Logger logger = new Logger();
	protected Intent timerTaskIntent;
	protected Intent alarmIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		logger.init(this, getClass().getName());
		timerTaskIntent = new Intent(this, TimerTaskService.class);
		alarmIntent = new Intent(this, AlarmService.class);


		if(!Helper.isServiceRunning(this, TimerTaskService.class)){
			startTimerTaskService();
		}else{
			logger.log("Timer Task Service Is Running");
		}

		if(!Helper.isServiceRunning(this,AlarmService.class)){
			startAlarmService();
		}else{
			logger.log("Alarm Service Is Running");
		}

		scheduleJob();
	}

	public void scheduleJob(){
		logger.log("Call: scheduleJob()");
		ComponentName serviceComponent = new ComponentName(this, ScheduledJobService.class);

		JobInfo.Builder jobBuilder = new JobInfo.Builder(0, serviceComponent);
		jobBuilder.setPeriodic(5*60*1000);

		JobScheduler jobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
		int result = jobScheduler.schedule(jobBuilder.build());
		logger.log("scheduleJob - ", String.valueOf(result));
	}

	public void startTimerTaskService(){
		logger.log("Start Timer Task Service");
		startService(timerTaskIntent);
	}

	public void startAlarmService(){
		logger.log("Start Alarm Service");
		startService(alarmIntent);
	}

	@Override
	protected void onDestroy() {
		logger.log("Call: onDestroy()");
		stopService(timerTaskIntent);
		stopService(alarmIntent);
		super.onDestroy();
	}
}
