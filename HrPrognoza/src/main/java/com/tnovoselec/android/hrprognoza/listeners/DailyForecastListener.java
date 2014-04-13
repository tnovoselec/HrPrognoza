package com.tnovoselec.android.hrprognoza.listeners;


import com.tnovoselec.android.hrprognoza.model.DailyForecast;

public interface DailyForecastListener {
	void onSuccess(DailyForecast dailyForecast);

	void onError(int code);
}
