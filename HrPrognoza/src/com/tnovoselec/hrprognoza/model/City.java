package com.tnovoselec.hrprognoza.model;

import com.tnovoselec.hrprognoza.db.CityTable;

import android.content.ContentValues;
import android.database.Cursor;

public class City {
	private int id;
	private String name;
	private double lat;
	private double lng;
	private String country;

	private HourlyForecast hourlyForecast;
	private DailyForecast dailyForecast;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public HourlyForecast getHourlyForecast() {
		return hourlyForecast;
	}

	public void setHourlyForecast(HourlyForecast hourlyForecast) {
		this.hourlyForecast = hourlyForecast;
	}

	public DailyForecast getDailyForecast() {
		return dailyForecast;
	}

	public void setDailyForecast(DailyForecast dailyForecast) {
		this.dailyForecast = dailyForecast;
	}

	public ContentValues getContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(CityTable.COLUMN_COUNTRY, getCountry());
		cv.put(CityTable.COLUMN_COUNTRY, getCountry());
		cv.put(CityTable.COLUMN_COUNTRY, getCountry());
		cv.put(CityTable.COLUMN_COUNTRY, getCountry());
		return cv;
	}

	public static City fromCursor(Cursor cursor) {
		City city = new City();
		city.setCountry(cursor.getString(cursor.getColumnIndex(CityTable.COLUMN_COUNTRY)));
		city.setId(cursor.getInt(cursor.getColumnIndex(CityTable.COLUMN_ID)));
		city.setLat(cursor.getDouble(cursor.getColumnIndex(CityTable.COLUMN_LAT)));
		city.setLng(cursor.getDouble(cursor.getColumnIndex(CityTable.COLUMN_LNG)));
		city.setName(cursor.getString(cursor.getColumnIndex(CityTable.COLUMN_NAME)));
		return city;
	}

}
