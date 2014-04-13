package com.tnovoselec.android.hrprognoza.async;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.tnovoselec.android.hrprognoza.Config;

public class DbInitializerTask extends AsyncTask<Void, Void, Void> {

	private static String TAG = DbInitializerTask.class.getSimpleName();

	public interface DbInitializerListener {
		void onCompleted();

		void onError();
	}

	private Context context;
	private DbInitializerListener listener;
	private SQLiteDatabase database;
	private boolean failed = false;

	public DbInitializerTask(Context context, DbInitializerListener listener, SQLiteDatabase database) {
		this.listener = listener;
		this.context = context;
		this.database = database;
	}

	@Override
	protected Void doInBackground(Void... params) {
		executeInserts(getInserts());
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if (listener != null) {
			if (!failed) {
				listener.onCompleted();
			} else {
				listener.onError();
			}
		}
	}

	public void setDbInitializerListener(DbInitializerListener listener) {
		this.listener = listener;
	}

	private List<String> getInserts() {
		BufferedReader reader = null;
		String line = null;
		List<String> inserts = new ArrayList<String>();
		try {
			InputStream is = context.getAssets().open(Config.CITIES_SCRIPT);
			reader = new BufferedReader(new InputStreamReader(is));

			while ((line = reader.readLine()) != null) {
				inserts.add(line);
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			failed = true;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
				}
			}
		}
		return inserts;
	}

	private void executeInserts(List<String> inserts) {
		for (String insert : inserts) {
			Log.e(TAG, insert);
			database.execSQL(insert);
		}
	}
}
