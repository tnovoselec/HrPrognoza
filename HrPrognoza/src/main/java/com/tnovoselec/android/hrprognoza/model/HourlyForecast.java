package com.tnovoselec.android.hrprognoza.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;
import com.tnovoselec.android.hrprognoza.db.HourlyForecastTable;

public class HourlyForecast {

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

		@SerializedName("main")
		private Main main;

		@SerializedName("weather")
		private List<Weather> weather;

		@SerializedName("wind")
		private Wind wind;

		@SerializedName("clouds")
		private Clouds clouds;

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

		public Main getMain() {
			return main;
		}

		public void setMain(Main main) {
			this.main = main;
		}

		public List<Weather> getWeather() {
			return weather;
		}

		public Weather getFirstWeather() {
			return this.weather != null ? this.weather.get(0) : null;
		}

		public void setWeather(List<Weather> weather) {
			this.weather = weather;
		}

		public Wind getWind() {
			return wind;
		}

		public void setWind(Wind wind) {
			this.wind = wind;
		}

		public Clouds getClouds() {
			return clouds;
		}

		public void setClouds(Clouds clouds) {
			this.clouds = clouds;
		}

		public ContentValues getContentValues() {
			ContentValues cv = new ContentValues();
			cv.put(HourlyForecastTable.COLUMN_CITY_ID, this.getCityId());
			cv.put(HourlyForecastTable.COLUMN_CLOUDS, this.getClouds().getAll());
			cv.put(HourlyForecastTable.COLUMN_DEG, this.getWind().getDeg());
			cv.put(HourlyForecastTable.COLUMN_HUMIDITY, this.getMain().getHumidity());
			// cv.put(HourlyForecastTable.COLUMN_ID, this.getCityId());
			cv.put(HourlyForecastTable.COLUMN_PRESSURE, this.getMain().getPressure());
			// cv.put(HourlyForecastTable.COLUMN_RAIN, this.getRain());
			cv.put(HourlyForecastTable.COLUMN_SPEED, this.getWind().getSpeed());
			cv.put(HourlyForecastTable.COLUMN_TEMP, this.getMain().getTemp());
			cv.put(HourlyForecastTable.COLUMN_SEA_LEVEL, this.getMain().getSeaLevel());
			cv.put(HourlyForecastTable.COLUMN_TEMP_MAX, this.getMain().getTempMax());
			cv.put(HourlyForecastTable.COLUMN_TEMP_MIN, this.getMain().getTempMin());
			cv.put(HourlyForecastTable.COLUMN_GROUND_LEVEL, this.getMain().getGroundLevel());
			cv.put(HourlyForecastTable.COLUMN_TEMP_KF, this.getMain().getTempKf());
			cv.put(HourlyForecastTable.COLUMN_TIMESTAMP, this.getTimestamp());
			cv.put(HourlyForecastTable.COLUMN_WEATHER_DESCRIPTION, this.getFirstWeather().getDescription());
			cv.put(HourlyForecastTable.COLUMN_WEATHER_ICON, this.getFirstWeather().getIcon());
			cv.put(HourlyForecastTable.COLUMN_WEATHER_ID, this.getFirstWeather().getId());
			cv.put(HourlyForecastTable.COLUMN_WEATHER_MAIN, this.getFirstWeather().getMain());
			return cv;
		}

		public class Main {

			@SerializedName("dt")
			private float temp;

			@SerializedName("temp_min")
			private float tempMin;

			@SerializedName("temp_max")
			private float tempMax;

			@SerializedName("pressure")
			private float pressure;

			@SerializedName("sea_level")
			private float seaLevel;

			@SerializedName("grnd_level")
			private float groundLevel;

			@SerializedName("humidity")
			private float humidity;

			@SerializedName("temp_kf")
			private float tempKf;

			public float getTemp() {
				return temp;
			}

			public void setTemp(float temp) {
				this.temp = temp;
			}

			public float getTempMin() {
				return tempMin;
			}

			public void setTempMin(float tempMin) {
				this.tempMin = tempMin;
			}

			public float getTempMax() {
				return tempMax;
			}

			public void setTempMax(float tempMax) {
				this.tempMax = tempMax;
			}

			public float getPressure() {
				return pressure;
			}

			public void setPressure(float pressure) {
				this.pressure = pressure;
			}

			public float getSeaLevel() {
				return seaLevel;
			}

			public void setSeaLevel(float seaLevel) {
				this.seaLevel = seaLevel;
			}

			public float getGroundLevel() {
				return groundLevel;
			}

			public void setGroundLevel(float groundLevel) {
				this.groundLevel = groundLevel;
			}

			public float getHumidity() {
				return humidity;
			}

			public void setHumidity(float humidity) {
				this.humidity = humidity;
			}

			public float getTempKf() {
				return tempKf;
			}

			public void setTempKf(float tempKf) {
				this.tempKf = tempKf;
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

		public class Clouds {
			@SerializedName("all")
			private float all;

			public float getAll() {
				return all;
			}

			public void setAll(float all) {
				this.all = all;
			}

		}

		public class Wind {

			@SerializedName("speed")
			private float speed;

			@SerializedName("deg")
			private float deg;

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

		}
	}

}
