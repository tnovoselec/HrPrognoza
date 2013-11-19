package com.tnovoselec.hrprognoza.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

import com.tnovoselec.hrprognoza.async.ForecastUpdaterTask;
import com.tnovoselec.hrprognoza.data.DataManager;
import com.tnovoselec.hrprognoza.db.CityTable;
import com.tnovoselec.hrprognoza.listeners.CompletitionListener;
import com.tnovoselec.hrprognoza.listeners.DailyForecastListener;
import com.tnovoselec.hrprognoza.listeners.HourlyForecastListener;
import com.tnovoselec.hrprognoza.model.City;
import com.tnovoselec.hrprognoza.model.DailyForecast;
import com.tnovoselec.hrprognoza.model.HourlyForecast;
import com.tnovoselec.hrprognoza.provider.CityProvider;

public class UpdateService extends Service implements CompletitionListener {

	private static String TAG = UpdateService.class.getSimpleName();
	private List<City> cities;
	private int dailyForecastCounter = 0;
	private int hourlyForecastCounter = 0;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.e(TAG, "start");
		cities = new ArrayList<City>();
		String[] projection = { CityTable.COLUMN_NAME, CityTable.COLUMN_LAT, CityTable.COLUMN_LNG, CityTable.COLUMN_COUNTRY, CityTable.COLUMN_ID,
				CityTable.COLUMN_SELECTED, CityTable.COLUMN_TIMESTAMP };
		Cursor cursor = getContentResolver().query(CityProvider.CONTENT_URI, projection, null, null, null);
		while (cursor.moveToNext()) {
			cities.add(City.fromCursor(cursor));
		}

		for (City city : cities) {
			if (!city.isSelected()) {
				getDailyForecastForCity(city);
				getHourlyForecastForCity(city);

			}

		}
	}

	private void getDailyForecastForCity(final City city) {
		DataManager.getInstance().getDailyForecast(city, new DailyForecastListener() {

			@Override
			public void onSuccess(DailyForecast dailyForecast) {
				city.setDailyForecast(dailyForecast);
				increaseDailyForecastCounter();
			}

			@Override
			public void onError(int code) {
				Log.e(TAG, "Getting daily forecasts failed..");
				increaseDailyForecastCounter();
			}
		});
	}

	private void getHourlyForecastForCity(final City city) {
		DataManager.getInstance().getHourlyForecast(city, new HourlyForecastListener() {

			@Override
			public void onSuccess(HourlyForecast hourlyForecast) {
				city.setHourlyForecast(hourlyForecast);
				increaseHourlyForecastCounter();
			}

			@Override
			public void onError(int code) {
				Log.e(TAG, "Getting hourly forecasts failed..");
				increaseHourlyForecastCounter();
			}
		});
	}

	private void increaseDailyForecastCounter() {
		dailyForecastCounter++;
		checkIfUpdateIsFinished();
	}

	private void increaseHourlyForecastCounter() {
		hourlyForecastCounter++;
		checkIfUpdateIsFinished();
	}

	private void checkIfUpdateIsFinished() {
		if (dailyForecastCounter == hourlyForecastCounter && dailyForecastCounter == cities.size()) {
			startDbUpdate();
		}
	}

	private void startDbUpdate() {
		ForecastUpdaterTask fut = new ForecastUpdaterTask(this, this);
		fut.execute(cities);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onSuccess() {
		Log.e(TAG, "Update finished successfully!");
		stopSelf();
	}

	@Override
	public void onError(int code) {
		Log.e(TAG, "Update failed horribly!");
		stopSelf();
	}

}
