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

import org.jnrain.mobile.network.PostsListRequest;
import org.jnrain.weiyu.collection.ListPosts;
import org.jnrain.weiyu.entity.Post;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class PostsListActivity extends SpicedRoboActivity {
	private String _brd_id;
	private ListPosts _posts;

	private static final String TAG = "PostsListActivity";
	private static final String CACHE_KEY = "brds_json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_posts_list);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		this._brd_id = intent.getStringExtra(BoardListActivity.BRD_ID);

		// update title of action bar
		getSupportActionBar().setTitle(this._brd_id);

		// fetch thread list
		spiceManager.execute(new PostsListRequest(this._brd_id), CACHE_KEY,
				DurationInMillis.ONE_MINUTE, new PostsListRequestListener());
	}

	private class PostsListRequestListener implements
			RequestListener<ListPosts> {
		@Override
		public void onRequestFailure(SpiceException arg0) {
			Log.d(TAG, "err on req: " + arg0.toString());
		}

		@Override
		public void onRequestSuccess(ListPosts posts) {
			Log.v(TAG, "got posts list: " + posts.toString());
			_posts = posts;

			ListView lv = (ListView) findViewById(R.id.listPosts);
			PostsListAdapter adapter = new PostsListAdapter(
					getApplicationContext(), _posts);
			lv.setAdapter(adapter);

			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Post post = _posts.getPosts().get(position);

					Log.i(TAG, "clicked: " + position + ", id=" + id
							+ ", post=" + post.toString());
				}
			});
		}
	}
}
