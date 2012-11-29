/*
 * Copyright 2012 JNRain
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
package org.jnrain.mobile;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class SplashActivity extends RoboActivity {
	public static final int SPLASH_DURATION_MS = 3000;

	@InjectView(R.id.imageSplash)
	protected ImageView imgSplash;
	private Thread splashThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		// The thread to wait for splash screen events
		splashThread = new Thread() {
			@Override
			public void run() {
				try {
					synchronized (this) {
						// Wait given period of time
						wait(SPLASH_DURATION_MS);
					}
				} catch (InterruptedException ex) {
				}

				// Run next activity
				Intent intent = new Intent();
				intent.setClass(SplashActivity.this,
						GlobalHotPostsListActivity.class);
				startActivity(intent);

				// finish off self
				finish();
			}
		};

		splashThread.start();
	}

}
