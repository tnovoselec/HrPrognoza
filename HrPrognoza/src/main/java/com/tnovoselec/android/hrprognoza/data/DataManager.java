package com.tnovoselec.android.hrprognoza.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tnovoselec.android.hrprognoza.Config;
import com.tnovoselec.android.hrprognoza.listeners.DailyForecastListener;
import com.tnovoselec.android.hrprognoza.listeners.HourlyForecastListener;
import com.tnovoselec.android.hrprognoza.model.City;
import com.tnovoselec.android.hrprognoza.model.DailyForecast;
import com.tnovoselec.android.hrprognoza.model.HourlyForecast;


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
				Log.e(TAG, ""+ error.getMessage());
			}
		});
		reqQueue.add(req);

	}
	
	public void getDailyForecast(City city, final DailyForecastListener dailyForecastListener){
		String url = String.format(Config.DAILY_FORECAST_URL, city.getId());

		GsonRequest<DailyForecast> req = new GsonRequest<DailyForecast>(url, DailyForecast.class, null, new Listener<DailyForecast>() {

			@Override
			public void onResponse(DailyForecast response) {
				if (dailyForecastListener != null) {
					dailyForecastListener.onSuccess(response);
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
	
	public void getTestData(){
		String url ="http://192.168.1.94/restapi.php?action=get_app_list";
		StringRequest req = new StringRequest(url, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.e("kita", response);
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("kita", error.getLocalizedMessage());
			}
		});
		reqQueue.add(req);
	}
}
