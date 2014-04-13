package com.tnovoselec.android.hrprognoza.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CityTable {
	

	  // Database table
	public static final String TABLE_CITIES = "cities";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LAT = "lat";
	public static final String COLUMN_LNG = "lng";
	public static final String COLUMN_COUNTRY = "country";
	public static final String COLUMN_SELECTED = "selected";

	  // Database creation SQL statement
	  private static final String TABLE_CREATE = "create table " 
	      + TABLE_CITIES
	      + "(" 
	      + COLUMN_ID + " integer primary key, " 
	      + COLUMN_NAME + " text not null, " 
	      + COLUMN_COUNTRY + " text not null," 
	      + COLUMN_LAT + " real not null,"
	      + COLUMN_SELECTED + " integer default 0,"
	      + COLUMN_LNG + " real not null,"
	      + COLUMN_TIMESTAMP + " integer default 0" 
	      + ");";

	  public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(TABLE_CREATE);
	  }

	  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
	    Log.w(CityTable.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_CITIES);
	    onCreate(database);
	  }

}
