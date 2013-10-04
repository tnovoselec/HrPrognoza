package com.tnovoselec.hrprognoza.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.tnovoselec.hrprognoza.R;
import com.tnovoselec.hrprognoza.async.DbInitializerTask;
import com.tnovoselec.hrprognoza.async.DbInitializerTask.DbInitializerListener;
import com.tnovoselec.hrprognoza.db.CityDatabaseHelper;
import com.tnovoselec.hrprognoza.utils.Util;

public class MainActivity extends Activity implements DbInitializerListener {

	private static String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (!Util.isCityDbInitialized(this)) {
			new DbInitializerTask(this, this, new CityDatabaseHelper(this).getWritableDatabase()).execute(new Void[0]);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onCompleted() {
		Log.i(TAG, "Db successfully initialized!");
	}

	@Override
	public void onError() {
		Log.e(TAG, "Db failed to initialize!");

	}

}
