package com.tnovoselec.hrprognoza.data;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class DataManager {

	private static DataManager INSTANCE;

	private RequestQueue reqQueue;

	private DataManager() {
	}

	public static DataManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DataManager();
		}
		return INSTANCE;
	}

	public void init(Context context) {
		reqQueue = Volley.newRequestQueue(context);
	}
}
