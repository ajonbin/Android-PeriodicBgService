package com.ajobin.android.periodicbgservice;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = { UserLocation.class }, version = 1)
public abstract class UserLocationDatabase extends RoomDatabase {

	public abstract UserLocationDAO userLocationDAO();

	private static UserLocationDatabase INSTANCE;

	public static UserLocationDatabase getDatabase(final Context context) {
		if (INSTANCE == null) {
			synchronized (UserLocationDatabase.class) {
				if (INSTANCE == null) {
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
						UserLocationDatabase.class,
						"user_location_database")/*.allowMainThreadQueries()*/.build();
				}
			}
		}
		return INSTANCE;
	}
}
