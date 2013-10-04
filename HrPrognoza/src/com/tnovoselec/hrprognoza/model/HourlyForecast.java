package com.tnovoselec.hrprognoza.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class HourlyForecast implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1103684303375905653L;
	@SerializedName("list")
	private ArrayList<Forecast> forecasts;

	public List<Forecast> getForecasts() {
		return forecasts;
	}

	public void setForecasts(ArrayList<Forecast> forecasts) {
		this.forecasts = forecasts;
	}

	public class Forecast {
		@SerializedName("dt")
		private long dt;

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

		public long getDt() {
			return dt;
		}

		public void setDt(long dt) {
			this.dt = dt;
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

		public class Temp implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = -4864520288192067500L;

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

		public class Weather implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = -3301791705552141816L;

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
	}
}
