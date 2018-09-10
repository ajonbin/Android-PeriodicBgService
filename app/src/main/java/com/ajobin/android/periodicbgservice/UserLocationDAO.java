package com.ajobin.android.periodicbgservice;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserLocationDAO {

	@Insert
	void insertLocation(UserLocation userLocation);

	@Query("SELECT * FROM user_location ORDER BY location_id DESC")
	List<UserLocation> getAll();

	@Delete
	void deleteLocatioin(UserLocation userLocation);
}
