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

import org.jnrain.mobile.network.ThreadListRequest;
import org.jnrain.weiyu.collection.ListPosts;
import org.jnrain.weiyu.entity.Post;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ThreadListActivity extends SpicedRoboActivity {
	@InjectView(R.id.listPosts)
	LoadMoreListView listPosts;

	public static final String THREAD_ID = "org.jnrain.mobile.THREAD_ID";
	public static final String THREAD_TITLE = "org.jnrain.mobile.THREAD_TITLE";

	private String _brd_id;
	private ListPosts _posts;
	private int _page;

	private ThreadListAdapter _adapter;

	private static final String TAG = "ThreadListActivity";
	private static final String CACHE_KEY_PREFIX = "brd_json_";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thread_list);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		this._brd_id = intent.getStringExtra(BoardListActivity.BRD_ID);
		this._page = 1;

		// update title of action bar
		getSupportActionBar().setTitle(this._brd_id);

		// fetch thread list
		makeRequest(_page);

		// load more handler
		listPosts.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				_page++;
				makeRequest(_page);
			}
		});
	}

	public void makeRequest(int page) {
		spiceManager
				.execute(
						new ThreadListRequest(this._brd_id, page),
						CACHE_KEY_PREFIX + this._brd_id + "_p"
								+ Integer.toString(page),
						DurationInMillis.ONE_MINUTE,
						new ThreadListRequestListener());
	}

	public synchronized void updateData() {
		_adapter = new ThreadListAdapter(getApplicationContext(), _posts);
		listPosts.setAdapter(_adapter);

		listPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Post post = _posts.getPosts().get(position);

				Log.i(TAG, "clicked: " + position + ", id=" + id + ", post="
						+ post.toString());

				Intent intent = new Intent(ThreadListActivity.this,
						ReadThreadActivity.class);
				intent.putExtra(BoardListActivity.BRD_ID, post.getBoard());
				intent.putExtra(THREAD_ID, post.getID());
				intent.putExtra(THREAD_TITLE, post.getTitle());

				startActivity(intent);
			}
		});
	}

	private class ThreadListRequestListener implements
			RequestListener<ListPosts> {
		@Override
		public void onRequestFailure(SpiceException arg0) {
			Log.d(TAG, "err on req: " + arg0.toString());
		}

		@Override
		public void onRequestSuccess(ListPosts posts) {
			Log.v(TAG, "got posts list: " + posts.toString());
			if (_posts == null) {
				_posts = posts;

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						updateData();
					}
				});
			} else {
				// update the current list instead
				runOnUiThread(new Runnable() {
					private ListPosts _posts;

					public Runnable setPosts(ListPosts posts) {
						this._posts = posts;
						return this;
					}

					@Override
					public void run() {
						ThreadListActivity.this._posts.getPosts().addAll(
								_posts.getPosts());
						_adapter.notifyDataSetChanged();
					}
				}.setPosts(posts));
			}
		}
	}
}
