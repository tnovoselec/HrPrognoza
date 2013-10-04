package com.tnovoselec.hrprognoza.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.tnovoselec.hrprognoza.R;
import com.tnovoselec.hrprognoza.async.DbInitializerTask;
import com.tnovoselec.hrprognoza.async.DbInitializerTask.DbInitializerListener;
import com.tnovoselec.hrprognoza.data.DataManager;
import com.tnovoselec.hrprognoza.db.CityDatabaseHelper;
import com.tnovoselec.hrprognoza.db.CityTable;
import com.tnovoselec.hrprognoza.listeners.HourlyForecastListener;
import com.tnovoselec.hrprognoza.model.City;
import com.tnovoselec.hrprognoza.model.HourlyForecast;
import com.tnovoselec.hrprognoza.provider.CityProvider;
import com.tnovoselec.hrprognoza.utils.Util;

public class MainActivity extends Activity implements DbInitializerListener, LoaderManager.LoaderCallbacks<Cursor> {

	private static String TAG = MainActivity.class.getSimpleName();
	private List<City> cities;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (!Util.isCityDbInitialized(this)) {
			new DbInitializerTask(this, this, new CityDatabaseHelper(this).getWritableDatabase()).execute(new Void[0]);
		} else {
			onCompleted();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onCompleted() {
		Log.i(TAG, "Db successfully initialized!");
		Util.setDbInitialized(this, true);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onError() {
		Log.e(TAG, "Db failed to initialize!");

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { CityTable.COLUMN_NAME, CityTable.COLUMN_LAT, CityTable.COLUMN_LNG, CityTable.COLUMN_COUNTRY, CityTable.COLUMN_ID };
		CursorLoader cursorLoader = new CursorLoader(this, CityProvider.CONTENT_URI, projection, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		cities = new ArrayList<City>();
		Log.e(TAG, "Start:" + System.currentTimeMillis());
		while (cursor.moveToNext()) {
			cities.add(City.fromCursor(cursor));
		}
		Log.e(TAG, "End:" + System.currentTimeMillis());
		for (final City city : cities) {
			DataManager.getInstance().getHourlyForecast(city, new HourlyForecastListener() {
				
				@Override
				public void onSuccess(HourlyForecast hourlyForecast) {
					city.setHourlyForecast(hourlyForecast);
					getContentResolver().u
				}
				
				@Override
				public void onError(int code) {
					// TODO Auto-generated method stub
					
				}
			});
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

}
