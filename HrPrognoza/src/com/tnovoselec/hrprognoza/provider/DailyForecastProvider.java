package com.tnovoselec.hrprognoza.provider;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.tnovoselec.hrprognoza.db.DailyForecastTable;
import com.tnovoselec.hrprognoza.db.DatabaseHelper;

public class DailyForecastProvider extends ContentProvider{
	
	static final String TAG = "DailyForecastProvider";

	private DatabaseHelper database;

	// Used for the UriMacher
	private static final int DAILY_FORECASTS = 10;
	private static final int DAILY_FORECAST_ID = 20;

	private static final String AUTHORITY = "com.tnovoselec.hrprognoza.dailyforecastprovider";

	private static final String BASE_PATH = "dailyforecasts";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/dailyforecasts";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/dailyforecast";

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, DAILY_FORECASTS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", DAILY_FORECAST_ID);
	}


	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
		case DAILY_FORECASTS:
			rowsDeleted = sqlDB.delete(DailyForecastTable.TABLE_DAILY_FORECASTS, selection, selectionArgs);
			break;
		case DAILY_FORECAST_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(DailyForecastTable.TABLE_DAILY_FORECASTS, DailyForecastTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(DailyForecastTable.TABLE_DAILY_FORECASTS, DailyForecastTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		long id = 0;
		switch (uriType) {
		case DAILY_FORECASTS:
			id = sqlDB.insert(DailyForecastTable.TABLE_DAILY_FORECASTS, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}


	@Override
	public boolean onCreate() {
		database = new DatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// Check if the caller has requested a column which does not exists
		checkColumns(projection);

		// Set the table
		queryBuilder.setTables(DailyForecastTable.TABLE_DAILY_FORECASTS);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case DAILY_FORECASTS:
			break;
		case DAILY_FORECAST_ID:
			// Adding the ID to the original query
			queryBuilder.appendWhere(DailyForecastTable.COLUMN_ID + "=" + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case DAILY_FORECASTS:
			rowsUpdated = sqlDB.update(DailyForecastTable.TABLE_DAILY_FORECASTS, values, selection, selectionArgs);
			break;
		case DAILY_FORECAST_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(DailyForecastTable.TABLE_DAILY_FORECASTS, values, DailyForecastTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(DailyForecastTable.TABLE_DAILY_FORECASTS, values, DailyForecastTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkColumns(String[] projection) {
		String[] available = { DailyForecastTable.COLUMN_CITY_ID,DailyForecastTable.COLUMN_CLOUDS ,DailyForecastTable.COLUMN_DEG ,DailyForecastTable.COLUMN_HUMIDITY ,DailyForecastTable.COLUMN_ID ,DailyForecastTable.COLUMN_PRESSURE ,DailyForecastTable.COLUMN_RAIN ,DailyForecastTable.COLUMN_SPEED ,DailyForecastTable.COLUMN_TEMP_DAY ,DailyForecastTable.COLUMN_TEMP_EVE ,DailyForecastTable.COLUMN_TEMP_MAX ,DailyForecastTable.COLUMN_TEMP_MIN,DailyForecastTable.COLUMN_TEMP_MORNING,DailyForecastTable.COLUMN_TEMP_NIGHT,DailyForecastTable.COLUMN_TIMESTAMP,DailyForecastTable.COLUMN_WEATHER_DESCRIPTION,DailyForecastTable.COLUMN_WEATHER_ICON,DailyForecastTable.COLUMN_WEATHER_ID,DailyForecastTable.COLUMN_WEATHER_MAIN  };
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}


}
