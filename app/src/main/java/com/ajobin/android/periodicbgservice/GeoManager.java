package com.ajobin.android.periodicbgservice;

import android.content.Context;

public enum GeoManager {
	INSTANCE;

	private Context context;
	private boolean isLocating = false;

	public void SetContext(Context ctx){
		this.context = ctx;
	}


}
