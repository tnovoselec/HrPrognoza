package com.tnovoselec.hrprognoza.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CityDatabaseHelper extends SQLiteOpenHelper {
	
	  private static final String DATABASE_NAME = "hr_prognoza.db";
	  private static final int DATABASE_VERSION = 3;


	public CityDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		CityTable.onCreate(db);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		CityTable.onUpgrade(db, oldVersion, newVersion);
	}

}
