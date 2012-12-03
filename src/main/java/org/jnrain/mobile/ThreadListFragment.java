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
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ThreadListFragment extends RoboSherlockFragment {
	@InjectView(R.id.listPosts)
	ListView listPosts;

	private ThreadListActivityListener _listener;
	private String _brd_id;
	private ListPosts _posts;
	private int _page;

	private ThreadListAdapter _adapter;

	private static final String TAG = "ThreadListFragment";
	private static final String CACHE_KEY_PREFIX = "brd_json_";

	public ThreadListFragment(String brd_id, int page) {
		this._brd_id = brd_id;
		this._page = page;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			_listener = (ThreadListActivityListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ThreadListActivityListener");
		}
	}

	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// this._brd_id = getArguments().getString(BoardListActivity.BRD_ID);
	// this._page = getArguments().getInt(ThreadListActivity.PAGE, 1);
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.thread_list_fragment, container,
				false);

		// fetch a page of thread list
		makeRequest(this._page);

		return view;
	}

	public void makeRequest(int page) {
		_listener
				.makeSpiceRequest(
						new ThreadListRequest(this._brd_id, page),
						CACHE_KEY_PREFIX + this._brd_id + "_p"
								+ Integer.toString(page),
						DurationInMillis.ONE_MINUTE,
						new ThreadListRequestListener());
	}

	public synchronized void updateData() {
		_adapter = new ThreadListAdapter(this.getActivity()
				.getApplicationContext(), _posts);
		listPosts.setAdapter(_adapter);

		listPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Post post = _posts.getPosts().get(position);

				Log.i(TAG, "clicked: " + position + ", id=" + id + ", post="
						+ post.toString());

				_listener.sendReadThreadIntent(post);
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
			_posts = posts;

			ThreadListFragment.this.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updateData();
				}
			});
		}
	}
}
