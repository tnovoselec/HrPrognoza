package com.tnovoselec.hrprognoza.listeners;

import com.tnovoselec.hrprognoza.model.HourlyForecast;

public interface HourlyForecastListener {

	void onSuccess(HourlyForecast hourlyForecast);

	void onError(int code);

}
