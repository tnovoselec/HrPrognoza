package com.tnovoselec.android.hrprognoza.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownloader
{
	public static void loadImageIntoViewAsync(ImageView view, String imageUrl, OnImageLoadedListener listener)
	{
		LoadImageAsyncTask loadImageTask = new LoadImageAsyncTask(imageUrl, view, listener);
		loadImageTask.execute();
	}

	public interface OnImageLoadedListener
	{
		public void onImageLoaded(String imageUrl, Bitmap bitmap, ImageView imageView);
	}

	private static class LoadImageAsyncTask extends AsyncTask<Void, Void, Bitmap>
	{
		private ImageView _imageView;
		private String _imageUrl;
		private OnImageLoadedListener _listener;

		public LoadImageAsyncTask(String imageUrl, ImageView imageView, OnImageLoadedListener listener)
		{
			_imageUrl = imageUrl;
			_imageView = imageView;
			_listener = listener;
		}

		@Override
		protected Bitmap doInBackground(Void... voids)
		{
			try
			{
				// TODO: replace statis method getInputStream with HttpClient call
				InputStream is = getInputStream(_imageUrl);
				Bitmap bitmap = null;
				if (is != null)
				{
					bitmap = BitmapFactory.decodeStream(is);
				}
				return bitmap;
			}
			catch (IOException e)
			{
				return null;
			}
		}

		@Override
		protected void onPostExecute(Bitmap bitmap)
		{
			_listener.onImageLoaded(_imageUrl, bitmap, _imageView);
		}
	}

	public static InputStream getInputStream(String url) throws IOException
	{
		InputStream inputStream = null;

		// Set up a new set of basic params and make sure the socket buffer size is a fixed size (8k should be plenty)
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setSocketBufferSize(params, 8192);

		// create a new HttpClient, DefaultHttpClient is fine
		DefaultHttpClient httpClient = new DefaultHttpClient(params);

		HttpContext localHttpContext = new BasicHttpContext();

		HttpRequestBase requestBase = new HttpGet(url);

		requestBase.addHeader("User-Agent", "Mozilla/5.0 (SmartScreens; U; Linux) Metro/AN1.0.0.0.1 0.0.0.1");
		try
		{
			HttpResponse response = httpClient.execute(requestBase, localHttpContext);
			HttpEntity entity = response.getEntity();

			if (entity != null)
			{
				inputStream = entity.getContent();
			}
			else
			{
				throw new IOException("IOException in response.getEntity(). URL: " + url);
			}


		}
		catch (Exception e)
		{
			Log.e("Error", "Error getting inputstream", e);
		}

		return inputStream;
	}
}
