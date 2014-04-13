package com.tnovoselec.android.hrprognoza;

import android.app.Application;
import android.graphics.Bitmap.CompressFormat;

import com.tnovoselec.android.hrprognoza.data.DataManager;
import com.tnovoselec.android.hrprognoza.utils.ImageCacheManager;
import com.tnovoselec.android.hrprognoza.utils.RequestManager;


public class HrPrognozaApp extends Application {
	private static int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 10;
	private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
	private static int DISK_IMAGECACHE_QUALITY = 100;

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	private void init() {
		RequestManager.init(this);
		DataManager.getInstance().init(this);
		createImageCache();
	}

	private void createImageCache() {
		ImageCacheManager.getInstance().init(this, this.getPackageCodePath(), DISK_IMAGECACHE_SIZE, DISK_IMAGECACHE_COMPRESS_FORMAT, DISK_IMAGECACHE_QUALITY,
				ImageCacheManager.CacheType.MEMORY);
	}
}
