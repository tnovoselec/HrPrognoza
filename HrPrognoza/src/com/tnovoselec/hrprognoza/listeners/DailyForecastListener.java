package com.tnovoselec.hrprognoza.listeners;

import com.tnovoselec.hrprognoza.model.DailyForecast;

public interface DailyForecastListener {
	void onSuccess(DailyForecast dailyForecast);

	void onError(int code);
}
