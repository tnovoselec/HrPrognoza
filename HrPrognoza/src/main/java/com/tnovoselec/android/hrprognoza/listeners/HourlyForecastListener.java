package com.tnovoselec.android.hrprognoza.listeners;

import com.tnovoselec.android.hrprognoza.model.HourlyForecast;

public interface HourlyForecastListener {

	void onSuccess(HourlyForecast hourlyForecast);

	void onError(int code);

}
