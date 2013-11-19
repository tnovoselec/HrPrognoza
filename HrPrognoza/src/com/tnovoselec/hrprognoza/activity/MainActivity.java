package com.tnovoselec.hrprognoza.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.tnovoselec.hrprognoza.R;
import com.tnovoselec.hrprognoza.async.DbInitializerTask;
import com.tnovoselec.hrprognoza.async.DbInitializerTask.DbInitializerListener;
import com.tnovoselec.hrprognoza.data.DataManager;
import com.tnovoselec.hrprognoza.db.CityTable;
import com.tnovoselec.hrprognoza.db.DailyForecastTable;
import com.tnovoselec.hrprognoza.db.DatabaseHelper;
import com.tnovoselec.hrprognoza.db.HourlyForecastTable;
import com.tnovoselec.hrprognoza.listeners.DailyForecastListener;
import com.tnovoselec.hrprognoza.listeners.HourlyForecastListener;
import com.tnovoselec.hrprognoza.model.City;
import com.tnovoselec.hrprognoza.model.DailyForecast;
import com.tnovoselec.hrprognoza.model.HourlyForecast;
import com.tnovoselec.hrprognoza.model.HourlyForecast.Forecast;
import com.tnovoselec.hrprognoza.provider.CityProvider;
import com.tnovoselec.hrprognoza.provider.DailyForecastProvider;
import com.tnovoselec.hrprognoza.provider.HourlyForecastProvider;
import com.tnovoselec.hrprognoza.service.UpdateService;
import com.tnovoselec.hrprognoza.utils.Util;

public class MainActivity extends Activity implements DbInitializerListener, LoaderManager.LoaderCallbacks<Cursor> {

	private static String TAG = MainActivity.class.getSimpleName();
	private List<City> cities;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (!Util.isCityDbInitialized(this)) {
			new DbInitializerTask(this, this, new DatabaseHelper(this).getWritableDatabase()).execute(new Void[0]);
		} else {
			onCompleted();
		}
	}

	@Override
	public void onCompleted() {
		Log.i(TAG, "Db successfully initialized!");
		Util.setDbInitialized(this, true);
//		getLoaderManager().initLoader(0, null, this);
		Intent i = new Intent(this, UpdateService.class);
		startService(i);
	}

	@Override
	public void onError() {
		Log.e(TAG, "Db failed to initialize!");

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { CityTable.COLUMN_NAME, CityTable.COLUMN_LAT, CityTable.COLUMN_LNG, CityTable.COLUMN_COUNTRY, CityTable.COLUMN_ID, CityTable.COLUMN_SELECTED, CityTable.COLUMN_TIMESTAMP};
		CursorLoader cursorLoader = new CursorLoader(this, CityProvider.CONTENT_URI, projection, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		cities = new ArrayList<City>();
		while (cursor.moveToNext()) {
			cities.add(City.fromCursor(cursor));
		}
		for (final City city : cities) {
			getDailyForecastForCity(city);
			getHourlyForecastForCity(city);

		}

	}

	private void getDailyForecastForCity(final City city) {
		DataManager.getInstance().getDailyForecast(city, new DailyForecastListener() {

			@Override
			public void onSuccess(DailyForecast dailyForecast) {
				insertDailyForecastsToDb(dailyForecast, city.getId());

			}

			@Override
			public void onError(int code) {
				Log.e(TAG, "Getting daily forecasts failed..");
			}
		});
	}

	private void getHourlyForecastForCity(final City city) {
		DataManager.getInstance().getHourlyForecast(city, new HourlyForecastListener() {

			@Override
			public void onSuccess(HourlyForecast hourlyForecast) {
				insertHourlyForecastsToDb(hourlyForecast, city.getId());
			}

			@Override
			public void onError(int code) {
				Log.e(TAG, "Getting hourly forecasts failed..");
			}
		});
	}

	private synchronized void insertHourlyForecastsToDb(HourlyForecast hourlyForecast, int cityId) {
		getContentResolver().delete(HourlyForecastProvider.CONTENT_URI, HourlyForecastTable.COLUMN_CITY_ID + "=" + cityId, null);
		for (Forecast forecast : hourlyForecast.getForecasts()) {
			forecast.setCityId(cityId);
			getContentResolver().insert(HourlyForecastProvider.CONTENT_URI, forecast.getContentValues());
		}
	}

	private synchronized void insertDailyForecastsToDb(DailyForecast dailyForecast, int cityId) {
		getContentResolver().delete(DailyForecastProvider.CONTENT_URI, DailyForecastTable.COLUMN_CITY_ID + "=" + cityId, null);
		for (com.tnovoselec.hrprognoza.model.DailyForecast.Forecast forecast : dailyForecast.getForecasts()) {
			forecast.setCityId(cityId);
			getContentResolver().insert(DailyForecastProvider.CONTENT_URI, forecast.getContentValues());
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

}
