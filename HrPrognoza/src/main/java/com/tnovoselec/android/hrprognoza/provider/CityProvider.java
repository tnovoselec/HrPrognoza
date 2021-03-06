package com.tnovoselec.android.hrprognoza.provider;

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

import com.tnovoselec.android.hrprognoza.db.CityTable;
import com.tnovoselec.android.hrprognoza.db.DatabaseHelper;


public class CityProvider extends ContentProvider {

	static final String TAG = "CityProvider";

	private DatabaseHelper database;

	// Used for the UriMacher
	private static final int CITIES = 10;
	private static final int CITY_ID = 20;

	private static final String AUTHORITY = "com.tnovoselec.hrprognoza.cityprovider";

	private static final String BASE_PATH = "cities";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/cities";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/city";

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, CITIES);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", CITY_ID);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
		case CITIES:
			rowsDeleted = sqlDB.delete(CityTable.TABLE_CITIES, selection, selectionArgs);
			break;
		case CITY_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(CityTable.TABLE_CITIES, CityTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(CityTable.TABLE_CITIES, CityTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
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
		case CITIES:
			id = sqlDB.insert(CityTable.TABLE_CITIES, null, values);
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
		queryBuilder.setTables(CityTable.TABLE_CITIES);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case CITIES:
			break;
		case CITY_ID:
			// Adding the ID to the original query
			queryBuilder.appendWhere(CityTable.COLUMN_ID + "=" + uri.getLastPathSegment());
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
		case CITIES:
			rowsUpdated = sqlDB.update(CityTable.TABLE_CITIES, values, selection, selectionArgs);
			break;
		case CITY_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(CityTable.TABLE_CITIES, values, CityTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(CityTable.TABLE_CITIES, values, CityTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkColumns(String[] projection) {
		String[] available = { CityTable.COLUMN_NAME, CityTable.COLUMN_LAT, CityTable.COLUMN_LNG, CityTable.COLUMN_COUNTRY, CityTable.COLUMN_ID,
				CityTable.COLUMN_SELECTED, CityTable.COLUMN_TIMESTAMP };
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
