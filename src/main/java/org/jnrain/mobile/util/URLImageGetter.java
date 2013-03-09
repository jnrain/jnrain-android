/*
 * Copyright 2013 JNRain
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jnrain.mobile.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jnrain.mobile.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/*
 * this code taken from
 * stackoverflow.com/questions/7424512/android-html-imagegetter-as-asynctask
 */
public class URLImageGetter implements ImageGetter {
	private static final String TAG = "URLImageGetter";

	protected Context _c;
	protected View _container;
	protected String _baseURL;
	protected Activity _activity;

	/***
	 * Construct the URLImageParser which will execute AsyncTask and refresh the
	 * container
	 * 
	 * @param t
	 * @param c
	 * @param baseURL
	 */
	public URLImageGetter(Activity activity, View t, Context c, String baseURL) {
		_activity = activity;
		_c = c;
		_container = t;
		_baseURL = baseURL;
	}

	public Drawable getDrawable(String source) {
		Log.d(TAG, "Image request: " + source);
		URLDrawable urlDrawable = new URLDrawable(_activity.getResources()
				.getDrawable(R.drawable.loading_pic));

		// get the actual source
		// TODO: migrate to RoboSpice
		ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);

		asyncTask.execute(source);

		// return reference to URLDrawable where I will change with actual image
		// from
		// the src tag
		return urlDrawable;
	}

	public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
		URLDrawable urlDrawable;

		public ImageGetterAsyncTask(URLDrawable d) {
			this.urlDrawable = d;
		}

		@Override
		protected Drawable doInBackground(String... params) {
			String source = params[0];
			return fetchDrawable(source);
		}

		@Override
		protected void onPostExecute(Drawable result) {
			if (result == null) {
				// Fetch failed, show the failed image instead
				// TODO: assign a "failed" image here
			} else {
				// set the correct bound according to the result from HTTP call
				urlDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(),
						0 + result.getIntrinsicHeight());

				// change the reference of the current drawable to the result
				// from the HTTP call
				urlDrawable.setDrawable(result);
			}

			// // redraw the image by invalidating the container
			// URLImageGetter.this._container.invalidate();

			// according to this SO question,
			// stackoverflow.com/questions/7739649/async-imagegetter-not-functioning-properly
			// we need to force a reflow of the container.
			// TODO: extract interface for reusability here
			TextView v = (TextView) _container;
			v.setText(v.getText());
		}

		/***
		 * Get the Drawable from URL
		 * 
		 * @param urlString
		 * @return
		 */
		public Drawable fetchDrawable(String urlString) {
			try {
				InputStream is = fetch(urlString);
				Drawable drawable = Drawable.createFromStream(is, "src");
				drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth(),
						0 + drawable.getIntrinsicHeight());
				return drawable;
			} catch (Exception e) {
				return null;
			}
		}

		private InputStream fetch(String urlString)
				throws MalformedURLException, IOException {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			String absoluteURL;

			if (urlString.startsWith("http://")
					|| urlString.startsWith("https://")) {
				absoluteURL = urlString;
			} else {
				absoluteURL = _baseURL + urlString;
			}

			HttpGet request = new HttpGet(absoluteURL);
			HttpResponse response = httpClient.execute(request);

			return response.getEntity().getContent();
		}
	}
}
