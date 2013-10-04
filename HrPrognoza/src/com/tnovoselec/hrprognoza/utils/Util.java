package com.tnovoselec.hrprognoza.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class Util {

	private static String KEY_CITY_DB_INITIALIZED = "city_db_initialized";

	public static boolean isCityDbInitialized(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_CITY_DB_INITIALIZED, false);
	}
}
