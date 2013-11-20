package com.tnovoselec.hrprognoza.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;

import com.tnovoselec.hrprognoza.R;
import com.tnovoselec.hrprognoza.adapter.CitiesAdapter;
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
	private List<City> cities = new ArrayList<City>();
	List<DailyForecast> dailyForecasts = new ArrayList<DailyForecast>();
	private ListView list;
	private CitiesAdapter adapter;
	private Set<Integer> citiesIds = new HashSet<Integer>();

	private int activeLoader = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		list = (ListView) findViewById(R.id.list);
		adapter = new CitiesAdapter(this, cities);
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
		activeLoader = 0;
		getLoaderManager().initLoader(0, null, this);

		// Intent i = new Intent(this, UpdateService.class);
		// startService(i);
	}

	@Override
	public void onError() {
		Log.e(TAG, "Db failed to initialize!");

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = null;

		if (id == 0) {
			String[] projection = { CityTable.COLUMN_NAME, CityTable.COLUMN_LAT, CityTable.COLUMN_LNG, CityTable.COLUMN_COUNTRY, CityTable.COLUMN_ID,
					CityTable.COLUMN_SELECTED, CityTable.COLUMN_TIMESTAMP };
			cursorLoader = new CursorLoader(this, CityProvider.CONTENT_URI, projection, null, null, null);

		} else if (id == 1) {
			String[] projection = { DailyForecastTable.COLUMN_CITY_ID, DailyForecastTable.COLUMN_CLOUDS, DailyForecastTable.COLUMN_DEG,
					DailyForecastTable.COLUMN_HUMIDITY, DailyForecastTable.COLUMN_ID, DailyForecastTable.COLUMN_PRESSURE, DailyForecastTable.COLUMN_RAIN,
					DailyForecastTable.COLUMN_SPEED, DailyForecastTable.COLUMN_TEMP_DAY, DailyForecastTable.COLUMN_TEMP_EVE,
					DailyForecastTable.COLUMN_TEMP_MAX, DailyForecastTable.COLUMN_TEMP_MIN, DailyForecastTable.COLUMN_TEMP_MORNING,
					DailyForecastTable.COLUMN_TEMP_NIGHT, DailyForecastTable.COLUMN_TIMESTAMP, DailyForecastTable.COLUMN_WEATHER_DESCRIPTION,
					DailyForecastTable.COLUMN_WEATHER_ICON, DailyForecastTable.COLUMN_WEATHER_ID, DailyForecastTable.COLUMN_WEATHER_MAIN };
			cursorLoader = new CursorLoader(this, DailyForecastProvider.CONTENT_URI, projection, null, null, null);

		}
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		if (activeLoader == 0) {
			while (cursor.moveToNext()) {
				cities.add(City.fromCursor(cursor));
			}
			for (final City city : cities) {
				citiesIds.add(city.getId());

			}

			if (PreferenceManager.getDefaultSharedPreferences(this).getLong("last_update", -1) + 5 * 2 * 60 * 1000 < System.currentTimeMillis()) {
				for (final City city : cities) {
					getDailyForecastForCity(city);
					// getHourlyForecastForCity(city);

				}
			} else {
				activeLoader = 1;
				getLoaderManager().initLoader(1, null, this);
			}
		} else if (activeLoader == 1) {
			while (cursor.moveToNext()) {
				com.tnovoselec.hrprognoza.model.DailyForecast.Forecast forecast = DailyForecast.fromCursor(cursor);
				boolean found = false;
				for (DailyForecast df : dailyForecasts) {

					if (df.getForecasts() != null && df.getForecasts().size() > 0 && df.getForecasts().get(0).getCityId() == forecast.getCityId()) {
						df.getForecasts().add(forecast);
						found = true;
					}
				}
				if (!found) {
					DailyForecast dff = new DailyForecast();
					dff.setForecasts(new ArrayList<DailyForecast.Forecast>());
					dff.getForecasts().add(forecast);
					boolean cityFound = false;
					for (City city : cities){
						if (city.getId() == forecast.getCityId()){
							city.setDailyForecast(dff);
							
						}
					}
				}

			}
			list.setAdapter(adapter);
		}

	}

	private void getDailyForecastForCity(final City city) {
		DataManager.getInstance().getDailyForecast(city, new DailyForecastListener() {

			@Override
			public void onSuccess(DailyForecast dailyForecast) {
				insertDailyForecastsToDb(dailyForecast, city.getId());
				city.setDailyForecast(dailyForecast);
				removeFromSet(city.getId());
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
				city.setHourlyForecast(hourlyForecast);
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

	private void removeFromSet(Integer id) {
		if (citiesIds.contains(id)) {
			citiesIds.remove(id);
			if (citiesIds.isEmpty()) {
				PreferenceManager.getDefaultSharedPreferences(this).edit().putLong("last_update", System.currentTimeMillis()).commit();
				list.setAdapter(adapter);
			}
		}
	}

}
