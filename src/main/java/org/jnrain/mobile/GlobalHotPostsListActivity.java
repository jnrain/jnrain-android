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

import org.jnrain.mobile.network.HotPostsListRequest;
import org.jnrain.weiyu.collection.ListHotPosts;
import org.jnrain.weiyu.entity.Post;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class GlobalHotPostsListActivity extends SpicedRoboActivity {
	@InjectView(R.id.textGlobalHotPostsStatus)
	TextView textStatus;

	@InjectResource(R.string.fail_load_global_hot_posts)
	public String LOAD_FAIL_MSG;

	private static final String TAG = "GlobalHotPostsListActivity";
	private static final String CACHE_KEY = "global_hot_json";

	private ListHotPosts _posts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_global_hot_posts_list);

		spiceManager.execute(new HotPostsListRequest(ListHotPosts.GLOBAL),
				CACHE_KEY, DurationInMillis.NEVER,
				new GlobalHotPostsListRequestListener());
	}

	private class GlobalHotPostsListRequestListener implements
			RequestListener<ListHotPosts> {
		@Override
		public void onRequestFailure(SpiceException arg0) {
			Log.e(TAG, "err on req: " + arg0.toString());
			textStatus.setText(LOAD_FAIL_MSG);
		}

		@Override
		public void onRequestSuccess(ListHotPosts posts) {
			Log.v(TAG, "got hot posts list: " + posts.toString());
			textStatus.setText("");
			_posts = posts;

			ListView lv = (ListView) findViewById(R.id.listGlobalHotPosts);
			HotPostsListAdapter adapter = new HotPostsListAdapter(
					getApplicationContext(), _posts);
			lv.setAdapter(adapter);

			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Post post = _posts.getPosts().get(position);

					Log.i(TAG,
							"clicked: " + position + ", post.title="
									+ post.getTitle());

					GlobalHotPostsListActivity.this.finish();

					Intent intent = new Intent(GlobalHotPostsListActivity.this,
							SectionListActivity.class);
					startActivity(intent);
				}
			});
		}
	}

}
