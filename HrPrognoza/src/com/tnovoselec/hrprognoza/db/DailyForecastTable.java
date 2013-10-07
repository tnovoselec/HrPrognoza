package com.tnovoselec.hrprognoza.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DailyForecastTable {
	
	  // Database table
	public static final String TABLE_DAILY_FORECASTS = "daily_forecasts";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_CITY_ID = "city_id";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String COLUMN_PRESSURE = "pressure";
	public static final String COLUMN_HUMIDITY = "humidity";
	public static final String COLUMN_TEMP_DAY = "temp_day";
	public static final String COLUMN_TEMP_MAX = "temp_max";
	public static final String COLUMN_TEMP_MIN = "temp_min";
	public static final String COLUMN_TEMP_MORNING = "temp_morning";
	public static final String COLUMN_TEMP_NIGHT = "temp_night";
	public static final String COLUMN_TEMP_EVE = "temp_eve";
	public static final String COLUMN_WEATHER_ID = "weather_id";
	public static final String COLUMN_WEATHER_MAIN= "weather_main";
	public static final String COLUMN_WEATHER_DESCRIPTION = "weather_description";
	public static final String COLUMN_WEATHER_ICON = "weather_icon";
	public static final String COLUMN_SPEED = "speed";
	public static final String COLUMN_DEG = "deg";
	public static final String COLUMN_CLOUDS = "clouds";
	public static final String COLUMN_RAIN = "rain";

	  // Database creation SQL statement
	  private static final String TABLE_CREATE = "create table " 
	      + TABLE_DAILY_FORECASTS
	      + "(" 
	      + COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_CITY_ID + " integer not null, " 
	      + COLUMN_TIMESTAMP + " integer not null," 
	      + COLUMN_PRESSURE + " real,"
	      + COLUMN_HUMIDITY + " real ,"
	      + COLUMN_TEMP_DAY+ " real ,"
	      + COLUMN_TEMP_MAX + " real,"
	      + COLUMN_TEMP_MIN + " real,"
	      + COLUMN_TEMP_MORNING + " real,"
	      + COLUMN_TEMP_NIGHT + " real,"
	      + COLUMN_TEMP_EVE + " real,"
	      + COLUMN_WEATHER_ID + " integer not null," 
	      + COLUMN_WEATHER_MAIN + " text,"
	      + COLUMN_WEATHER_DESCRIPTION + " text,"
	      + COLUMN_WEATHER_ICON + " text,"
	      + COLUMN_SPEED + " real,"
	      + COLUMN_DEG + " real,"
	      + COLUMN_CLOUDS + " real,"
	      + COLUMN_RAIN + " real"
	      + ");";

	  public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(TABLE_CREATE);
	  }

	  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
	    Log.w(CityTable.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY_FORECASTS);
	    onCreate(database);
	  }


}
