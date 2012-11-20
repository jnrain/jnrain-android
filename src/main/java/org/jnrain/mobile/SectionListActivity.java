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

import org.jnrain.mobile.network.SectionListRequest;
import org.jnrain.weiyu.collection.ListSections;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class SectionListActivity extends SpicedRoboActivity {
	private static final String TAG = "SectionListActivity";
	private static final String CACHE_KEY = "secs_json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_section_list);

		spiceManager.execute(new SectionListRequest(), CACHE_KEY,
				DurationInMillis.NEVER, new SectionListRequestListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_section_list, menu);
		return true;
	}

	private class SectionListRequestListener implements
			RequestListener<ListSections> {
		@Override
		public void onRequestFailure(SpiceException arg0) {
			Log.d(TAG, "err on req: " + arg0.toString());
		}

		@Override
		public void onRequestSuccess(ListSections sections) {
			// Log.v(TAG, "got section list: " + sections.toString());

			ListView lv = (ListView) findViewById(R.id.listSections);
			SectionListAdapter adapter = new SectionListAdapter(
					getApplicationContext(), sections);
			lv.setAdapter(adapter);
		}
	}

}
