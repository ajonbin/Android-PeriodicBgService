package com.ajobin.android.periodicbgservice;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "user_location")
public class UserLocation {
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "location_id")
	private long locationId;

	@NonNull
	@ColumnInfo(name = "longitude")
	private double longitude;

	@NonNull
	@ColumnInfo(name = "latitude")
	private double latitude;

	@NonNull
	@ColumnInfo(name = "timestamp_utc")
	private long timestampUTC;

	@NonNull
	@ColumnInfo(name = "provider")
	private String provider;

	public UserLocation(@NonNull String provider, @NonNull double longitude, @NonNull double latitude, @NonNull long timestampUTC) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.timestampUTC = timestampUTC;
		this.provider = provider;
	}

	@NonNull
	public String getProvider() {
		return provider;
	}

	public void setProvider(@NonNull String provider) {
		this.provider = provider;
	}

	public long getLocationId() {
		return locationId;
	}

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	@NonNull
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(@NonNull double longitude) {
		this.longitude = longitude;
	}

	@NonNull
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(@NonNull double latitude) {
		this.latitude = latitude;
	}

	@NonNull
	public long getTimestampUTC() {
		return timestampUTC;
	}

	public void setTimestampUTC(@NonNull long timestampUTC) {
		this.timestampUTC = timestampUTC;
	}
}
