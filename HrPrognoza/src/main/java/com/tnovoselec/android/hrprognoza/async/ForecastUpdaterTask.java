package com.tnovoselec.android.hrprognoza.async;


import android.content.Context;
import android.os.AsyncTask;

import com.tnovoselec.android.hrprognoza.db.CityTable;
import com.tnovoselec.android.hrprognoza.db.DailyForecastTable;
import com.tnovoselec.android.hrprognoza.db.HourlyForecastTable;
import com.tnovoselec.android.hrprognoza.listeners.CompletitionListener;
import com.tnovoselec.android.hrprognoza.model.City;
import com.tnovoselec.android.hrprognoza.model.DailyForecast;
import com.tnovoselec.android.hrprognoza.model.HourlyForecast;
import com.tnovoselec.android.hrprognoza.provider.CityProvider;
import com.tnovoselec.android.hrprognoza.provider.DailyForecastProvider;
import com.tnovoselec.android.hrprognoza.provider.HourlyForecastProvider;

import java.util.List;


public class ForecastUpdaterTask extends AsyncTask<List<City>, Void, Void> {

	private CompletitionListener completitionListener;
	private Context context;
	private boolean failed = false;

	public ForecastUpdaterTask(CompletitionListener completitionListener, Context context) {
		this.completitionListener = completitionListener;
		this.context = context;
	}

	@Override
	protected Void doInBackground(List<City>... params) {
		List<City> cities = params[0];
		for (City city : cities) {
			if (city.getDailyForecast() != null && city.getHourlyForecast() != null) {
				insertDailyForecastsToDb(city.getDailyForecast(), city.getId());
				insertHourlyForecastsToDb(city.getHourlyForecast(), city.getId());
				updateCity(city);
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if (completitionListener != null) {
			if (failed) {
				completitionListener.onError(0);
			} else {
				completitionListener.onSuccess();
			}
		}
	}

	private synchronized void insertHourlyForecastsToDb(HourlyForecast hourlyForecast, int cityId) {
		this.context.getContentResolver().delete(HourlyForecastProvider.CONTENT_URI, HourlyForecastTable.COLUMN_CITY_ID + "=" + cityId, null);
		for (HourlyForecast.Forecast forecast : hourlyForecast.getForecasts()) {
			forecast.setCityId(cityId);
			this.context.getContentResolver().insert(HourlyForecastProvider.CONTENT_URI, forecast.getContentValues());
		}
	}

	private synchronized void insertDailyForecastsToDb(DailyForecast dailyForecast, int cityId) {
		this.context.getContentResolver().delete(DailyForecastProvider.CONTENT_URI, DailyForecastTable.COLUMN_CITY_ID + "=" + cityId, null);
		for (DailyForecast.Forecast forecast : dailyForecast.getForecasts()) {
			forecast.setCityId(cityId);
			this.context.getContentResolver().insert(DailyForecastProvider.CONTENT_URI, forecast.getContentValues());
		}
	}

	private synchronized void updateCity(City city) {
		city.setTimestamp((int) System.currentTimeMillis());
		this.context.getContentResolver().update(CityProvider.CONTENT_URI, city.getContentValues(), CityTable.COLUMN_ID + "=" + city.getId(), null);
	}

}
