package com.tnovoselec.hrprognoza.async;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.tnovoselec.hrprognoza.db.CityTable;
import com.tnovoselec.hrprognoza.db.DailyForecastTable;
import com.tnovoselec.hrprognoza.db.HourlyForecastTable;
import com.tnovoselec.hrprognoza.listeners.CompletitionListener;
import com.tnovoselec.hrprognoza.model.City;
import com.tnovoselec.hrprognoza.model.DailyForecast;
import com.tnovoselec.hrprognoza.model.HourlyForecast;
import com.tnovoselec.hrprognoza.model.HourlyForecast.Forecast;
import com.tnovoselec.hrprognoza.provider.CityProvider;
import com.tnovoselec.hrprognoza.provider.DailyForecastProvider;
import com.tnovoselec.hrprognoza.provider.HourlyForecastProvider;

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
		for (Forecast forecast : hourlyForecast.getForecasts()) {
			forecast.setCityId(cityId);
			this.context.getContentResolver().insert(HourlyForecastProvider.CONTENT_URI, forecast.getContentValues());
		}
	}

	private synchronized void insertDailyForecastsToDb(DailyForecast dailyForecast, int cityId) {
		this.context.getContentResolver().delete(DailyForecastProvider.CONTENT_URI, DailyForecastTable.COLUMN_CITY_ID + "=" + cityId, null);
		for (com.tnovoselec.hrprognoza.model.DailyForecast.Forecast forecast : dailyForecast.getForecasts()) {
			forecast.setCityId(cityId);
			this.context.getContentResolver().insert(DailyForecastProvider.CONTENT_URI, forecast.getContentValues());
		}
	}

	private synchronized void updateCity(City city) {
		city.setTimestamp((int) System.currentTimeMillis());
		this.context.getContentResolver().update(CityProvider.CONTENT_URI, city.getContentValues(), CityTable.COLUMN_ID + "=" + city.getId(), null);
	}

}
