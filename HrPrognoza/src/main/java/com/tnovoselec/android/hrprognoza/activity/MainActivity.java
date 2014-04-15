package com.tnovoselec.android.hrprognoza.activity;

import java.util.ArrayList;
import java.util.List;

import com.tnovoselec.android.hrprognoza.R;
import com.tnovoselec.android.hrprognoza.async.DbInitializerTask;
import com.tnovoselec.android.hrprognoza.data.DataManager;
import com.tnovoselec.android.hrprognoza.db.CityTable;
import com.tnovoselec.android.hrprognoza.db.DailyForecastTable;
import com.tnovoselec.android.hrprognoza.db.DatabaseHelper;
import com.tnovoselec.android.hrprognoza.db.HourlyForecastTable;
import com.tnovoselec.android.hrprognoza.listeners.DailyForecastListener;
import com.tnovoselec.android.hrprognoza.listeners.HourlyForecastListener;
import com.tnovoselec.android.hrprognoza.model.City;
import com.tnovoselec.android.hrprognoza.model.DailyForecast;
import com.tnovoselec.android.hrprognoza.model.HourlyForecast;
import com.tnovoselec.android.hrprognoza.provider.CityProvider;
import com.tnovoselec.android.hrprognoza.provider.DailyForecastProvider;
import com.tnovoselec.android.hrprognoza.provider.HourlyForecastProvider;
import com.tnovoselec.android.hrprognoza.utils.Util;
import com.tnovoselec.android.hrprognoza.view.ClockView;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity
    implements DbInitializerTask.DbInitializerListener, LoaderManager.LoaderCallbacks<Cursor> {

  private static String TAG = MainActivity.class.getSimpleName();

  private List<City> cities;

  private ClockView clockView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    clockView = (ClockView)findViewById(R.id.clockview);
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
    getLoaderManager().initLoader(0, null, this);
    //        Intent i = new Intent(this, UpdateService.class);
    //        startService(i);
  }

  @Override
  public void onError() {
    Log.e(TAG, "Db failed to initialize!");
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String[] projection = { CityTable.COLUMN_NAME, CityTable.COLUMN_LAT, CityTable.COLUMN_LNG, CityTable.COLUMN_COUNTRY,
        CityTable.COLUMN_ID, CityTable.COLUMN_SELECTED, CityTable.COLUMN_TIMESTAMP };
    CursorLoader cursorLoader = new CursorLoader(this, CityProvider.CONTENT_URI, projection, null, null, null);
    return cursorLoader;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    cities = new ArrayList<City>();
    while (cursor.moveToNext()) {
      cities.add(City.fromCursor(cursor));
      break;
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
    getContentResolver().delete(HourlyForecastProvider.CONTENT_URI, HourlyForecastTable.COLUMN_CITY_ID + "=" + cityId,
        null);
    for (HourlyForecast.Forecast forecast : hourlyForecast.getForecasts()) {
      forecast.setCityId(cityId);
      getContentResolver().insert(HourlyForecastProvider.CONTENT_URI, forecast.getContentValues());
    }
    clockView.setData(hourlyForecast);
  }

  private synchronized void insertDailyForecastsToDb(DailyForecast dailyForecast, int cityId) {
    getContentResolver().delete(DailyForecastProvider.CONTENT_URI, DailyForecastTable.COLUMN_CITY_ID + "=" + cityId,
        null);
    for (DailyForecast.Forecast forecast : dailyForecast.getForecasts()) {
      forecast.setCityId(cityId);
      getContentResolver().insert(DailyForecastProvider.CONTENT_URI, forecast.getContentValues());
    }
  }

  @Override
  public void onLoaderReset(Loader<Cursor> arg0) {

  }
}
