package com.ajobin.android.periodicbgservice;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;

import java.util.List;

public class ScheduledJobService extends JobService {
	private Logger logger = new Logger();
	private UserLocationDatabase db;
	private UserLocationDAO dao;
	private JobParameters jobParameters;
	private JobService self = this;
	private class QueryAsyncTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			self.jobFinished(jobParameters,false);
		}

		@Override
		protected Void doInBackground(Void... voids) {
			List<UserLocation> locationList = dao.getAll();
			logger.log("|----");
			for (UserLocation location : locationList) {
				logger.log(
					String.valueOf(location.getLocationId()),
					String.valueOf(location.getTimestampUTC()),
					location.getProvider(),
					String.valueOf(location.getLongitude()),
					String.valueOf(location.getLatitude()));
				dao.deleteLocatioin(location);
			}
			logger.log("----|");
			return null;
		}
	}
	@Override
	public boolean onStartJob(JobParameters params) {
		logger.log("ScheduledJobService::onStartJob()");
		this.jobParameters = params;
		db = UserLocationDatabase.getDatabase(this);
		dao = db.userLocationDAO();

		new QueryAsyncTask().execute();

		//Return true to make the job continue running
		return true;
	}

	@Override
	public boolean onStopJob(JobParameters params) {
		logger.log("onStopJob()");

		return false;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		logger.init(this, getClass().getSimpleName());
		logger.log("onCreate()");
	}

	@Override
	public void onDestroy() {
		logger.log("onDestroy()");
		super.onDestroy();
	}
}
