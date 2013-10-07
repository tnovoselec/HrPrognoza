package com.tnovoselec.hrprognoza.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;
import com.tnovoselec.hrprognoza.db.HourlyForecastTable;

public class DailyForecast {

	@SerializedName("list")
	private ArrayList<Forecast> forecasts;

	public List<Forecast> getForecasts() {
		return forecasts;
	}

	public void setForecasts(ArrayList<Forecast> forecasts) {
		this.forecasts = forecasts;
	}

	public class Forecast {

		private int cityId;

		@SerializedName("dt")
		private long timestamp;

		@SerializedName("pressure")
		private float pressure;

		@SerializedName("humidity")
		private float humidity;

		@SerializedName("temp")
		private Temp temp;

		@SerializedName("weather")
		private ArrayList<Weather> weather;

		@SerializedName("speed")
		private float speed;

		@SerializedName("deg")
		private float deg;

		@SerializedName("clouds")
		private float clouds;

		@SerializedName("rain")
		private float rain;

		public int getCityId() {
			return cityId;
		}

		public void setCityId(int cityId) {
			this.cityId = cityId;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public float getPressure() {
			return pressure;
		}

		public void setPressure(float pressure) {
			this.pressure = pressure;
		}

		public float getHumidity() {
			return humidity;
		}

		public void setHumidity(float humidity) {
			this.humidity = humidity;
		}

		public Temp getTemp() {
			return temp;
		}

		public void setTemp(Temp temp) {
			this.temp = temp;
		}

		public List<Weather> getWeather() {
			return weather;
		}

		public void setWeather(ArrayList<Weather> weather) {
			this.weather = weather;
		}

		public Weather getFirstWether() {
			return this.weather != null ? this.weather.get(0) : null;
		}

		public float getSpeed() {
			return speed;
		}

		public void setSpeed(float speed) {
			this.speed = speed;
		}

		public float getDeg() {
			return deg;
		}

		public void setDeg(float deg) {
			this.deg = deg;
		}

		public float getClouds() {
			return clouds;
		}

		public void setClouds(float clouds) {
			this.clouds = clouds;
		}

		public float getRain() {
			return rain;
		}

		public void setRain(float rain) {
			this.rain = rain;
		}

		public class Temp {

			@SerializedName("max")
			private float max;

			@SerializedName("min")
			private float min;

			@SerializedName("day")
			private float day;

			@SerializedName("eve")
			private float eve;

			@SerializedName("morn")
			private float morn;

			@SerializedName("night")
			private float night;

			public float getMax() {
				return max;
			}

			public void setMax(float max) {
				this.max = max;
			}

			public float getMin() {
				return min;
			}

			public void setMin(float min) {
				this.min = min;
			}

			public float getDay() {
				return day;
			}

			public void setDay(float day) {
				this.day = day;
			}

			public float getEve() {
				return eve;
			}

			public void setEve(float eve) {
				this.eve = eve;
			}

			public float getMorn() {
				return morn;
			}

			public void setMorn(float morn) {
				this.morn = morn;
			}

			public float getNight() {
				return night;
			}

			public void setNight(float night) {
				this.night = night;
			}

		}

		public class Weather {

			@SerializedName("id")
			private int id;

			@SerializedName("icon")
			private String icon;

			@SerializedName("description")
			private String description;

			@SerializedName("main")
			private String main;

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public String getIcon() {
				return icon;
			}

			public void setIcon(String icon) {
				this.icon = icon;
			}

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}

			public String getMain() {
				return main;
			}

			public void setMain(String main) {
				this.main = main;
			}
		}

		public ContentValues getContentValues() {
			ContentValues cv = new ContentValues();
			cv.put(HourlyForecastTable.COLUMN_CITY_ID, this.getCityId());
			cv.put(HourlyForecastTable.COLUMN_CLOUDS, this.getClouds());
			cv.put(HourlyForecastTable.COLUMN_DEG, this.getDeg());
			cv.put(HourlyForecastTable.COLUMN_HUMIDITY, this.getHumidity());
			// cv.put(HourlyForecastTable.COLUMN_ID, this.getCityId());
			cv.put(HourlyForecastTable.COLUMN_PRESSURE, this.getPressure());
			cv.put(HourlyForecastTable.COLUMN_RAIN, this.getRain());
			cv.put(HourlyForecastTable.COLUMN_SPEED, this.getSpeed());
			cv.put(HourlyForecastTable.COLUMN_TEMP_DAY, this.getTemp().getDay());
			cv.put(HourlyForecastTable.COLUMN_TEMP_EVE, this.getTemp().getEve());
			cv.put(HourlyForecastTable.COLUMN_TEMP_MAX, this.getTemp().getMax());
			cv.put(HourlyForecastTable.COLUMN_TEMP_MIN, this.getTemp().getMin());
			cv.put(HourlyForecastTable.COLUMN_TEMP_MORNING, this.getTemp().getMorn());
			cv.put(HourlyForecastTable.COLUMN_TEMP_NIGHT, this.getTemp().getNight());
			cv.put(HourlyForecastTable.COLUMN_TIMESTAMP, this.getTimestamp());
			cv.put(HourlyForecastTable.COLUMN_WEATHER_DESCRIPTION, this.getFirstWether().getDescription());
			cv.put(HourlyForecastTable.COLUMN_WEATHER_ICON, this.getFirstWether().getIcon());
			cv.put(HourlyForecastTable.COLUMN_WEATHER_ID, this.getFirstWether().getId());
			cv.put(HourlyForecastTable.COLUMN_WEATHER_MAIN, this.getFirstWether().getMain());
			return cv;
		}
	}

}
