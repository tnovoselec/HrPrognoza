package com.tnovoselec.hrprognoza.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.tnovoselec.hrprognoza.Config;
import com.tnovoselec.hrprognoza.listeners.HourlyForecastListener;
import com.tnovoselec.hrprognoza.model.City;
import com.tnovoselec.hrprognoza.model.HourlyForecast;

public class DataManager {
	private static String TAG = DataManager.class.getSimpleName();

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

	public void getHourlyForecast(City city, final HourlyForecastListener hourlyForecastListener) {
		String url = String.format(Config.HOURLY_FORECAST_URL, city.getId());

		GsonRequest<HourlyForecast> req = new GsonRequest<HourlyForecast>(url, HourlyForecast.class, null, new Listener<HourlyForecast>() {

			@Override
			public void onResponse(HourlyForecast response) {
				if (hourlyForecastListener != null) {
					hourlyForecastListener.onSuccess(response);
				}

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, error.getMessage());
			}
		});
		reqQueue.add(req);

	}
}
